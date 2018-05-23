package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.SocketConnect.SocketHandler;
import sample.Utils.RSA;

import java.net.InetAddress;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
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
//        launch(args);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String tmp = RSA.RSAPublicCrypto("wong", rsaPublicKey.getEncoded());
        System.out.println(RSA.RSAPrivateDecrypt(tmp, rsaPrivateKey.getEncoded()));
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
	    FXMLLoader myEditPageLoader = new FXMLLoader (getClass ().getResource ("./FXML/login.fxml"));
	    Parent root = myEditPageLoader.load();


	    /**连接socket**/
        SocketHandler.initSocket(InetAddress.getLocalHost().getHostAddress(), 8888);
//        SocketHandler.initSocket("172.20.11.219", 8888);

//        /**加载数据库**/
//        try {
//            //加载MySql的驱动类
//            Class.forName("com.mysql.jdbc.Driver");
//            SQLHandler.con = DriverManager.getConnection(url, username, password);
//            SQLHandler.query = SQLHandler.con.createStatement();
//
//        } catch (ClassNotFoundException e) {
//            System.out.println("can't find driver，load driver failed!");
//            e.printStackTrace();
//        } catch (SQLException e) {
//            System.out.println("can't connect to local database!");
//            e.printStackTrace();
//        }

        /**启动登录页面**/
	    primaryStage.setTitle ("Patronus");
	    Scene scene = new Scene(root);
	    primaryStage.initStyle(StageStyle.UNDECORATED);
	    //scene.getStylesheets().add(JavaKeywordsAsync.class.getResource("java-keywords.css").toExternalForm());
	    primaryStage.setScene (scene);
        hashMap.put("login", primaryStage);
	    primaryStage.show();
    }
}
