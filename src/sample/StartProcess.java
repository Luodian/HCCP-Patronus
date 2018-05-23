package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.SocketConnect.SocketHandler;
import sample.Utils.JSONCryptoUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class StartProcess extends Application {
    public static String url = "jdbc:mysql://localhost:3306/Patronus" ;
    public static String username = "root" ;
    public static String password = "wyz123348377" ;
    /**目前已有的页面的key：
     * main_page
     * login
     * data_load
     * data_setting
     * groups
     * group_setting
     * group_information
     * coding
     * tasks
     * confirm
     * data_choose_board
     * group_choose_board
     * nn_choose_board
     * **/

    public static HashMap<String, Stage> hashMap = new HashMap<String, Stage>();

    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
        launch(args);
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        keyPairGenerator.initialize(512);
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
//
//        String tmp = RSA.RSAPublicCrypto("wong", Base64.encodeBase64String(rsaPublicKey.getEncoded()));
//        System.out.println(RSA.RSAPrivateDecrypt(tmp, Base64.encodeBase64String(rsaPrivateKey.getEncoded())));
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
	    FXMLLoader myEditPageLoader = new FXMLLoader (getClass ().getResource ("./FXML/login.fxml"));
	    Parent root = myEditPageLoader.load();


	    /**连接socket**/
        SocketHandler.initSocket(InetAddress.getLocalHost().getHostAddress(), 8888);

        /**获取本地私钥**/
        File file = new File(getClass().getResource("./Crypto/rsa_key.txt").getPath());
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            JSONCryptoUtils.LOCAL_PRIVATE_KEY = bufferedReader.readLine();
            if (JSONCryptoUtils.LOCAL_PRIVATE_KEY == null) {
                try {
                    throw new Throwable("Can't find private_key");
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            } else {
                JSONCryptoUtils.LOCAL_PUBLIC_KEY = bufferedReader.readLine();
                if (JSONCryptoUtils.LOCAL_PUBLIC_KEY == null) {
                    try {
                        throw new Throwable("Can't find public_key");
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        } else {
            try {
                throw new Throwable("Can't find private_key");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return;
            }
        }



        /**启动登录页面**/
	    primaryStage.setTitle ("Patronus");
	    Scene scene = new Scene(root);
	    primaryStage.initStyle(StageStyle.UNDECORATED);
	    primaryStage.setScene (scene);
        hashMap.put("login", primaryStage);
	    primaryStage.show();
    }
}
