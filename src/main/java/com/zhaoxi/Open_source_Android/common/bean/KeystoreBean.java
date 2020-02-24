package com.zhaoxi.Open_source_Android.common.bean;

/**
 * des: Keystore文件的bean
 * Created by ztt on 2018/7/25.
 */

public class KeystoreBean {
    private int version;
    private String id;
    private cryptoInfo crypto;
    public static class cryptoInfo{
        private String ciphertext;
        private String kdf;
        private kdfparamsInfo kdfparams;
        private cipherparamsInfo cipherparams;
        private class cipherparamsInfo{
            private String iv;

            public String getIv() {
                return iv;
            }

            public void setIv(String iv) {
                this.iv = iv;
            }
        }
        public class kdfparamsInfo{
            private int r;
            private int p;
            private int n;
            private int dklen;
            private String salt;

            public int getR() {
                return r;
            }

            public void setR(int r) {
                this.r = r;
            }

            public int getP() {
                return p;
            }

            public void setP(int p) {
                this.p = p;
            }

            public int getN() {
                return n;
            }

            public void setN(int n) {
                this.n = n;
            }

            public int getDklen() {
                return dklen;
            }

            public void setDklen(int dklen) {
                this.dklen = dklen;
            }

            public String getSalt() {
                return salt;
            }

            public void setSalt(String salt) {
                this.salt = salt;
            }
        }
        private String mac;
        private String cipher;

        public String getCiphertext() {
            return ciphertext;
        }

        public void setCiphertext(String ciphertext) {
            this.ciphertext = ciphertext;
        }

        public String getKdf() {
            return kdf;
        }

        public void setKdf(String kdf) {
            this.kdf = kdf;
        }

        public kdfparamsInfo getKdfparams() {
            return kdfparams;
        }

        public void setKdfparams(kdfparamsInfo kdfparams) {
            this.kdfparams = kdfparams;
        }

        public cipherparamsInfo getCipherparams() {
            return cipherparams;
        }

        public void setCipherparams(cipherparamsInfo cipherparams) {
            this.cipherparams = cipherparams;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getCipher() {
            return cipher;
        }

        public void setCipher(String cipher) {
            this.cipher = cipher;
        }
    }
    private String address;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public cryptoInfo getCrypto() {
        return crypto;
    }

    public void setCrypto(cryptoInfo crypto) {
        this.crypto = crypto;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
