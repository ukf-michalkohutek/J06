package sample;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.jar.JarException;

public class Controller {

    private String APIKEY = "84fdec0f67c06b68eb7ccb6e4582d0ac";
    @FXML private TextField CommandPane;
    SQLiteJDBC sqlConnector;

    // Datumy prveho miesta
    @FXML private Label firstDate;
    @FXML private Label secondDate;
    @FXML private Label thirdDate;

    @FXML private Label city;
    @FXML private Label cityFirst;
    @FXML private Label citySecond;
    @FXML private Label cityThird;

    // File kde sa ukladaju posledne miesta
    private File file;

    // staticka premenna pre prvy datum
    // frC a jeho podobeniny su pre farbu
    static String fr = "";
    static String frC = "";
    static String sc = "";
    static String scC = "";
    static String th = "";
    static String thC = "";


    protected void connectDatabase() {
        sqlConnector = new SQLiteJDBC();
    }

    @FXML
    protected void initialize() throws SQLException {
        connectDatabase();
        initializeFirstCity();

        boolean created = false;

        // Vytvori sa file kde sa ukladaju mesta
        this.file = new File("lastCity.txt");

        try {
            if (file.createNewFile())
            {
                System.out.println("File is created!");
                created = true;
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!created) {
            initializeSecondCity();
        }


    }

    // Nitra je ako default
    private void initializeFirstCity() {
        downloadCurrentWeatherDataByID(sqlConnector.getIdFromName("Nitra"));

        this.firstDate.setText(fr);
        this.secondDate.setText(sc);
        this.thirdDate.setText(th);

    }

    // Posledne vyhladane mesto alebo co sa napise do konzoly
    private void initializeSecondCity() {
        String line = "";
        try {
            FileReader fr = new FileReader(this.file);
            BufferedReader br = new BufferedReader(fr);

            line = br.readLine();

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        parseSecondCity(line);
    }

    // Preparsuje text kde '@' je delimiter
    private void parseSecondCity(String line) {

        String[] arr = line.split("@");

        this.city.setText(arr[0]);
        this.cityFirst.setText(arr[1]);
        this.cityFirst.setTextFill(Paint.valueOf(arr[2]));
        this.citySecond.setText(arr[3]);
        this.citySecond.setTextFill(Paint.valueOf(arr[4]));
        this.cityThird.setText(arr[5]);
        this.cityThird.setTextFill(Paint.valueOf(arr[6]));


    }

    private void downloadCurrentWeatherDataByID(long id) {
        HttpClient client = HttpClient.newHttpClient();
        String idUrl = "https://api.openweathermap.org/data/2.5/forecast?id=" + id + "&units=metric&appid=" + APIKEY;

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(idUrl)).build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(Controller::parseCurrentWeather)
                    .join();
    }

    // Preparsuje JSON a naplni data
    private static String parseCurrentWeather(String response) {
        fr = "";
        sc = "";
        th = "";
        JSONArray resultDates = new JSONArray("[" + response + "]" );

        JSONArray list = null;
        try {
            list = resultDates.getJSONObject(0).getJSONArray("list");
        }catch (Exception e) {
            System.out.println("Invalid json");
            return null;
        }


        JSONObject dt1 = list.getJSONObject(0);
        JSONArray wh1 = dt1.getJSONArray("weather");
        String de1 = wh1.getJSONObject(0).getString("description");


        JSONObject dt2 = list.getJSONObject(8);
        JSONArray wh2 = dt2.getJSONArray("weather");
        String de2 = wh2.getJSONObject(0).getString("description");


        JSONObject dt3 = list.getJSONObject(16);
        JSONArray wh3 = dt3.getJSONArray("weather");
        String de3 = wh3.getJSONObject(0).getString("description");




        Object da1 = dt1.get("dt_txt");
        JSONObject main1 = dt1.getJSONObject("main");
        Object temperature1 = main1.get("temp");

        // Teplota pre prvy den + farba textu
        if (Double.parseDouble(temperature1.toString()) < 10) {
            frC = "blue";
        }else if (Double.parseDouble(temperature1.toString()) >= 10 && Double.parseDouble(temperature1.toString()) < 17) {
            frC = "orange";
        }else {
            frC = "red";
        }

        String date1 = da1.toString().substring(0, 10);
        fr = date1 + " || " + de1 + " || Temperature: " + temperature1;

        Object da2 = dt2.get("dt_txt");
        JSONObject main2 = dt2.getJSONObject("main");
        Object temperature2 = main2.get("temp");

        // Teplota pre druhy den + farba textu
        if (Double.parseDouble(temperature2.toString()) < 10) {
            scC = "blue";
        }else if (Double.parseDouble(temperature2.toString()) >= 10 && Double.parseDouble(temperature2.toString()) < 17) {
            scC = "orange";
        }else {
            scC = "red";
        }

        String date2 = da2.toString().substring(0, 10);
        sc = date2  + " || " + de2 + " || Temperature: " + temperature2;

        Object da3 = dt3.get("dt_txt");
        JSONObject main3 = dt3.getJSONObject("main");
        Object temperature3 = main3.get("temp");

        // Teplota pre treti den + farba textu
        if (Double.parseDouble(temperature3.toString()) < 10) {
            thC = "blue";
        }else if (Double.parseDouble(temperature3.toString()) >= 10 && Double.parseDouble(temperature3.toString()) < 17) {
            thC = "orange";
        }else {
            thC = "red";
        }

        String date3 = da3.toString().substring(0, 10);
        th = date3 + " || " + de3 + " || Temperature: " + temperature3;

        return null;
    }

    // Po stlaceni klavesy Execute sa vyhlada mesto
    public void execute(ActionEvent event) {
        String givenCity = this.CommandPane.getText();



        try {
            downloadCurrentWeatherDataByID(sqlConnector.getIdFromName(givenCity));
            String content = givenCity + "@" + fr + "@" + frC + "@" + sc + "@" + scC + "@" + th + "@" + thC;

            FileWriter fw = new FileWriter(this.file, false);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(content);
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        this.city.setText(givenCity);
        this.cityFirst.setText(fr);
        this.cityFirst.setTextFill(Paint.valueOf(frC));
        this.citySecond.setText(sc);
        this.citySecond.setTextFill(Paint.valueOf(scC));
        this.cityThird.setText(th);
        this.cityThird.setTextFill(Paint.valueOf(thC));

        this.CommandPane.setText("");


    }
}
