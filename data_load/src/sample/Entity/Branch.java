package sample.Entity;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.transform.Rotate;

import static sample.Utils.RandomUtil.getGaussianRandom;
import static sample.Utils.RandomUtil.getRandom;
import static sample.Utils.Util.addChildToParent;

public class Branch extends Group {

    public Line base;
    public double length;
//    public SmartNode smartNode;

    public SimpleDoubleProperty globalAngle =  new SimpleDoubleProperty(90);

    public enum Type {
        TOP, LEFT, RIGHT;
    }

    public Branch(int rootAngle) {
        base = new Line();
        getChildren().add(base);
        setTranslateY(30);
        getTransforms().add(new Rotate(getGaussianRandom(rootAngle, 1), 0, 0));
        length = 100;

//        smartNode = new SmartNode(10);
//        //这里的 20 并没有任何用
//        getChildren().add(smartNode);
////        smartNode.setTranslateY(30);

        setBranchStyle(0);

    }

    public Branch(int rootAngle, Branch parentBranch, Type type, int depth) {
        this(rootAngle);
        SimpleDoubleProperty locAngle = new SimpleDoubleProperty(0);
        globalAngle.bind(locAngle.add(parentBranch.globalAngle.get()));
        double transY = 0; //place of beggining child branch
        switch (type) {
            case TOP: //creates top branch
                transY = parentBranch.length;
                length = parentBranch.length * 0.8;
                locAngle.set(getRandom(10));
                break;
            case LEFT: //create left branch
            case RIGHT: //create right branch
                transY = parentBranch.length - getGaussianRandom(0, parentBranch.length, parentBranch.length / 10, parentBranch.length / 10);
                locAngle.set(getGaussianRandom(45, 10) * (Type.LEFT == type ? 1 : -1));
                if ((0 > globalAngle.get() || globalAngle.get() > 180) && depth < 4) {
                    length = parentBranch.length * getGaussianRandom(0.3, 0.1); //branches pointed down are shorter
                } else {
                    length = parentBranch.length * 0.75;
                }
                break;
        }
        setTranslateY(transY);
        int baseAngle = rootAngle;
        if (rootAngle == 90 ||rootAngle == 270) {
            baseAngle = rootAngle + 180;
        }
        getTransforms().add(new Rotate(baseAngle + locAngle.get(), 0, 0)); // rotate branch to local angle relative to parent branch

        setBranchStyle(depth);
        addChildToParent(parentBranch, this);

    }


    private void setBranchStyle(int depth) {
        base.setStroke(Color.color(0.4, 0.1, 0.1, 1));

        if (depth < 5) { //line rendering optimization
            base.setStrokeLineJoin(StrokeLineJoin.ROUND);
            base.setStrokeLineCap(StrokeLineCap.ROUND);
        }
        base.setStrokeWidth(0); //trick to hide lines
    }

}
