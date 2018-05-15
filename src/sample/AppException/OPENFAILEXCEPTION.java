package sample.AppException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class OPENFAILEXCEPTION extends Throwable{
    public OPENFAILEXCEPTION() {
        super("OPEN FAILED");
    }
}
