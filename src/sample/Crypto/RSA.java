package sample.Crypto;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
    //私钥加密，公钥解密——加密
    public static String RSAPrivateCrypto(String source, String private_key) {
        try {
            byte[] private_key_bytes = Base64.decodeBase64(private_key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(private_key_bytes);
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
    public static String RSAPublicDecrypt(String source, String public_key) {
        try {
            byte[] public_key_bytes = Base64.decodeBase64(public_key);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(public_key_bytes);
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
    public static String RSAPublicCrypto(String source, String public_key) {
        byte[] public_key_bytes = Base64.decodeBase64(public_key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(public_key_bytes);
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
    public static String RSAPrivateDecrypt(String source, String private_key) {
        byte[] private_key_bytes = Base64.decodeBase64(private_key);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(private_key_bytes);
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
}


