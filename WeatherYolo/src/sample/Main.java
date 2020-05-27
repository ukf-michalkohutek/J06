package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class Main extends Application {

    Group root = new Group();
    VBox tempnow = new VBox();
    HBox windnow = new HBox();
    HBox flikenow = new HBox();
    VBox plus1 = new VBox(5);
    VBox plus2 = new VBox(5);
    VBox plus3 = new VBox(5);

    GetWeather getWeather;

    private ArrayList<ImageView> images = new ArrayList<>();
    private ArrayList<ImageView> icon = new ArrayList<>();
    private ArrayList<LocalDateTime> time = new ArrayList<>();
    private ArrayList<Integer> weather = new ArrayList<>();

    private TextField search = new TextField("search city");

    private Label d0 = new Label(), d1 = new Label(), d2 = new Label(), d3 = new Label();
    private Label w = new Label(), fl = new Label();
    private Label timenow = new Label(), time1 = new Label(), time2 = new Label(), time3 = new Label();


    @Override
    public void start(Stage primaryStage) throws Exception{

        root.getChildren().add(search);

        for (int i = 0; i < 6; i++){
            Image image = new Image("/Images/" + i + ".png", 50,50,false,true);
            Image image1 = new Image("/Backgrounds/" + i + ".png", 360,320,false,true);
            icon.add(new ImageView(image));
            icon.add(new ImageView(image));
            icon.add(new ImageView(image));
            images.add(new ImageView(image1));
        }

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime n1 = today.plusDays(1);
        LocalDateTime n2 = today.plusDays(2);
        LocalDateTime n3 = today.plusDays(3);
        time.add(today);
        time.add(n1);
        time.add(n2);
        time.add(n3);

        search.setLayoutX(100);
        search.setLayoutY(10);
        search.setPrefSize(160,25);
        search.setAlignment(Pos.CENTER);
        search.setFocusTraversable(false);

        tempnow.setAlignment(Pos.CENTER);
        tempnow.setLayoutY(40);
        tempnow.setPrefSize(360,100);

        windnow.setAlignment(Pos.CENTER);
        windnow.setLayoutY(160);
        windnow.setPrefSize(360,100);

        flikenow.setAlignment(Pos.CENTER);
        flikenow.setLayoutY(220);
        flikenow.setPrefSize(360,100);

        plus1.setAlignment(Pos.BASELINE_CENTER);
        plus1.setLayoutY(340);
        plus1.setPrefSize(360,180);

        plus2.setAlignment(Pos.BASELINE_CENTER);
        plus2.setLayoutY(470);
        plus2.setPrefSize(360,180);

        plus3.setAlignment(Pos.BASELINE_CENTER);
        plus3.setLayoutY(600);
        plus3.setPrefSize(360,180);

        root.getStylesheets().add("sample/Stylesheet.css");
        primaryStage.setTitle("Weather Yolo App");
        Scene scene = new Scene(root,360,750);
        scene.setFill(Color.rgb(64,64,64));
        primaryStage.setScene(scene);
        primaryStage.show();

        search.setOnKeyPressed(e -> {
            KeyCode k = e.getCode();
            switch (k){
                case ENTER:
                    try {
                        getWeather = new GetWeather(getCity());
                        changeBg();
                        showdata();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
            }

        });
    }

    public void showdata(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE dd.MM", new Locale("en"));
        root.getChildren().removeAll(tempnow,windnow,flikenow,plus1,plus2,plus3);
        tempnow.getChildren().removeAll(d0,timenow);
        windnow.getChildren().remove(w);
        flikenow.getChildren().remove(fl);
        plus1.getChildren().removeAll(time1,d1);
        plus2.getChildren().removeAll(time2,d2);
        plus3.getChildren().removeAll(time3,d3);

        d0 = new Label(String.valueOf((int)getWeather.getDay0()) + "°");
        d0.getStyleClass().add("d0");

        timenow = new Label(dateTimeFormatter.format(time.get(0)));
        timenow.getStyleClass().add("timenow");

        tempnow.getChildren().addAll(d0,timenow);

        w = new Label("Wind speed: " + String.valueOf(getWeather.getWind()) + " m/s");
        w.getStyleClass().add("f-w");

        windnow.getChildren().add(w);

        fl = new Label(String.valueOf("Feels like: " + getWeather.getFeelslike()) + "°");
        fl.getStyleClass().add("f-w");

        flikenow.getChildren().add(fl);

        d1 = new Label(String.valueOf((int)getWeather.getDay1()) + "°");
        d1.getStyleClass().add("d1-d2");

        time1 = new Label(dateTimeFormatter.format(time.get(1)));
        time1.getStyleClass().add("timenow");

        plus1.getChildren().addAll(time1,d1);

        d2 = new Label(String.valueOf((int)getWeather.getDay2()) + "°");
        d2.getStyleClass().add("d1-d2");

        time2 = new Label(dateTimeFormatter.format(time.get(2)));
        time2.getStyleClass().add("timenow");

        plus2.getChildren().addAll(time2,d2);

        d3 = new Label(String.valueOf((int)getWeather.getDay3()) + "°");
        d3.getStyleClass().add("d1-d2");

        time3 = new Label(dateTimeFormatter.format(time.get(3)));
        time3.getStyleClass().add("timenow");

        plus3.getChildren().addAll(time3,d3);

        changeIcon();

        root.getChildren().addAll(tempnow,windnow,flikenow,plus1,plus2,plus3);
    }

    public String getCity(){
        return search.getText();
    }

    public void changeIcon(){
        int weather1 = getWeather.getW1();
        int weather2 = getWeather.getW2();
        int weather3 = getWeather.getW3();
        weather.add(weather1);
        weather.add(weather2);
        weather.add(weather3);
        for (int i = 0; i < weather.size(); i++) {
            //CLOUDS
            if (weather.get(i) > 800 && weather.get(i) < 805) {
                if (i == 0) {
                    plus1.getChildren().removeAll(icon);
                    plus1.getChildren().addAll(icon.get(0));
                }
                else if (i == 1) {
                    plus2.getChildren().removeAll(icon);
                    plus2.getChildren().addAll(icon.get(1));
                }
                else if (i == 2) {
                    plus3.getChildren().removeAll(icon);
                    plus3.getChildren().addAll(icon.get(2));
                }
            }
            //CLEAR
            if (weather.get(i) == 800) {
                if (i == 0) {
                    plus1.getChildren().removeAll(icon);
                    plus1.getChildren().addAll(icon.get(3));
                }
                else if (i == 1) {
                    plus2.getChildren().removeAll(icon);
                    plus2.getChildren().addAll(icon.get(4));
                }
                else if (i == 2) {
                    plus3.getChildren().removeAll(icon);
                    plus3.getChildren().addAll(icon.get(5));
                }
            }
            //FOG
            if (weather.get(i) > 700 && weather.get(i) < 782) {
                if (i == 0) {
                    plus1.getChildren().removeAll(icon);
                    plus1.getChildren().addAll(icon.get(6));
                }
                else if (i == 1) {
                    plus2.getChildren().removeAll(icon);
                    plus2.getChildren().addAll(icon.get(7));
                }
                else if (i == 2) {
                    plus3.getChildren().removeAll(icon);
                    plus3.getChildren().addAll(icon.get(8));
                }
            }
            //SNOW
            if (weather.get(i) >= 600 && weather.get(i) <= 622) {
                if (i == 0) {
                    plus1.getChildren().removeAll(icon);
                    plus1.getChildren().addAll(icon.get(9));
                }
                else if (i == 1) {
                    plus2.getChildren().removeAll(icon);
                    plus2.getChildren().addAll(icon.get(10));
                }
                else if (i == 2) {
                    plus3.getChildren().removeAll(icon);
                    plus3.getChildren().addAll(icon.get(11));
                }
            }
            //RAIN
            if (weather.get(i) >= 300 && weather.get(i) <= 531) {
                if (i == 0) {
                    plus1.getChildren().removeAll(icon);
                    plus1.getChildren().add(icon.get(12));
                }
                else if (i == 1) {
                    plus2.getChildren().removeAll(icon);
                    plus2.getChildren().add(icon.get(13));
                }
                else if (i == 2) {
                    plus3.getChildren().removeAll(icon);
                    plus3.getChildren().add(icon.get(14));
                }
            }
            //THUNDER
            if (weather.get(i) >= 200 && weather.get(i) <= 232) {
                if (i == 0) {
                    plus1.getChildren().removeAll(icon);
                    plus1.getChildren().add(icon.get(15));
                }
                else if (i == 1) {
                    plus2.getChildren().removeAll(icon);
                    plus2.getChildren().add(icon.get(16));
                }
                else if (i == 2) {
                    plus3.getChildren().removeAll(icon);
                    plus3.getChildren().add(icon.get(17));
                }
            }
        }
        weather.clear();
    }

    public void changeBg(){
        int weather = getWeather.getW();
        root.getChildren().remove(search);
        //CLOUDS
        if (weather > 800 && weather < 805) {
            root.getChildren().remove(images.get(0));
            root.getChildren().addAll(images.get(0),search);
        }
        //CLEAR
        else if (weather == 800) {
            root.getChildren().remove(images.get(1));
            root.getChildren().addAll(images.get(1),search);
        }
        //FOG
        else if (weather > 700 && weather < 782) {
            root.getChildren().remove(images.get(2));
            root.getChildren().addAll(images.get(2),search);
        }
        //SNOW
        else if (weather >= 600 && weather <= 622) {
            root.getChildren().remove(images.get(3));
            root.getChildren().addAll(images.get(3),search);
        }
        //RAIN
        else if (weather >= 300 && weather <= 531) {
            root.getChildren().remove(images.get(4));
            root.getChildren().addAll(images.get(4),search);
        }
        //THUNDER
        else if (weather >= 200 && weather <= 232) {
            root.getChildren().remove(images.get(5));
            root.getChildren().addAll(images.get(5),search);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
