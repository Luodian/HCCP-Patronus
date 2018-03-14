package sample.Entity;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class Animator implements Runnable {

    public static final Duration BRANCH_GROWING_DURATION = Duration.seconds(0.5);
    public static final Duration LEAF_APPEARING_DURATION = Duration.seconds(2);
    private TreeGenerator treeGenerator;
    private JFXButton startBtn;

    public Animator(TreeGenerator treeGenerator, JFXButton startBtn ) {
        this.treeGenerator = treeGenerator;
        this.startBtn = startBtn;
    }

    @Override
    public void run() {

        List<Tree> treeList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Tree tree = treeGenerator.generateTree(90 * i);
            treeList.add(tree);
        }
        // branch growing animation
        List<SequentialTransition> branchGrowingAnimationList = new ArrayList<>();
        for (Tree tree:treeList) {
            SequentialTransition branchGrowingAnimation = new SequentialTransition();
            for (int i = 0; i < tree.generations.size(); i++) {
                List<Branch> branchGeneration = tree.generations.get(i);
                branchGrowingAnimation.getChildren().add(animateBranchGrowing(branchGeneration, i, BRANCH_GROWING_DURATION)); //create animation for current crown
            }
            branchGrowingAnimationList.add(branchGrowingAnimation);
        }
        // Main animation: grass bending, tree bending, tree growing, seasons changing
        final Transition all = new ParallelTransition(new SequentialTransition(branchGrowingAnimationList.get(0), animateLeaf(treeList.get(0).leafage)),
                new SequentialTransition(branchGrowingAnimationList.get(1), animateLeaf(treeList.get(1).leafage)),
                new SequentialTransition(branchGrowingAnimationList.get(2), animateLeaf(treeList.get(2).leafage)),
                new SequentialTransition(branchGrowingAnimationList.get(3), animateLeaf(treeList.get(3).leafage)));
        SequentialTransition allWithPlus = new SequentialTransition();
        ParallelTransition btnPT = new ParallelTransition();
        btnPT.getChildren().add(ScaleTransitionBuilder.create().toX(5).toY(5).node(startBtn).duration(LEAF_APPEARING_DURATION).build());
        allWithPlus.getChildren().addAll(all,btnPT);
        allWithPlus.play();

//        all.play();

    }

    //Animatation for  growing branches
    private Animation animateBranchGrowing(List<Branch> branchGeneration, int depth, Duration duration) {

        ParallelTransition sameDepthBranchAnimation = new ParallelTransition();
        for (final Branch branch : branchGeneration) {
            Timeline branchGrowingAnimation = new Timeline(new KeyFrame(duration, new KeyValue(branch.base.endYProperty(), branch.length)));//line is growing by changinh endY from 0 to brunch.length
//            branchGrowingAnimation.getKeyFrames().add(new KeyFrame(duration, new KeyValue(branch.smartNode.radiusProperty(), branch.length / 2)));
            sameDepthBranchAnimation.getChildren().add(
                    new SequentialTransition(
                    //To set width from 0 to some value we use pause transition with duration.one millisecond 
                    //trick to show lines
                    PauseTransitionBuilder.create().duration(Duration.ONE).onFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    branch.base.setStrokeWidth(branch.length / 50);
                }
            }).build(),
                    branchGrowingAnimation));

        }
        return sameDepthBranchAnimation;

    }

    private Transition animateLeaf(List<SmartNode> leafage){
        ParallelTransition parallelTransition = new ParallelTransition();
        for (SmartNode leaf : leafage) {
            //leafage appear
            parallelTransition.getChildren().add(ScaleTransitionBuilder.create().toX(1).toY(1).node(leaf).duration(LEAF_APPEARING_DURATION).build());
        }
        return parallelTransition;
    }
}
