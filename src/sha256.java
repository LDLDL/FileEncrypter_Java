import java.util.Arrays;

public class sha256 {

    //hash initial values
    private static final int[] hinit = {
            0x6a09e667, //h0
            0xbb67ae85, //h1
            0x3c6ef372, //h2
            0xa54ff53a, //h3
            0x510e527f, //h4
            0x9b05688c, //h5
            0x1f83d9ab, //h6
            0x5be0cd19  //h7
    };

    //hash constant values
    private static final int[] hconst = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5,
            0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3,
            0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc,
            0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7,
            0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13,
            0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3,
            0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5,
            0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208,
            0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    //the size of one block of sha256 is 512bits, 64bytes.
    private static final int SHA256_BLOCK_SIZE = 64;

    private byte[] result;
    private int[] result_32;
    private boolean finish = false;

    //ma function for sha256 calculating
    private static int ma(int x, int y, int z){
        return (x & y) ^ (x & z) ^ (y & z);
    }

    //ch function for sha256 calculating
    private static int ch(int x, int y, int z){
        return (x & y) ^ ((~x) & z);
    }

    //big sigma0 function for sha256 calculating
    private static int bsig0(int x){
        return Integer.rotateRight(x,2) ^ Integer.rotateRight(x,13) ^ Integer.rotateRight(x,22);
    }

    //big sigma1 function for sha256 calculating
    private static int bsig1(int x){
        return Integer.rotateRight(x,6) ^ Integer.rotateRight(x,11) ^ Integer.rotateRight(x,25);
    }

    //small sigma0 function for sha256 calculating
    private static int ssig0(int x){
        return Integer.rotateRight(x,7) ^ Integer.rotateRight(x,18) ^ (x >>> 3);
    }

    //small sigma1 function for sha256 calculating
    private static int ssig1(int x){
        return Integer.rotateRight(x,17) ^ Integer.rotateRight(x,19) ^ (x >>> 10);
    }

    private static void _sha256_calculate_8(byte[] msg, int offset, int[] result){
        int[] w = new int[64];

        int j=offset;

        //put byte into int
        for (int i = 0; i < 16; ++i, j+=4){
            w[i] |= (msg[j] << 24);
            w[i] |= (msg[j+1] << 16) & 0x00FF0000;
            w[i] |= (msg[j+2] << 8) & 0x0000FF00;
            w[i] |= (msg[j+3]) & 0xFF;
        }

        _sha256_calculate(w, result);
    }

    //function that calculate one block of message
    private static void _sha256_calculate(int[] w, int[] result){
        int a0,b1,c2,d3,e4,f5,g6,h7;
        int t1,t2;

        //make 64 word
        for (int i = 16; i < 64; ++i)
            w[i] = ssig1(w[i-2]) + w[i-7] + ssig0(w[i-15]) + w[i-16];

        //copy last block values
        //if this is first block, the values will be hash initial values
        a0 = result[0];
        b1 = result[1];
        c2 = result[2];
        d3 = result[3];
        e4 = result[4];
        f5 = result[5];
        g6 = result[6];
        h7 = result[7];

        //calculate
        for (int i = 0; i < 64; ++i){
            t1 = h7 + bsig1(e4) + ch(e4,f5,g6) + hconst[i] + w[i];
            t2 = bsig0(a0) + ma(a0,b1,c2);

            h7 = g6;
            g6 = f5;
            f5 = e4;
            e4 = d3 + t1;
            d3 = c2;
            c2 = b1;
            b1 = a0;
            a0 = t1 + t2;
        }

        //add this block values to last block values
        result[0] += a0;
        result[1] += b1;
        result[2] += c2;
        result[3] += d3;
        result[4] += e4;
        result[5] += f5;
        result[6] += g6;
        result[7] += h7;
    }

    //constructor
    public sha256(){
        result = new byte[32];
        result_32 = new int[8];

        //set hash initial value to result array
        System.arraycopy(hinit, 0, result_32, 0, 8);
    }

    public void clear(){
        //set hash initial value to result array
        System.arraycopy(hinit, 0, result_32, 0, 8);

        Arrays.fill(result, (byte)0);

        finish = false;
    }

    //getter
    public byte[] getResult(){
        return result;
    }

    public int[] getResult_32(){
        return result_32;
    }

    public boolean stream_add(byte[] msg, int len){
        if(finish)
            return false;

        if(len == SHA256_BLOCK_SIZE){
            _sha256_calculate_8(msg,0, result_32);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean stream_last_block(byte[] msg, int len, long stream_len){
        if(finish)
            return false;

        if(len == SHA256_BLOCK_SIZE){
            _sha256_calculate_8(msg,0,result_32);

            int[] w = new int[64];
            w[0] = 0x80000000;

            //set original message length
            stream_len *= 8;
            w[15] = (int) (stream_len);
            w[14] = (int) (stream_len >>> 32);

            _sha256_calculate(w,result_32);

            for (int i = 0; i < 8; ++i){
                result[i*4 + 3] = (byte) (result_32[i] &0xFF);
                result[i*4 + 2] = (byte) ((result_32[i] >>> 8) & 0xFF);
                result[i*4 + 1] = (byte) ((result_32[i] >>> 16) & 0xFF);
                result[i*4] = (byte) ((result_32[i] >>> 24) & 0xFF);
            }
            finish = true;
            return true;
        }

        if(len < SHA256_BLOCK_SIZE){
            int block_n = ((len < 56) ? 1 : 2);

            byte[] _t = new byte[SHA256_BLOCK_SIZE];
            System.arraycopy(msg,0,_t,0,len);

            int[] w = new int[64];
            int j = 0;

            //put byte into int
            for (int i = 0; i < 16; ++i, j+=4){
                w[i] |= (_t[j] << 24);
                w[i] |= (_t[j+1] << 16) & 0x00FF0000;
                w[i] |= (_t[j+2] << 8) & 0x0000FF00;
                w[i] |= (_t[j+3]) & 0xFF;
            }

            //set 0x80
            w[len / 4] |= (0x80000000 >>> (8 * (len % 4)));


            if(len >= 56) {
                _sha256_calculate(w,result_32);
                Arrays.fill(w,0);
            }

            //set original message length
            stream_len *= 8;
            w[15] = (int) (stream_len);
            w[14] = (int) (stream_len >>> 32);

            _sha256_calculate(w,result_32);

            for (int i = 0; i < 8; ++i){
                result[i*4 + 3] = (byte) (result_32[i] &0xFF);
                result[i*4 + 2] = (byte) ((result_32[i] >>> 8) & 0xFF);
                result[i*4 + 1] = (byte) ((result_32[i] >>> 16) & 0xFF);
                result[i*4] = (byte) ((result_32[i] >>> 24) & 0xFF);
            }
            finish = true;
            return true;

        }
        else{
            return false;
        }
    }

    public boolean stream_all(byte[] msg, int len){
        if(finish)
            return false;

        if(len < SHA256_BLOCK_SIZE){
            return stream_last_block(msg,len,len);
        }
        else{
            int i;
            int _l = len;

            for(i = 0; _l > SHA256_BLOCK_SIZE; _l-=SHA256_BLOCK_SIZE, ++i){
                _sha256_calculate_8(msg, i * SHA256_BLOCK_SIZE, result_32);
            }

            byte[] _lb = new byte[_l];
            System.arraycopy(msg,i * SHA256_BLOCK_SIZE,_lb,0,_l);
            return stream_last_block(_lb,_l,len);
        }
    }

    public boolean stream_all(String msg){
        byte[] _m = msg.getBytes();
        return stream_all(_m,_m.length);
    }
}
