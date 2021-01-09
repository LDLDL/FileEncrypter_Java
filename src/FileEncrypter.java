import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class FileEncrypter {

    //encrypted file header
    private static final byte[] HEADER = {
            0x59, 0x55, 0x4b, 0x49, //YUKI
            0x4d, 0x55, 0x47, 0x49, 0x59, 0x55, //MUGIYU
            0x4c, 0x44, 0x4c, 0x44, 0x4c, 0x44 //LDLDLD
    };

    private static final int SHA256_BUFFER_SIZE = 64;
    private static final int AES256_BUFFER_SIZE = 16;

    private static byte[] file_sha256(String file_path) throws IOException {
        byte[] sha256_buffer = new byte[SHA256_BUFFER_SIZE];

        File _f = new File(file_path);
        long read_file_size = _f.length();

        FileInputStream fis = new FileInputStream(file_path);
        InputStream is = new BufferedInputStream(fis);
        sha256 sha = new sha256();

        int _rs ;

        _rs = is.read(sha256_buffer);
        while(_rs == SHA256_BUFFER_SIZE){
            sha.stream_add(sha256_buffer, _rs);
            _rs = is.read(sha256_buffer);
        }

        if(_rs != -1) {
            sha.stream_last_block(sha256_buffer, _rs, read_file_size);
        }
        else {
            sha.stream_last_block(sha256_buffer, 0, read_file_size);
        }

        fis.close();

        return sha.getResult();
    }


    public static void encrypt(String inFile, String ouFile, String pwd) throws IOException {
        File _if = new File(inFile);
        long read_file_size = _if.length();
        File _of = new File(ouFile);
        aes256 e = new aes256(pwd);
        sha256 sha = new sha256();

        //check, if exists, do not encrypt
        if(_of.exists()){
            throw new RuntimeException("Encrypted File Exists.");
        }

        //create buffer
        byte[] buffer = new byte[AES256_BUFFER_SIZE];

        //calculate pbkdf2
        sha.stream_all(pwd);
        byte[] pb = pbkdf2.sha256_8(sha.getResult(), sha.getResult().length,4096);
        //calculate sha256
        byte[] _sha = file_sha256(inFile);

        //open file
        FileInputStream fis = new FileInputStream(_if);
        InputStream is = new BufferedInputStream(fis);
        FileOutputStream fos = new FileOutputStream(_of);
        OutputStream os = new BufferedOutputStream(fos);

        //write header to encrypted file
        os.write(HEADER);
        //write pbkdf2 stored key header
        os.write(pb);
        //write sha256 value to head
        os.write(_sha);

        int _rs;
        //read and calculate
        while(true){
            _rs = is.read(buffer);
            if (_rs < AES256_BUFFER_SIZE){
                break;
            }
            e.encrypt(buffer);
            os.write(buffer);
        }

        //calculate last block
        if(_rs > 0){
            //_rs != 0
            //fill the blank space with a number how many chars will be filled
            byte fill_num = (byte) (AES256_BUFFER_SIZE - _rs);
            Arrays.fill(buffer,_rs, AES256_BUFFER_SIZE , fill_num);
        }else{
            //_rs == 0
            //fill 16char of 16 then encrypt then write.
            Arrays.fill(buffer,(byte)16);
        }

        e.encrypt(buffer);
        os.write(buffer);

        //close file
        fis.close();
        os.flush();
        fos.flush();
        fos.close();
    }

    public static void decrypt(String inFile, String ouFile, String pwd) throws IOException {
        File _if = new File(inFile);
        File _of = new File(ouFile);
        aes256 e = new aes256(pwd);
        sha256 sha = new sha256();

        //check, if exists, do not encrypt
        if(_of.exists()){
            throw new RuntimeException("Decrypted File Exists.");
        }

        //create buffer
        byte[] buffer = new byte[AES256_BUFFER_SIZE];

        //calculate pbkdf2
        sha.stream_all(pwd);
        byte[] pb = pbkdf2.sha256_8(sha.getResult(), sha.getResult().length,4096);

        byte[] sha_result_buffer = new byte[32];

        //open file
        FileInputStream fis = new FileInputStream(_if);
        BufferedInputStream is = new BufferedInputStream(fis);

        //read header and check
        is.read(buffer);
        for(int i = 0; i < AES256_BUFFER_SIZE; ++i){
            if(buffer[i] != HEADER[i]){
                //close file
                fis.close();
                throw new RuntimeException("Not Our Encrypted File.");
            }
        }

        //read pbkdf2 header and check
        is.read(buffer);
        for(int i = 0; i < AES256_BUFFER_SIZE; ++i){
            if(buffer[i] != pb[i]){
                //close file
                fis.close();
                throw new RuntimeException("Wrong Password.");
            }
        }
        is.read(buffer);
        for(int i = 0; i < AES256_BUFFER_SIZE; ++i){
            if(buffer[i] != pb[i + 16]){
                //close file
                fis.close();
                throw new RuntimeException("Wrong Password.");
            }
        }

        //read sha256 header
        is.read(sha_result_buffer);

        FileOutputStream fos = new FileOutputStream(_of);
        BufferedOutputStream os = new BufferedOutputStream(fos);

        //read and decrypt and write
        while(is.available() > 16){
            is.read(buffer);
            e.decrypt(buffer);
            os.write(buffer);
        }

        //remove filling
        is.read(buffer);
        e.decrypt(buffer);
        if(buffer[15] < 16){
            int fn = AES256_BUFFER_SIZE - Byte.toUnsignedInt(buffer[15]);
            os.write(buffer,0,fn);
        }

        //close file
        fis.close();
        os.flush();
        fos.flush();
        fos.close();

        byte[] esha = file_sha256(ouFile);
        for(int i = 0; i < 32; ++i){
            if(sha_result_buffer[i] != esha[i]){
                throw new RuntimeException("File Broken.");
            }
        }
    }
}