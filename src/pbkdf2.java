public class pbkdf2 {
    //salt
    private static final byte[] salt = {
            0x59, 0x55, 0x4b, 0x49, //YUKI
            0x79, 0x75, 0x6b, 0x69, //yuki
            0x4c, 0x44, 0x4c, 0x44, 0x4c, //LDLDL
            0x6c, 0x64, 0x6c, 0x64, 0x6c, //ldldl
            0x4d, 0x55, 0x47, 0x49, 0x59, 0x55, //MUGIYU
            0x6d, 0x75, 0x67, 0x69, 0x79, 0x75, //mugiyu
            0x00, 0x00
    };

    public static byte[] sha256_8 (byte[] pwd, int len, int iter){
        //create result array
        byte[] result = new byte[32];
        //create sha256 object
        sha256 __sha = new sha256();
        __sha.clear();

        int _p_len = len + 32;
        //temporary array which contains password and salt
        //salt length is 32 * 8 bit
        byte[] _p = new byte[_p_len];

        //copy password and initial salt to temporary array
        System.arraycopy(pwd,0,_p,0,len);
        System.arraycopy(salt,0,_p,len,32);

        //calculate U1. U1 = sha256(password + initial salt)
        __sha.stream_all(_p,_p_len);
        byte[] sr = __sha.getResult();
        System.arraycopy(sr,0,result,0,32);
        __sha.clear();

        //use U1 as salt then calculate U2. Ui+1 = (password + Ui)
        System.arraycopy(result, 0, _p, len,32);

        //calculate n times
        for (int i = 1; i < iter; ++i){
            //calculate Ui+1
            __sha.stream_all(_p,_p_len);
            byte[] _Ui = __sha.getResult();

            //result = U1 xor U2 ... xor Ui
            for (int j = 0; j < 32; ++j){
                result[j] ^= _Ui[j];
            }

            //copy Ui+1 to temporary array as salt then calculate Ui+2
            System.arraycopy(_Ui, 0, _p, len,32);

            __sha.clear();
        }
        return result;
    }

    public static byte[] sha256_8 (String pwd, int iter){
        byte[] _m = pwd.getBytes();
        return sha256_8(_m, _m.length, iter);
    }

}
