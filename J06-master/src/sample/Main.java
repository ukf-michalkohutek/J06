package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Main extends Application {
    SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
    private ImageView background;
    Image back,cloudy,clear,fog,snow,storm,rain;
    ComboBox<String> combo;
    static double temper1, temper2, temper3;
    static int weather;
    Label info, day1, day2, day3;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 480, 600);

        back = new Image("blackbackground.jpg", 480, 600, false, false);
        cloudy = new Image("cloudy.jpg", 480, 600, false, false);
        clear = new Image("clear.jpg", 480, 600, false, false);
        fog = new Image("fog.jpg", 480, 600, false, false);
        snow = new Image("snow.jpg", 480, 600, false, false);
        rain = new Image("rain.jpg", 480, 600, false, false);
        storm = new Image("storm.jpg", 480, 600, false, false);

        background = new ImageView(back);

        combo = new ComboBox<>();
        sqLiteJDBC.fillCombo(combo);
        combo.getSelectionModel().selectFirst();
        combo.setMaxSize(250, 30);
        combo.setLayoutX(120);
        combo.setLayoutY(80);
        combo.setFocusTraversable(false);

        apiRequest(combo.getSelectionModel().getSelectedItem());


        Label dateLabel = new Label();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EE, dd.MM.yyyy", new Locale("en"));
        LocalDateTime now = LocalDateTime.now();
        dateLabel.setText(dtf.format(now));
        dateLabel.setLayoutX(150);
        dateLabel.setLayoutY(160);

        Label temperature1 = new Label();
        temperature1.setLayoutX(175);
        temperature1.setLayoutY(240);
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        temperature1.setEffect(ds);



        HBox forecast = new HBox();
        HBox days = new HBox();

        day1 = new Label();
        day2 = new Label();
        day3 = new Label();
        days.setLayoutX(40);
        days.setLayoutY(450);
        days.setSpacing(70);
        days.getChildren().addAll(day1, day2, day3);

        Label temperature2 = new Label();
        Label temperature3 = new Label();
        Label temperature4 = new Label();
        forecast.setLayoutY(500);
        forecast.setLayoutX(50);
        forecast.setSpacing(80);

        settemp(temperature1, temperature2, temperature3, temperature4);
        forecast.getChildren().addAll(temperature2, temperature3, temperature4);


        info = new Label();
        info.setLayoutY(300);
        showDates();
        setBackground();
        root.getChildren().addAll(background, combo, dateLabel, temperature1, info, forecast, days);
        root.setFocusTraversable(true);
        root.requestFocus();
        root.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            if (key == KeyCode.ENTER) {
                try {
                    apiRequest(combo.getSelectionModel().getSelectedItem());
                } catch (Exception exception) {
                    System.out.println("\n" + exception.toString() + "\nfault");
                }
                settemp(temperature1, temperature2, temperature3, temperature4);
                root.getChildren().remove(background);
                FadeTransition rt = new FadeTransition();
                rt.setDuration(Duration.millis(500));
                rt.setNode(background);
                rt.setFromValue(0.2);
                rt.setToValue(1.0);
                rt.play();
                setBackground();
                root.getChildren().add(background);
                background.toBack();
                if (temper1 < 10.0) {
                    temperature1.setLayoutX(185);
                } else temperature1.setLayoutX(165);
            }
        });

        apiRequest(combo.getSelectionModel().getSelectedItem());

        root.getStylesheets().add("sample/stylesheet.css");
        dateLabel.getStyleClass().add("date-label");
        combo.getStyleClass().add("combo");
        temperature1.getStyleClass().add("tempeture");
        temperature2.getStyleClass().add("temp-1");
        temperature3.getStyleClass().add("temp-2");
        temperature4.getStyleClass().add("temp-3");
        day1.getStyleClass().add("day-3");
        day2.getStyleClass().add("day-3");
        day3.getStyleClass().add("day-3");
        info.getStyleClass().add("info");
        primaryStage.setTitle("Weather App");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.toFront();
    }

    private void settemp(Label teplota, Label teplotka1, Label teplotka2, Label teplotka3) {
        teplota.setText((Math.round(temper1 * 10) / 10.0) + "°C");
        teplotka1.setText((Math.round(temper1 * 10) / 10.0) + "°C");
        teplotka2.setText((Math.round(temper2 * 10) / 10.0) + "°C");
        teplotka3.setText((Math.round(temper3 * 10) / 10.0) + "°C");
    }

    public static void apiRequest(String city) throws Exception {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=795b308f2fcda5ed5a2a3f4db95f56e9&units=metric";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending GET request to URL : " + url);
        System.out.println("Response code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String apiResponse = response.toString();
        System.out.println(apiResponse);

        JSONObject jsonOutput = new JSONObject(apiResponse);
        JSONObject list = jsonOutput.getJSONArray("list").getJSONObject(0);
        long dt1 = list.getLong("dt");

        JSONObject list1 = jsonOutput.getJSONArray("list").getJSONObject(1);
        long dt2 = list1.getLong("dt");

        JSONObject list2 = jsonOutput.getJSONArray("list").getJSONObject(2);
        long dt3 = list2.getLong("dt");


        String[][] temp = new String[8][];

        temp[0] = apiResponse.split("\"dt\":" + dt1 + ",\"main\":\\{\"temp\":");
        temp[1] = temp[0][1].split(",\"feels_like");
        temp[2] = apiResponse.split("\"dt\":" + dt2 + ",\"main\":\\{\"temp\":");
        temp[3] = temp[2][1].split(",\"feels_like\"");
        temp[4] = apiResponse.split("\"dt\":" + dt3 + ",\"main\":\\{\"temp\":");
        temp[5] = temp[4][1].split(",\"feels_like\"");
        temp[6] = apiResponse.split("\"weather\":\\[\\{\"id\":");
        temp[7] = temp[6][1].split(",\"main\":");

        String temp_1 = temp[1][0];
        String temp_2 = temp[3][0];
        String temp_3 = temp[5][0];
        String weder = temp[7][0];

        System.out.println(temp_1 + " °C");
        System.out.println(temp_2 + " °C");
        System.out.println(temp_3 + " °C");
        System.out.println(weder);
        temper1 = Double.parseDouble(temp_1);
        temper2 = Double.parseDouble(temp_2);
        temper3 = Double.parseDouble(temp_3);
        weather = Integer.parseInt(weder);
    }

    public void setBackground() {
        // best weather all around
        if (weather > 800 && weather < 805) {
            background.setImage(cloudy);
            info.setText("Clouds");
            info.setLayoutX(190);
        }
        // bad weather
        else if (weather == 800) {
            background.setImage(clear);
            info.setText("Clear sky");
            info.setLayoutX(175);
        }
        // vape nation
        else if (weather > 700 && weather < 782) {
            background = new ImageView(fog);
            info.setText("Fog");
            info.setLayoutX(210);
        }
        // ymir has a bad day
        else if (weather >= 600 &&weather <= 622) {
            background = new ImageView(snow);
            info.setText("Snow");
            info.setLayoutX(200);
        }
        // chaac weeps
        else if (weather >= 300 &&weather <= 531) {
            background = new ImageView(rain);
            info.setText("Rain");
            info.setLayoutX(210);
        }
        // thor is grumpy
        else if (weather >= 200 &&weather <= 232) {
            background = new ImageView(storm);
            info.setText("Thunder");
            info.setLayoutX(180);
        }
    }

    public void showDates() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime d1 = today.plusDays(1);
        LocalDateTime d2 = d1.plusDays(1);
        LocalDateTime d3 = d2.plusDays(1);

        DateTimeFormatter dayz = DateTimeFormatter.ofPattern("EEEE", new Locale("en"));
        String day_1 = dayz.format(d1);
        String day_2 = dayz.format(d2);
        String day_3 = dayz.format(d3);
        day1.setText(day_1);
        day2.setText(day_2);
        day3.setText(day_3);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
