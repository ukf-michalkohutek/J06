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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Locale;

import org.json.JSONObject;

public class Main extends Application {

    private ImageView icon, upperview, lowerview, windview;
    public TextField search;
    public static double weatherToday, weatherTomorrow, weatherTomorroww, weatherTomorrowww, windSpeedDouble;
    public static int weatherIDinteger;
    public static String responseString, temperature0, temperature1, temperature2, temperature3, weatherIDString, windSpeed;
    public static String cityString = "Trstice";
    Image upper, lower, clouds, clear, foggy, snowy, rainy, thunder, background, wind;
    Label weatherID, day1Label, day2Label, day3Label, windtext, temperaturetext;
    private int scenewidth = 630;
    private int width = 640;
    private int heigth = 600;
    private int iconwidth = 100;
    private int iconheigth = 80;
    private String windString = "Wind: ";

    @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();
        Scene scene = new Scene(root, scenewidth, heigth);

        //region images
        upper = new Image("upper.jpg", width, 200, false, false);
        lower = new Image("lower.jpg", width, 400, false, false);
        clouds = new Image("clouds.png", iconwidth, iconheigth, false, false);
        clear = new Image("sunny.png", iconwidth, 90, false, false);
        foggy = new Image("foggy.png", iconwidth, iconheigth, false, false);
        snowy = new Image("snowy.png", iconwidth, iconheigth, false, false);
        rainy = new Image("rainy.png", iconwidth, iconheigth, false, false);
        thunder = new Image("thunder.png", iconwidth, iconheigth, false, false);
        background = new Image("background.jpg", iconwidth, iconheigth, false, false);
        wind = new Image("wind.png", 60, 60, false, false);
        //endregion

        //region imageviews
        icon = new ImageView(background);
        icon.setLayoutX(80);
        icon.setLayoutY(50);

        upperview = new ImageView(upper);
        lowerview = new ImageView(lower);
        lowerview.setLayoutY(200);
        //endregion

        //region searchbar
        search = new TextField();
        search.setMaxSize(250, 30);
        search.setLayoutX(320);
        search.setLayoutY(50);
        search.setAlignment(Pos.CENTER);
        search.setPromptText("Trstice");
        search.setFocusTraversable(false);
        //endregion

        update();

        //region dateLabel
        Label dateLabel = new Label();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", new Locale("en"));
        LocalDateTime now = LocalDateTime.now();
        dateLabel.setText(dtf.format(now));
        dateLabel.setTextFill(Color.WHITE);
        dateLabel.setLayoutX(335);
        dateLabel.setLayoutY(150);
        //endregion

        //region today
        Label weatherToday = new Label();
        weatherToday.setLayoutX(width/2 +50);
        weatherToday.setLayoutY(235);
        weatherToday.setTextFill(Color.WHITE);
        weatherToday.setText((Math.round(Main.weatherToday * 10) / 10.0) + "°C");

        temperaturetext = new Label("Temperature:");
        temperaturetext.setLayoutX((width/2) -195);
        temperaturetext.setLayoutY(240);

        //endregion

        //region wind
        windview = new ImageView(wind);
        windview.setLayoutY(300);
        windview.setLayoutX(150);

        windtext = new Label(windString);
        windtext.setTextFill(Color.WHITE);
        windtext.setLayoutX((width/2)-110);
        windtext.setLayoutY(330);

        Label weather5 = new Label();
        weather5.setLayoutX((width/2) -20);
        weather5.setLayoutY(330);
        weather5.setText((Math.round(windSpeedDouble * 10) / 10.0) + " m/s");
        //endregion

        //region forecast
        HBox hbox = new HBox();

        Label weather2 = new Label();
        Label weather3 = new Label();
        Label weather4 = new Label();

        weather2.setText((Math.round(weatherTomorrow * 10) / 10.0) + "°C");
        weather3.setText((Math.round(weatherTomorroww * 10) / 10.0) + "°C");
        weather4.setText((Math.round(weatherTomorrowww * 10) / 10.0) + "°C");

        hbox.getChildren().addAll(weather2, weather3, weather4);

        hbox.setLayoutY(500);
        hbox.setLayoutX(100);
        hbox.setSpacing(100);

        //region forecastdays
        HBox hboxDays = new HBox();

        day1Label = new Label();
        day2Label = new Label();
        day3Label = new Label();

        hboxDays.setLayoutX(95);
        hboxDays.setLayoutY(450);
        hboxDays.setSpacing(70);
        hboxDays.getChildren().addAll(day1Label, day2Label, day3Label);

        //endregion

        //endregion

        weatherID = new Label();
        weatherID.setLayoutY(150);
        weatherID.setLayoutX(100);
        weatherID.setTextFill(Color.WHITESMOKE);

        showDates();
        icon();

        root.getChildren().addAll(upperview, lowerview, icon, search, dateLabel,
                weatherToday, temperaturetext, weatherID, windtext, weather5, hbox, hboxDays);

        update();
        dataPrint();

        root.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            if (key == KeyCode.ENTER) {
                cityString = search.getText();
                try {
                    update();
                } catch (Exception exception) {
                    System.out.println(exception);
                }

                weatherToday.setText((Math.round(Main.weatherToday * 10) / 10.0) + "°C");
                weather2.setText((Math.round(weatherTomorrow * 10) / 10.0) + "°C");
                weather3.setText((Math.round(weatherTomorroww * 10) / 10.0) + "°C");
                weather4.setText((Math.round(weatherTomorrowww * 10) / 10.0) + "°C");
                weather5.setText((Math.round(windSpeedDouble * 10) / 10.0) + " m/s");

                root.getChildren().remove(icon);
                icon();
                root.getChildren().add(icon);
            }
        });

        //region styling
        root.getStylesheets().add("sample/css.css");
        dateLabel.getStyleClass().add("date-label");
        search.getStyleClass().add("search-bar");
        weatherToday.getStyleClass().add("weatherToday");
        temperaturetext.getStyleClass().add("weatherTodayText");
        weather2.getStyleClass().add("temperature");
        weather3.getStyleClass().add("temperature");
        weather4.getStyleClass().add("temperature");
        weather5.getStyleClass().add("wind");
        windtext.getStyleClass().add("wind");
        day1Label.getStyleClass().add("days");
        day2Label.getStyleClass().add("days");
        day3Label.getStyleClass().add("days");
        weatherID.getStyleClass().add("weatherID");
        primaryStage.setTitle("Weather App");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.toFront();
        primaryStage.getIcons().add(new Image("icon.png"));
        //endregion

        primaryStage.setResizable(true);
        primaryStage.setTitle("Weather Application");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.toFront();
        primaryStage.getIcons().add(new Image("icon.png"));
    }

    public static void update() throws Exception {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityString + "&appid=2b753eec567dbf89c62d6b1dd4878c7a&units=metric";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String input;
        StringBuffer response = new StringBuffer();
        while ((input = in.readLine()) != null) {
            response.append(input);
        }
        in.close();
        responseString = response.toString();


        JSONObject jsonObject = new JSONObject(responseString);
        JSONObject list0 = jsonObject.getJSONArray("list").getJSONObject(0);
        long date0 = list0.getLong("dt");

        JSONObject list1 = jsonObject.getJSONArray("list").getJSONObject(8);
        long date1 = list1.getLong("dt");

        JSONObject list2 = jsonObject.getJSONArray("list").getJSONObject(16);
        long date2 = list2.getLong("dt");

        JSONObject list3 = jsonObject.getJSONArray("list").getJSONObject(24);
        long date3 = list3.getLong("dt");

        String split0[] = responseString.split("\"dt\":" + date0 + ",\"main\":\\{\"temp\":");
        String split1[] = split0[1].split(",\"feels_like");
        String split2[] = responseString.split("\"wind\":\\{\"speed\":");
        String split3[] = split2[1].split(",\"deg\":");
        String split4[] = responseString.split("\"dt\":" + date1 + ",\"main\":\\{\"temp\":");
        String split5[] = split4[1].split(",\"feels_like\"");
        String split6[] = responseString.split("\"dt\":" + date2 + ",\"main\":\\{\"temp\":");
        String split7[] = split6[1].split(",\"feels_like\"");
        String split8[] = responseString.split("\"dt\":" + date3 + ",\"main\":\\{\"temp\":");
        String split9[] = split8[1].split(",\"feels_like\"");
        String split10[] = responseString.split("\"weather\":\\[\\{\"id\":");
        String split11[] = split10[1].split(",\"main\":");

        temperature0 = split1[0];
        temperature1 = split5[0];
        temperature2 = split7[0];
        temperature3 = split9[0];
        weatherIDString = split11[0];
        windSpeed = split3[0];

        weatherToday = Double.valueOf(temperature0);
        weatherTomorrow = Double.valueOf(temperature1);
        weatherTomorroww = Double.valueOf(temperature2);
        weatherTomorrowww = Double.valueOf(temperature3);
        weatherIDinteger = Integer.valueOf(weatherIDString);
        windSpeedDouble = Double.valueOf(windSpeed);
    }

    private void dataPrint(){
        System.out.println(temperature0 + " °C");
        System.out.println();
        System.out.println(temperature1 + " °C");
        System.out.println(temperature2 + " °C");
        System.out.println(temperature3 + " °C");
        System.out.println();
        System.out.println(weatherIDString);
        System.out.println(windSpeed + " m/s");
    }

    public void icon() {
        if (weatherIDinteger > 800) {
            icon.setImage(clouds);
            weatherID.setText("Clouds");
            icon.setLayoutX(80);
            icon.setLayoutY(50);
        }else if (weatherIDinteger == 800) {
            icon.setImage(clear);
            weatherID.setText("Clear");
            icon.setLayoutX(80);
            icon.setLayoutY(50);
        }else if (weatherIDinteger >= 700 && weatherIDinteger < 800) {
            icon = new ImageView(foggy);
            weatherID.setText("Foggy");
            icon.setLayoutX(80);
            icon.setLayoutY(50);
        }else if (weatherIDinteger >= 600 && weatherIDinteger < 700) {
            icon = new ImageView(snowy);
            weatherID.setText("Snow");
            icon.setLayoutX(80);
            icon.setLayoutY(50);
        } else if (weatherIDinteger >= 300 && weatherIDinteger < 600) {
            icon = new ImageView(rainy);
            weatherID.setText("Rain");
            icon.setLayoutX(80);
            icon.setLayoutY(50);
        }else if (weatherIDinteger >= 200 && weatherIDinteger < 300) {
            icon = new ImageView(thunder);
            weatherID.setText("Thunder");
            icon.setLayoutX(80);
            icon.setLayoutY(50);
        }
    }

    public void showDates() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime d1 = today.plusDays(1);
        LocalDateTime d2 = d1.plusDays(1);
        LocalDateTime d3 = d2.plusDays(1);

        DateTimeFormatter dayz = DateTimeFormatter.ofPattern("EEEE", new Locale("en"));
        String day1String = dayz.format(d1);
        String day2String = dayz.format(d2);
        String day3String = dayz.format(d3);
        day1Label.setText(day1String);
        day2Label.setText(day2String);
        day3Label.setText(day3String);
    }

    public static void main(String[] args) {
        launch(args);
    }
}