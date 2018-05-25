package sample.Crypto;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import sample.Utils.JSONCryptoUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {
    public static String SHA256StringChange(String str) {
        MessageDigest messageDigest;
        String encdeStr = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

    public static JSONObject DigitalSign(JSONObject json) throws JSONException {
        String digest = SHA256StringChange(json.toString());
        String digital_sign = RSA.RSAPrivateCrypto(digest, JSONCryptoUtils.LOCAL_PRIVATE_KEY);
        json.put("signature", digital_sign);
        return json;
    }

    public static boolean MessageAuthentication(JSONObject json, String public_key) {
        if (json.has("signature")) {
            String signature = (String) json.remove("signature");
            String digest = SHA256StringChange(json.toString());
            String digital_sign = RSA.RSAPublicDecrypt(digest, public_key);
            if (digital_sign.equals(signature)) return true;
        }
        return false;
    }
}
