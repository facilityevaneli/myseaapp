/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myseaapp;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author stvn
 */
public class MySeaApp extends Application
{

    private ImageView sea0;
    private Rectangle sea0Clip;
    private ImageView sea1;
    private Rectangle sea1Clip;
    private ImageView quit;
    private ImageView shark;
    private double sX = 0;
    private DoubleProperty coordXReal = new SimpleDoubleProperty(0);
    private FadeTransition fadeTransition;
    private DropShadow sharkShaddow;
    private SimpleDoubleProperty xOff = new SimpleDoubleProperty(0.0);
    private SimpleDoubleProperty yOff = new SimpleDoubleProperty(0.0);
    private SimpleDoubleProperty sOff = new SimpleDoubleProperty(5.0);
    private SimpleDoubleProperty scale = new SimpleDoubleProperty(0.5);
    private Timeline sharkSwimAway;
    private boolean exited = true;

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        sea0 = new ImageView(new Image(MySeaApp.class.getResourceAsStream(
                "images/sea0.jpg")));
        sea0Clip = new Rectangle(300, 220);
        sea0Clip.setArcHeight(20);
        sea0Clip.setArcWidth(20);

        sea1 = new ImageView(new Image(MySeaApp.class.getResourceAsStream(
                "images/sea1.jpg")));
        sea1Clip = new Rectangle(300, 220);
        sea1Clip.setArcHeight(20);
        sea1Clip.setArcWidth(20);
        sea1.setOpacity(0.0);
        setShark();

        fadeTransition = new FadeTransition(Duration.seconds(1), sea1);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        setDrag();
        setQuit();

        sea0.setClip(sea0Clip);
        sea1.setClip(sea1Clip);
        Pane root = new Pane();
        root.getChildren().addAll(sea0, sea1, shark, quit);

        Scene myScene = new Scene(root, 1300, 1000);
        myScene.setFill(null);
        primaryStage.setScene(myScene);
        primaryStage.show();
    }

    private void setQuit()
    {
        quit = new ImageView(new Image(MySeaApp.class.getResourceAsStream(
                "images/closeIcon.png")));
        quit.setFitHeight(25);
        quit.setFitWidth(25);
        quit.setX(270);
        quit.setY(10);

        quit.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t)
            {
                System.exit(0);
            }
        });
    }

    private void setDrag()
    {
        sea0.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t)
            {
                sX = t.getSceneX() - coordXReal.getValue();
            }
        });

        sea0.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t)
            {
                coordXReal.set(t.getSceneX() - sX);
            }
        });

        sea0.xProperty().bind(coordXReal);

        sea1.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t)
            {
                sX = t.getSceneX() - coordXReal.getValue();
            }
        });

        sea1.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t)
            {
                coordXReal.set(t.getSceneX() - sX);
            }
        });

        sea1.setOnMouseEntered(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t)
            {
                //sea1.setOpacity(1.0);
                if (exited)
                {
                    fadeTransition.setRate(1.0);
                    fadeTransition.play();
                    exited = false;
                }
            }
        });

        sea1.setOnMouseExited(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t)
            {
                //sea1.setOpacity(0.0);
                if (!sea1Clip.contains(new Point2D(t.getSceneX(), t.getSceneY())))
                {
                    fadeTransition.setRate(-1.0);
                    fadeTransition.play();
                    exited = true;
                }
            }
        });

        sea1.xProperty().bind(coordXReal);
    }

    private void setShark()
    {
        shark = new ImageView(new Image(MySeaApp.class.getResourceAsStream(
                "images/shark.png")));
        shark.setScaleX(0.5);
        shark.setScaleY(0.5);
        shark.opacityProperty().bind(sea1.opacityProperty());

        shark.scaleXProperty().bind(scale);
        shark.scaleYProperty().bind(scale);
        shark.xProperty().bind(xOff);
        shark.yProperty().bind(yOff);


        sharkShaddow = new DropShadow(10.0, 10.0, 0.0, Color.BLACK);
        sharkShaddow.offsetXProperty().bind(sOff);
        sharkShaddow.offsetYProperty().bind(sOff);
        shark.setEffect(sharkShaddow);

        sharkSwimAway = new Timeline(
                new KeyFrame(Duration.ZERO,
                new KeyValue(sOff, 0),
                new KeyValue(scale, 0.8),
                new KeyValue(xOff, 0),
                new KeyValue(yOff, 0)),
                new KeyFrame(new Duration(5000),
                new KeyValue(sOff, -100),
                new KeyValue(scale, 4.0),
                new KeyValue(xOff, 2500),
                new KeyValue(yOff, 800)));

        shark.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t)
            {
                sharkSwimAway.play();
            }
        });
    }
}
