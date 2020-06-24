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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class Main extends Application {

    Group root = new Group();
    VBox tempnow = new VBox();
    VBox plus1 = new VBox(10);
    VBox plus2 = new VBox(10);

    GetWeather getWeather;

    private ArrayList<ImageView> obrazok = new ArrayList<>();
    private ArrayList<LocalDateTime> cas = new ArrayList<>();
    private ArrayList<Integer> pocasie = new ArrayList<>();

    private TextField search = new TextField("City + ENTER");

    private Label d0 = new Label(), d1 = new Label(), d2 = new Label();
    private Label timenow = new Label(), time1 = new Label(), time2 = new Label();


    @Override
    public void start(Stage primaryStage) throws Exception{

        Image back = new Image("/Backgrounds/0.jpg",360,540,false,false);
        ImageView bg = new ImageView(back);

        root.getChildren().addAll(bg,search);



        for (int i = 0; i < 6; i++){
            Image image = new Image("/Images/" + i + ".png", 50,50,false,true);
            obrazok.add(new ImageView(image));
            obrazok.add(new ImageView(image));
            obrazok.add(new ImageView(image));
        }

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime n1 = today.plusDays(1);
        LocalDateTime n2 = today.plusDays(2);
        cas.add(today);
        cas.add(n1);
        cas.add(n2);

        search.setLayoutX(100);
        search.setLayoutY(10);
        search.setPrefSize(160,25);
        search.setAlignment(Pos.CENTER);
        search.setFocusTraversable(false);

        tempnow.setAlignment(Pos.CENTER);
        tempnow.setLayoutY(40);
        tempnow.setPrefSize(360,100);

        plus1.setAlignment(Pos.BASELINE_CENTER);
        plus1.setLayoutY(240);
        plus1.setPrefSize(360,180);

        plus2.setAlignment(Pos.BASELINE_CENTER);
        plus2.setLayoutY(380);
        plus2.setPrefSize(360,180);

        root.getStylesheets().add("sample/Stylesheet.css");
        primaryStage.setTitle("Weather App");
        Scene scene = new Scene(root,360, 540);
        primaryStage.setScene(scene);
        primaryStage.show();

        search.setOnKeyPressed(e -> {
            KeyCode k = e.getCode();
            switch (k){
                case ENTER:
                    try {
                        getWeather = new GetWeather(getCity());
                        showdata();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
            }

        });
    }

    public void showdata(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE dd.MM", new Locale("sk"));
        root.getChildren().removeAll(tempnow,plus1,plus2);
        tempnow.getChildren().removeAll(d0,timenow);
        plus1.getChildren().removeAll(time1,d1);
        plus2.getChildren().removeAll(time2,d2);

        d0 = new Label(String.valueOf((int)getWeather.getDay0()) + "°");
        d0.getStyleClass().add("d0");

        timenow = new Label(dateTimeFormatter.format(cas.get(0)));
        timenow.getStyleClass().add("timenow");

        tempnow.getChildren().addAll(d0,timenow);

        d1 = new Label(String.valueOf((int)getWeather.getDay1()) + "°");
        d1.getStyleClass().add("d1-d2");

        time1 = new Label(dateTimeFormatter.format(cas.get(1)));
        time1.getStyleClass().add("timenow");

        plus1.getChildren().addAll(time1,d1);

        d2 = new Label(String.valueOf((int)getWeather.getDay2()) + "°");
        d2.getStyleClass().add("d1-d2");

        time2 = new Label(dateTimeFormatter.format(cas.get(2)));
        time2.getStyleClass().add("timenow");

        plus2.getChildren().addAll(time2,d2);

        changeIcon();

        root.getChildren().addAll(tempnow,plus1,plus2);
    }

    public String getCity(){
        return search.getText();
    }

    public void changeIcon(){
        int weather0 = getWeather.getW();
        int weather1 = getWeather.getW1();
        int weather2 = getWeather.getW2();

        pocasie.add(weather0);
        pocasie.add(weather1);
        pocasie.add(weather2);

        for (int i = 0; i < pocasie.size(); i++) {

            if (pocasie.get(i) > 800 && pocasie.get(i) < 805) {
                if (i == 0) {
                    tempnow.getChildren().removeAll(obrazok);
                    tempnow.getChildren().addAll(obrazok.get(0));
                }
                if (i == 1) {
                    plus1.getChildren().removeAll(obrazok);
                    plus1.getChildren().addAll(obrazok.get(1));
                }
                else if (i == 2) {
                    plus2.getChildren().removeAll(obrazok);
                    plus2.getChildren().addAll(obrazok.get(2));
                }
            }

            if (pocasie.get(i) == 800) {
                if (i == 0) {
                    tempnow.getChildren().removeAll(obrazok);
                    tempnow.getChildren().addAll(obrazok.get(3));
                }
                if (i == 1) {
                    plus1.getChildren().removeAll(obrazok);
                    plus1.getChildren().addAll(obrazok.get(4));
                }
                else if (i == 2) {
                    plus2.getChildren().removeAll(obrazok);
                    plus2.getChildren().addAll(obrazok.get(5));
                }
            }

            if (pocasie.get(i) > 700 && pocasie.get(i) < 782) {
                if (i == 0) {
                    tempnow.getChildren().removeAll(obrazok);
                    tempnow.getChildren().addAll(obrazok.get(6));
                }
                if (i == 1) {
                    plus1.getChildren().removeAll(obrazok);
                    plus1.getChildren().addAll(obrazok.get(7));
                }
                else if (i == 2) {
                    plus2.getChildren().removeAll(obrazok);
                    plus2.getChildren().addAll(obrazok.get(8));
                }
            }

            if (pocasie.get(i) >= 600 && pocasie.get(i) <= 622) {
                if (i == 0) {
                    tempnow.getChildren().removeAll(obrazok);
                    tempnow.getChildren().addAll(obrazok.get(9));
                }
                if (i == 1) {
                    plus1.getChildren().removeAll(obrazok);
                    plus1.getChildren().addAll(obrazok.get(10));
                }
                else if (i == 2) {
                    plus2.getChildren().removeAll(obrazok);
                    plus2.getChildren().addAll(obrazok.get(11));
                }
            }

            if (pocasie.get(i) >= 300 && pocasie.get(i) <= 531) {
                if (i == 0) {
                    tempnow.getChildren().removeAll(obrazok);
                    tempnow.getChildren().addAll(obrazok.get(12));
                }
                if (i == 1) {
                    plus1.getChildren().removeAll(obrazok);
                    plus1.getChildren().addAll(obrazok.get(13));
                }
                else if (i == 2) {
                    plus2.getChildren().removeAll(obrazok);
                    plus2.getChildren().addAll(obrazok.get(14));
                }
            }

            if (pocasie.get(i) >= 200 && pocasie.get(i) <= 232) {
                if (i == 0) {
                    tempnow.getChildren().removeAll(obrazok);
                    tempnow.getChildren().addAll(obrazok.get(15));
                }
                if (i == 1) {
                    plus1.getChildren().removeAll(obrazok);
                    plus1.getChildren().addAll(obrazok.get(16));
                }
                else if (i == 2) {
                    plus2.getChildren().removeAll(obrazok);
                    plus2.getChildren().addAll(obrazok.get(17));
                }
            }
        }
        pocasie.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
