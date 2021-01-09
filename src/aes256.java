public class aes256 {

    //sbox
    private static final int[] sbox = {
            // 0     1     2     3     4     5     6     7     8     9     a     b     c     d     e     f
            0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76, // 0
            0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0, // 1
            0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15, // 2
            0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75, // 3
            0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84, // 4
            0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf, // 5
            0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8, // 6
            0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2, // 7
            0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73, // 8
            0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb, // 9
            0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79, // a
            0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08, // b
            0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a, // c
            0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e, // d
            0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf, // e
            0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16  // f
    };

    private static final int[] inv_sbox = {
            // 0     1     2     3     4     5     6     7     8     9     a     b     c     d     e     f
            0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb, // 0
            0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb, // 1
            0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e, // 2
            0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25, // 3
            0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92, // 4
            0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84, // 5
            0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06, // 6
            0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b, // 7
            0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73, // 8
            0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e, // 9
            0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b, // a
            0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4, // b
            0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f, // c
            0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef, // d
            0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61, // e
            0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d  // f
    };

    private static final int[] rcon = {0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36};

    private static final int KEY_LENGTH = 32; //32 Byte
    private static final int E_KEY_LENGTH = 240;
    private static final int ROUND_TIMES = 14;

    private byte[] key;
    private byte[] e_key;

    private void key_expansion(){

        //first 32 char of expanded key is the key itself
        System.arraycopy(key,0,e_key,0,KEY_LENGTH);

        /// \note W(8i)   = g(W(8i-1)) xor W(8i-8)
        ///       W(8i+1) = W(8i)      xor W(8i-7)
        ///       W(8i+2) = W(8i+1)    xor W(8i-6)
        ///       W(8i+3) = W(8i+2)    xor W(8i-5)
        ///       W(8i+4) = h(W(8i-3)) xor W(8i-4)
        ///       W(8i+5) = W(8i+4)    xor W(8i-3)
        ///       W(8i+6) = W(8i+5)    xor W(8i-2)
        ///       W(8i+7) = W(8i+6)    xor W(8i-1)
        ///       the length of one W is 32bits
        ///       a round of aes256 need 4 W
        ///       total 60 W
        for(int i = KEY_LENGTH / 4; i < 4 * (ROUND_TIMES + 1); ++i){
            //temp array
            byte[] _t = new byte[4];
            // copy Wi-1 to temp array
            System.arraycopy(e_key,(i - 1) * 4, _t,0,4);

            //g func
            if (i % 8 == 0){
                //rot word
                byte __t = _t[0];
                _t[0] = _t[1];
                _t[1] = _t[2];
                _t[2] = _t[3];
                _t[3] = __t;

                //sbox replace
                _t[0] = (byte) sbox[Byte.toUnsignedInt(_t[0])];
                _t[1] = (byte) sbox[Byte.toUnsignedInt(_t[1])];
                _t[2] = (byte) sbox[Byte.toUnsignedInt(_t[2])];
                _t[3] = (byte) sbox[Byte.toUnsignedInt(_t[3])];

                // xor
                _t[0] = (byte) (_t[0] ^ rcon[i/8]);
            }

            //h func
            if (i % 8 == 4){
                //sbox replace
                _t[0] = (byte) sbox[Byte.toUnsignedInt(_t[0])];
                _t[1] = (byte) sbox[Byte.toUnsignedInt(_t[1])];
                _t[2] = (byte) sbox[Byte.toUnsignedInt(_t[2])];
                _t[3] = (byte) sbox[Byte.toUnsignedInt(_t[3])];
            }

            e_key[i * 4 + 0] = (byte) (e_key[(i - 8) * 4 + 0] ^ _t[0]);
            e_key[i * 4 + 1] = (byte) (e_key[(i - 8) * 4 + 1] ^ _t[1]);
            e_key[i * 4 + 2] = (byte) (e_key[(i - 8) * 4 + 2] ^ _t[2]);
            e_key[i * 4 + 3] = (byte) (e_key[(i - 8) * 4 + 3] ^ _t[3]);
        }

    }

    public aes256(String pwd){
        //use pwd hash value as key
        key = new byte[KEY_LENGTH];

        sha256 s = new sha256();
        s.stream_all(pwd);
        byte[] r = s.getResult();

        System.arraycopy(r,0,key,0,KEY_LENGTH);

        e_key = new byte[E_KEY_LENGTH];
        key_expansion();
    }

    private static void SubBytes(byte[] msg){
        for(int i = 0; i < 16; ++i){
            msg[i] = (byte) sbox[Byte.toUnsignedInt(msg[i])];
        }
    }

    private static void InvSubBytes(byte[] msg){
        for(int i = 0; i < 16; ++i){
            msg[i] = (byte) inv_sbox[Byte.toUnsignedInt(msg[i])];
        }
    }

    private static void ShiftRows(byte[] msg){
        byte _t;

        // Rotate first row 1 columns to left
        _t = msg[(0 * 4) + (1)];
        msg[(0 * 4) + (1)] = msg[(1 * 4) + (1)];
        msg[(1 * 4) + (1)] = msg[(2 * 4) + (1)];
        msg[(2 * 4) + (1)] = msg[(3 * 4) + (1)];
        msg[(3 * 4) + (1)] = _t;

        // Rotate second row 2 columns to left
        _t = msg[(0 * 4) + (2)];
        msg[(0 * 4) + (2)] = msg[(2 * 4) + (2)];
        msg[(2 * 4) + (2)] = _t;

        _t = msg[(1 * 4) + (2)];
        msg[(1 * 4) + (2)] = msg[(3 * 4) + (2)];
        msg[(3 * 4) + (2)] = _t;

        // Rotate third row 3 columns to left
        _t = msg[(0 * 4) + (3)];
        msg[(0 * 4) + (3)] = msg[(3 * 4) + (3)];
        msg[(3 * 4) + (3)] = msg[(2 * 4) + (3)];
        msg[(2 * 4) + (3)] = msg[(1 * 4) + (3)];
        msg[(1 * 4) + (3)] = _t;
    }

    private static void InvShiftRows(byte[] msg){
        byte _t;

        // Rotate second row 1 columns to right
        _t = msg[(3 * 4) + (1)];
        msg[(3 * 4) + (1)] = msg[(2 * 4) + (1)];
        msg[(2 * 4) + (1)] = msg[(1 * 4) + (1)];
        msg[(1 * 4) + (1)] = msg[(0 * 4) + (1)];
        msg[(0 * 4) + (1)] = _t;

        // Rotate second row 2 columns to right
        _t = msg[(0 * 4) + (2)];
        msg[(0 * 4) + (2)] = msg[(2 * 4) + (2)];
        msg[(2 * 4) + (2)] = _t;

        _t = msg[(1 * 4) + (2)];
        msg[(1 * 4) + (2)] = msg[(3 * 4) + (2)];
        msg[(3 * 4) + (2)] = _t;

        // Rotate second row 3 columns to right
        _t = msg[(0 * 4) + (3)];
        msg[(0 * 4) + (3)] = msg[(1 * 4) + (3)];
        msg[(1 * 4) + (3)] = msg[(2 * 4) + (3)];
        msg[(2 * 4) + (3)] = msg[(3 * 4) + (3)];
        msg[(3 * 4) + (3)] = _t;
    }

    private static byte xtime(byte x){
        return (byte) ((x<<1) ^ (((x>>>7) & 1) * 0x1b));
    }

    private static void MixColumns(byte[] msg){
        byte Tmp, Tm, t;

        for (int i = 0; i < 4; ++i){
            t = msg[(i * 4) + 0];
            Tmp = (byte) (msg[(i * 4) + 0] ^ msg[(i * 4) + 1] ^ msg[(i * 4) + 2] ^ msg[(i * 4) + 3]);

            Tm = (byte) (msg[(i * 4) + 0] ^ msg[(i * 4) + 1]);
            Tm = xtime(Tm);
            msg[(i * 4) + 0] ^= Tm ^ Tmp ;

            Tm = (byte) (msg[(i * 4) + 1] ^ msg[(i * 4) + 2]);
            Tm = xtime(Tm);
            msg[(i * 4) + 1] ^= Tm ^ Tmp ;

            Tm = (byte) (msg[(i * 4) + 2] ^ msg[(i * 4) + 3]);
            Tm = xtime(Tm);
            msg[(i * 4) + 2] ^= Tm ^ Tmp ;

            Tm = (byte) (msg[(i * 4) + 3] ^ t);
            Tm = xtime(Tm);
            msg[(i * 4) + 3] ^= Tm ^ Tmp ;
        }
    }

    private static byte Multiply(byte x, int y){
        return (byte) (((y & 1) * x) ^ ((y>>>1 & 1) * xtime(x)) ^ ((y>>>2 & 1) * xtime(xtime(x))) ^ ((y>>>3 & 1) * xtime(xtime(xtime(x)))) ^ ((y>>>4 & 1) * xtime(xtime(xtime(xtime(x))))));
    }

    private static void InvMixColumns(byte[] ctxt){
        byte a, b, c, d;

        for (int i = 0; i < 4; ++i){
            a = ctxt[(i * 4) + 0];
            b = ctxt[(i * 4) + 1];
            c = ctxt[(i * 4) + 2];
            d = ctxt[(i * 4) + 3];

            ctxt[(i * 4) + 0] = (byte) (Multiply(a, 0x0e) ^ Multiply(b, 0x0b) ^ Multiply(c, 0x0d) ^ Multiply(d, 0x09));
            ctxt[(i * 4) + 1] = (byte) (Multiply(a, 0x09) ^ Multiply(b, 0x0e) ^ Multiply(c, 0x0b) ^ Multiply(d, 0x0d));
            ctxt[(i * 4) + 2] = (byte) (Multiply(a, 0x0d) ^ Multiply(b, 0x09) ^ Multiply(c, 0x0e) ^ Multiply(d, 0x0b));
            ctxt[(i * 4) + 3] = (byte) (Multiply(a, 0x0b) ^ Multiply(b, 0x0d) ^ Multiply(c, 0x09) ^ Multiply(d, 0x0e));
        }
    }

    private void RoundKeyAdd(byte[] msg, int round){
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                msg[(i * 4) + (j)] ^= e_key[(round * 16) + (i * 4) + j];
            }
        }
    }

    public void encrypt(byte[] msg){

        //0 round
        RoundKeyAdd(msg, 0);

        // 1 - 13 round
        for(int i = 1; i < 14 ; ++i){
            SubBytes(msg);

            ShiftRows(msg);

            MixColumns(msg);

            RoundKeyAdd(msg, i);
        }

        //last round
        SubBytes(msg);

        ShiftRows(msg);

        RoundKeyAdd(msg, 14);
    }

    public void decrypt(byte[] ctxt){

        RoundKeyAdd(ctxt, 14);

        for(int i = 13; i > 0; --i){
            InvShiftRows(ctxt);

            InvSubBytes(ctxt);

            RoundKeyAdd(ctxt, i);

            InvMixColumns(ctxt);
        }

        InvShiftRows(ctxt);

        InvSubBytes(ctxt);

        RoundKeyAdd(ctxt, 0);
    }
}