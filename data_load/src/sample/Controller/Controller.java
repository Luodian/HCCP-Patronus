package sample.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import sample.Entity.Animator;
import sample.Main;
import sample.Entity.TreeGenerator;
import sample.Main1;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    public JFXTabPane total_center_pane;
    @FXML
    public Tab gailan_tab;
    @FXML
    public Tab func_tab;
    @FXML
    public Tab manage_data_tab;
    @FXML
    public Tab personal_info_tab;

    private static double TAB_SIZE = 80;
    public static boolean isOnline = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        total_center_pane.setPadding(new Insets(30,50,30,50));
        total_center_pane.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null,new BorderWidths(3))));
        
        gailan_tab.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("./resources/1.png"), TAB_SIZE, TAB_SIZE, false, true)));
        func_tab.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("./resources/2.png"), TAB_SIZE, TAB_SIZE, false, true)));
        manage_data_tab.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("./resources/3.png"), TAB_SIZE, TAB_SIZE, false, true)));
        personal_info_tab.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("./resources/4.png"), TAB_SIZE, TAB_SIZE, false, true)));

        if(!isOnline) {
            gailan_tab.setContent(loginAni());
            isOnline = true;
        }

        Pane func_pane = new Pane();
        try {
            Parent main_page = FXMLLoader.load(getClass().getResource("../FXML/gongneng.fxml"));
            func_pane.getChildren().addAll(main_page);
            Parent data_load = FXMLLoader.load(getClass().getResource("../FXML/data_load.fxml"));
            Parent edit_code = FXMLLoader.load (getClass ().getResource ("../FXML/EditCodePage.fxml"));
            func_tab.setContent(edit_code);
            manage_data_tab.setContent(data_load);
        } catch (IOException e) {
            e.printStackTrace();
        }

//
//        double bianchang = 100;
//        TopBtn gailan = new TopBtn(bianchang, "1.png");
//        TopBtn selectFunc = new TopBtn(bianchang, "2.png");
//        TopBtn manageData = new TopBtn(bianchang, "3.png");
//        TopBtn personalInfo = new TopBtn(bianchang, "4.png");
//
//        top_hbox.getChildren().addAll(gailan, selectFunc, manageData, personalInfo);
//        top_hbox.setStyle("-fx-background-color: aquamarine");
//
//        try {
//            left_pane.getChildren().clear();
//            Parent root = FXMLLoader.load(getClass().getResource("gailan.fxml"));
//            left_pane.getChildren().addAll(root);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        gailan.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                try {
//                    left_pane.getChildren().clear();
//                    Parent root = FXMLLoader.load(getClass().getResource("gailan.fxml"));
//                    left_pane.getChildren().addAll(root);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        selectFunc.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                try {
//                    left_pane.getChildren().clear();
//                    Parent root = FXMLLoader.load(getClass().getResource("gongneng.fxml"));
//                    left_pane.getChildren().addAll(root);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    private Pane sideList() {
        Pane gailan_pane = new Pane();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../FXML/gailan.fxml"));
            gailan_pane.getChildren().addAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return gailan_pane;
//        gailan_tab.setContent(gailan_pane);
    }

    private Pane loginAni() {
        Group rootContent = new Group();
        Group treeContent = new Group();
        rootContent.getChildren().add(treeContent = new Group()); // tree layout
        rootContent.getTransforms().addAll(new Translate(Main1.SCENE_WIDTH / 2, Main1.SCENE_HEIGHT *2/5),new Rotate(225));
        JFXButton startBtn = new JFXButton("start");
        startBtn.setLayoutX(Main1.SCENE_WIDTH *5/6 );
        startBtn.setLayoutY(Main1.SCENE_HEIGHT *5/6);
        startBtn.setAlignment(Pos.BASELINE_LEFT);
        startBtn.setGraphicTextGap(16.0);

        Animator animator = new Animator(new TreeGenerator(treeContent, Main1.NUMBER_OF_BRANCH_GENERATIONS), startBtn);
        animator.run();

        Pane sidePane = sideList();
        sidePane.setVisible(false);
        startBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000),sidePane);
                translateTransition.setFromX(-1*sidePane.getWidth());
                translateTransition.setToX(0);
                translateTransition.play();
                sidePane.setVisible(true);
                startBtn.setVisible(false);
            }
        });

        Pane pane = new Pane();
        pane.getChildren().add(rootContent);
        pane.getChildren().add(startBtn);
        pane.getChildren().add(sidePane);
        return  pane;
    }

}


