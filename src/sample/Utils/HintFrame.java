package sample.Utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class HintFrame {
    public static void showFailFrame(String content){
        Image image = new Image("/sample/resources/error.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        Notifications notificationBuilder = Notifications.create()
                .title("FAILED")
                .text(content)
                .darkStyle()
                .graphic(imageView)
                .hideAfter(Duration.seconds(2))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println(content);
                    }
                });
        notificationBuilder.show();
    }
    public static void showSuccessFrame(String content){
        Image image = new Image("/sample/resources/correct.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        Notifications notificationBuilder = Notifications.create()
                .title("SUCCESS")
                .text(content)
                .darkStyle()
                .graphic(imageView)
                .hideAfter(Duration.seconds(2))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println(content);
                    }
                });
        notificationBuilder.show();
    }
}
