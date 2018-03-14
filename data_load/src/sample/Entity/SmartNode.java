package sample.Entity;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import sample.Main;


public class SmartNode extends Circle{
    public Tooltip tip;
    public FadeTransition ft;
    public ScaleTransition st;
    public RotateTransition rt1;
    public RotateTransition rt2;

    public SmartNode(Branch parentBranch, Tooltip tooltip) {
        super(0,parentBranch.length/2, parentBranch.length/2);
        this.setFill(new ImagePattern(new Image(Main.class.getResourceAsStream("./resources/" + tooltip.getText()+".jpg"))));

        tip = tooltip;
        setScaleX(0); //trick to hide leaves
        setScaleY(0);

        Tooltip.install(this,tooltip);

        rt1 = new RotateTransition(Duration.millis(500), this);
        rt1.setByAngle(360f);
        rt1.setCycleCount(1);
        rt1.setAutoReverse(true);

        rt2 = new RotateTransition(Duration.millis(500), this);
        rt2.setToAngle(0f);
        rt2.setCycleCount(1);
        rt2.setAutoReverse(true);

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rt2.stop();
                rt1.play();
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rt1.stop();
                rt2.play();
            }
        });

        ft = new FadeTransition(Duration.millis(1000), this);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
//        ft.play();

        st = new ScaleTransition(Duration.millis(1000), this);
        st.setToX(5f);
        st.setToY(5f);
        st.setCycleCount(-1);
        st.setAutoReverse(true);
//        st.play();
//        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                ft.play();
//                st.play();
//            }
//        });
    }

}
