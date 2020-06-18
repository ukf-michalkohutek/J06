package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Locale;

import org.json.JSONObject;
import org.json.JSONPropertyName;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main extends Application {

    private ImageView background;
    public TextField search;
    public static double abc, teplota1, teplota2;
    public static int pocasicko;
    public static String xxxxx;
    Image i1, i2, i3, i4, i5, i6, i7;
    Label info, den1, den2, den3;
    private long dt;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();
        Scene scene = new Scene(root, 480, 600);
        i1 = new Image("clouds.jpg", 480, 600, false, false);
        i2 = new Image("clearsky.jpg", 480, 600, false, false);
        i3 = new Image("fog.jpg", 480, 600, false, false);
        i4 = new Image("snowy.jpg", 480, 600, false, false);
        i5 = new Image("rain.jpg", 480, 600, false, false);
        i6 = new Image("thunder.jpg", 480, 600, false, false);
        i7 = new Image("bg.png", 480, 600, false, false);

        background = new ImageView(i7);

        search = new TextField();
        search.setMaxSize(250, 30);
        search.setLayoutX(120);
        search.setLayoutY(80);
        search.setAlignment(Pos.CENTER);
        search.setPromptText(getFromJson());
        search.setFocusTraversable(false);

        xxxxx = getFromJson();
        callMe();


        Label dateLabel = new Label();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EE, dd.MM.yyyy", new Locale("en"));
        LocalDateTime now = LocalDateTime.now();
        dateLabel.setText(dtf.format(now));
        dateLabel.setLayoutX(150);
        dateLabel.setLayoutY(160);

        Label nameOfApp = new Label("JV WeatherApp");
        nameOfApp.setLayoutX(190);
        nameOfApp.setLayoutY(10);

        Label teplota = new Label();
        teplota.setLayoutX(175);
        teplota.setLayoutY(240);
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        teplota.setEffect(ds);



        HBox predpoved = new HBox();
        HBox dni = new HBox();

        den1 = new Label();
        den2 = new Label();
        den3 = new Label();
        dni.setLayoutX(40);
        dni.setLayoutY(450);
        dni.setSpacing(70);
        dni.getChildren().addAll(den1, den2, den3);

        Label teplotka1 = new Label();
        Label teplotka2 = new Label();
        Label teplotka3 = new Label();
        predpoved.setLayoutY(500);
        predpoved.setLayoutX(50);
        predpoved.setSpacing(80);

        teplota.setText((Math.round(abc * 10) / 10.0) + "°C");
        teplotka1.setText((Math.round(abc * 10) / 10.0) + "°C");
        teplotka2.setText((Math.round(teplota1 * 10) / 10.0) + "°C");
        teplotka3.setText((Math.round(teplota2 * 10) / 10.0) + "°C");
        predpoved.getChildren().addAll(teplotka1, teplotka2, teplotka3);


        info = new Label();
        info.setLayoutY(300);
        showDates();
        setBG();
        root.getChildren().addAll(background, search, dateLabel, nameOfApp, teplota, info, predpoved, dni);
        root.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            if (key == KeyCode.ENTER) {
                addToJson(getSearch());

                xxxxx = search.getText();
                try {
                    callMe();
                } catch (Exception exception) {
                    System.out.println("\n" + exception.toString() + "\nChybička");
                }
                teplota.setText((Math.round(abc * 10) / 10.0) + "°C");
                teplotka1.setText((Math.round(abc * 10) / 10.0) + "°C");
                teplotka2.setText((Math.round(teplota1 * 10) / 10.0) + "°C");
                teplotka3.setText((Math.round(teplota2 * 10) / 10.0) + "°C");
                root.getChildren().remove(background);
                FadeTransition rt = new FadeTransition();
                rt.setDuration(Duration.millis(500));
                rt.setNode(background);
                rt.setFromValue(0.2);
                rt.setToValue(1.0);
                rt.play();
                setBG();
                root.getChildren().add(background);
                background.toBack();
                if (abc < 10.0) {
                    teplota.setLayoutX(185);
                } else teplota.setLayoutX(165);
            }
        });

        callMe();

        root.getStylesheets().add("sample/stylesheet.css");
        dateLabel.getStyleClass().add("date-label");
        search.getStyleClass().add("search-bar");
        nameOfApp.getStyleClass().add("name-of-app");
        teplota.getStyleClass().add("tempeture");
        teplotka1.getStyleClass().add("temp-1");
        teplotka2.getStyleClass().add("temp-2");
        teplotka3.getStyleClass().add("temp-3");
        den1.getStyleClass().add("day-3");
        den2.getStyleClass().add("day-3");
        den3.getStyleClass().add("day-3");
        info.getStyleClass().add("info");
        primaryStage.setTitle("Weather App");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.toFront();
        primaryStage.getIcons().add(new Image("icon.png"));
    }

    public String getSearch() {
        return search.getText();
    }


    public static void callMe() throws Exception {
//        String url = "http://api.openweathermap.org/data/2.5/weather?q="+xxxxx+"&appid=2b753eec567dbf89c62d6b1dd4878c7a&units=metric";
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + xxxxx + "&appid=2b753eec567dbf89c62d6b1dd4878c7a&units=metric";
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
        String odozva = response.toString();
        System.out.println(odozva);

        JSONObject outputjson = new JSONObject(odozva);
        JSONObject list = outputjson.getJSONArray("list").getJSONObject(0);
        long dt1 = list.getLong("dt");

        JSONObject listt = outputjson.getJSONArray("list").getJSONObject(1);
        long dt2 = listt.getLong("dt");

        JSONObject listtt = outputjson.getJSONArray("list").getJSONObject(2);
        long dt3 = listtt.getLong("dt");


        String temp[] = odozva.split("\"dt\":" + dt1 + ",\"main\":\\{\"temp\":");
        String temp2[] = temp[1].split(",\"feels_like");
        String temp3[] = odozva.split("\"dt\":" + dt2 + ",\"main\":\\{\"temp\":");
        String temp4[] = temp3[1].split(",\"feels_like\"");
        String temp5[] = odozva.split("\"dt\":" + dt3 + ",\"main\":\\{\"temp\":");
        String temp6[] = temp5[1].split(",\"feels_like\"");
        String temp7[] = odozva.split("\"weather\":\\[\\{\"id\":");
        String temp8[] = temp7[1].split(",\"main\":");

        String tempeture = temp2[0];
        String tepl1 = temp4[0];
        String tepl2 = temp6[0];
        String wedr = temp8[0];

        System.out.println(tempeture + " °C");
        System.out.println(tepl1 + " °C");
        System.out.println(tepl2 + " °C");
        System.out.println(wedr);
        abc = Double.valueOf(tempeture);
        teplota1 = Double.valueOf(tepl1);
        teplota2 = Double.valueOf(tepl2);
        pocasicko = Integer.valueOf(wedr);
    }

    public void setBG() {
        //CLOUDS
        if (pocasicko > 800 && pocasicko < 805) {
            background.setImage(i1);
            info.setText("Clouds");
            info.setLayoutX(190);
        }
        //CLEAR
        else if (pocasicko == 800) {
            background.setImage(i2);
            info.setText("Clear sky");
            info.setLayoutX(175);
        }
        //FOG
        else if (pocasicko > 700 && pocasicko < 782) {
            background = new ImageView(i3);
            info.setText("Fog");
            info.setLayoutX(210);
        }
        //SNOW
        else if (pocasicko >= 600 && pocasicko <= 622) {
            background = new ImageView(i4);
            info.setText("Snow");
            info.setLayoutX(200);
        }
        //RAIN
        else if (pocasicko >= 300 && pocasicko <= 531) {
            background = new ImageView(i5);
            info.setText("Rain");
            info.setLayoutX(210);
        }
        //THUNDER
        else if (pocasicko >= 200 && pocasicko <= 232) {
            background = new ImageView(i6);
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
        String deen1 = dayz.format(d1);
        String deen2 = dayz.format(d2);
        String deen3 = dayz.format(d3);
        den1.setText(deen1);
        den2.setText(deen2);
        den3.setText(deen3);
    }

    public void addToJson(String mesto) {
        org.json.simple.JSONObject cities = new org.json.simple.JSONObject();
        cities.put("city", mesto);

        try (FileWriter file = new FileWriter("lastCity.json")) {
            file.write(cities.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("----------------\n" + cities);
    }

    public String getFromJson() {
        JSONParser parser = new JSONParser();

        try {
            Object objj = parser.parse(new FileReader("lastCity.json"));
            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) objj;
            String city = (String) jsonObject.get("city");
            return city;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "2123";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
