package sample.Utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
    //私钥加密，公钥解密——加密
    public static String RSAPrivateCrypto(String source, byte[] private_key) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(private_key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] result = null;
            for (int i = 0; i < source.getBytes().length; i += 32) {
                byte[] tmp = cipher.doFinal(ArrayUtils.subarray(source.getBytes(), i, i + 32));
                result = ArrayUtils.addAll(result, tmp);
            }
            return Base64.encodeBase64String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }


    //私钥加密，公钥解密--解密
    public static String RSAPublicDecrypt(String source, byte[] public_key) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(public_key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            StringBuilder stringBuilder = new StringBuilder();
            byte[] bytes = Base64.decodeBase64(source);
            for (int i = 0; i < bytes.length; i += 64) {
                byte[] tmp = cipher.doFinal(ArrayUtils.subarray(bytes, i, i + 64));
                stringBuilder.append(new String(tmp));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 公钥加密，私钥解密--加密
    public static String RSAPublicCrypto(String source, byte[] public_key) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(public_key);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] result = null;
            for (int i = 0; i < source.getBytes().length; i += 32) {
                byte[] tmp = cipher.doFinal(ArrayUtils.subarray(source.getBytes(), i, i + 32));
                result = ArrayUtils.addAll(result, tmp);
            }
            return Base64.encodeBase64String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    //公钥加密，私钥解密--解密
    public static String RSAPrivateDecrypt(String source, byte[] private_key) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(private_key);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            StringBuilder stringBuilder = new StringBuilder();
            byte[] bytes = Base64.decodeBase64(source);
            for (int i = 0; i < bytes.length; i += 64) {
                byte[] tmp = cipher.doFinal(ArrayUtils.subarray(bytes, i, i + 64));
                stringBuilder.append(new String(tmp));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void jdkRSA() {
        try {
            String src = "+b0O7KOU9R+EzdPbxjpQIhAKitCDL+gydfxF5JTuC2TmfPB1hRm7";
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            System.out.println("公钥为");
            System.out.println(Base64.encodeBase64String(rsaPublicKey.getEncoded()));
            System.out.println("私钥为");
            System.out.println(Base64.encodeBase64String(rsaPrivateKey.getEncoded()));

            //私钥加密，公钥解密——加密
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");

            System.out.println(src.getBytes().length);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(src.getBytes());

            System.out.println(result.length);
            System.out.println("RSA私钥加密，公钥解密--加密:" + Base64.encodeBase64String(result));

            //私钥加密，公钥解密--解密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            result = cipher.doFinal(result);
            System.out.println("RSA私钥加密，公钥解密--解密:" + new String(result));


            //公钥加密，私钥解密--加密
            x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            result = cipher.doFinal(src.getBytes());
            System.out.println("RSA公钥加密，私钥解密--加密:" + Base64.encodeBase64String(result));

            //公钥加密，私钥解密--解密
            pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            result = cipher.doFinal(result);
            System.out.println("RSA公钥加密，私钥解密--解密:" + new String(result));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

    }
}


