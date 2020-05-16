package weather2;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDateTime;

public class Main extends Application {

    int resX = 1600;
    int resY = 500;

    private String APIKEY = "4bd868975ea32a8d2906308e5b3318ca";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();

        primaryStage.setTitle("weather 2"); //haha get it
        primaryStage.setScene(new Scene(root, resX, resY));
        primaryStage.setResizable(false);

        Weather w1 = new Weather(0,0);
        Weather w2 = new Weather(800,0);

        Rectangle timeBG = new Rectangle(0,460,resX,40);
        timeBG.setOpacity(0.4);

        Text timeText = new Text("");
        timeText.setLayoutX(736);
        timeText.setLayoutY(485);
        timeText.setFill(Color.WHITE);
        Timeline timeTimeLine = new Timeline(new KeyFrame(Duration.seconds(1), e->{
            String t1 = ""+LocalDateTime.now();
            timeText.setText(t1.substring(0,10)+"   "+t1.substring(11,19));
        }));

        timeTimeLine.setCycleCount(Animation.INDEFINITE);
        timeTimeLine.play();

        root.getChildren().addAll(w1,w2,timeBG,timeText);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
