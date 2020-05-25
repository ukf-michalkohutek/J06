package weatherAppExample;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Controller {
    //prve mesto
    @FXML private Button findtown;
    @FXML private TextField putInCity;
    @FXML private Label MainCity;
    @FXML private Label MainTempeture;
    @FXML private Label dayOne;
    @FXML private Label dayOneTemp;
    @FXML private Label dayTwo;
    @FXML private Label dayTwoTemp;
    @FXML private Label dayThree;
    @FXML private Label dayThreeTemp;
    //druhé mesto
    @FXML private Button findtown2;
    @FXML private TextField putInCity2;
    @FXML private Label NotMainCity;
    @FXML private Label NotMainTempeture;
    @FXML private Label dayOne2;
    @FXML private Label dayOneTemp2;
    @FXML private Label dayTwo2;
    @FXML private Label dayTwoTemp2;
    @FXML private Label dayThree2;
    @FXML private Label dayThreeTemp2;


    @FXML private Text datetime;
    static double lat;
    static double lon;

    static String first = "";
    static String second = "";
    static String third = "";


    private String APIKEY = "deddd17cf983d14b4964831776c6f67c";
    SQLiteJDBC sqlConnector;
    static conclusion[] conclusionArray;
    static String currentTempPom;

    protected void connectDatabase() {
        sqlConnector = new SQLiteJDBC();
    }

    @FXML
    protected void initialize() {
        connectDatabase();
        dateTime();
        conclusionArray = new conclusion[3];
        currentTempPom = "";
        find();
        find2();
        ShowNextThreeDays();
    }
    public void find(){
        findtown.setOnAction(event -> {
            long townId = sqlConnector.getIdFromName(putInCity.getText());
            if(townId == 0){
                MainTempeture.setText("City not found");
                dayOneTemp.setText("City not found");
                dayTwoTemp.setText("City not found");
                dayThreeTemp.setText("City not found");

            }else{
                long idCity = sqlConnector.getIdFromName(putInCity.getText());
                getCoordsFromId(idCity);
                downloadCurrentWeatherDataByID();
                MainCity.setText(putInCity.getText());
                MainTempeture.setText(currentTempPom+" °C");
                putInCity.setText("");
                dayOneTemp.setText(conclusionArray[0].getTemp()+"°C");
                dayTwoTemp.setText(conclusionArray[1].getTemp() +"°C");
                dayThreeTemp.setText(conclusionArray[2].getTemp() +"°C");

            }});
    }
    public void find2() {
        findtown2.setOnAction(event -> {
            long townId2 = sqlConnector.getIdFromName(putInCity2.getText());
            if (townId2 == 0) {
                NotMainTempeture.setText("City not found");
                dayOneTemp2.setText("City not found");
                dayTwoTemp2.setText("City not found");
                dayThreeTemp2.setText("City not found");

            } else {
                long idCity2 = sqlConnector.getIdFromName(putInCity2.getText());
                getCoordsFromId(idCity2);
                downloadCurrentWeatherDataByID();
                NotMainCity.setText(putInCity2.getText());
                NotMainTempeture.setText(currentTempPom + " °C");
                putInCity2.setText("");
                dayOneTemp2.setText(conclusionArray[0].getTemp() + "°C");
                dayTwoTemp2.setText(conclusionArray[1].getTemp() + "°C");
                dayThreeTemp2.setText(conclusionArray[2].getTemp() + "°C");

            }
        });
    }
    public void dateTime(){
       /*Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime currentTime = LocalTime.now();
            time.setText(currentTime.getHour()+":"+currentTime.getMinute()+":"+currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();*/


        LocalDateTime now = LocalDateTime.now();                                                       //from stackOverFlow :|
        DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = now.format(dateTimeFormatter);
        datetime.setText(formatDateTime);
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalDateTime now1 = LocalDateTime.now();
            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formatDateTime1 = now1.format(format1);
            datetime.setText(formatDateTime1);
        }));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    public void ShowNextThreeDays() {
        dayOne.setText(LocalDateTime.now().getDayOfWeek().toString().toLowerCase());
        dayTwo.setText(LocalDateTime.now().getDayOfWeek().plus(1).toString().toLowerCase());
        dayThree.setText(LocalDateTime.now().getDayOfWeek().plus(2).toString().toLowerCase());
        dayOne2.setText(LocalDateTime.now().getDayOfWeek().toString().toLowerCase());
        dayTwo2.setText(LocalDateTime.now().getDayOfWeek().plus(1).toString().toLowerCase());
        dayThree2.setText(LocalDateTime.now().getDayOfWeek().plus(2).toString().toLowerCase());

    }


    //api.openweathermap.org/data/2.5/weather?id={city id}&appid={your api key}

    private void downloadCurrentWeatherDataByID() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=hourly,minutely&units=metric&appid="+APIKEY)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Controller::parseCurrentWeather)
                .join();
    }
    private static String parseCurrentWeather(String responseString){
        JSONParser parser = new JSONParser();
        try {
            JSONObject response = (JSONObject) parser.parse(responseString);
            JSONObject current = (JSONObject) response.get("current");
            currentTempPom = current.get("temp").toString().substring(0,2);
            JSONArray daily = (JSONArray) response.get("daily");

            for (int i = 0; i < 3; i++) {
                JSONObject day = (JSONObject) daily.get(i);
                JSONObject temp = (JSONObject) day.get("temp");

                int tempDay;
                try {
                    tempDay = (int) ((double) temp.get("day"));
                } catch (Exception e) {
                    tempDay = (int) ((long) temp.get("day"));
                }
                conclusionArray[i] = new conclusion(tempDay);
            }
        } catch (Exception e ) {
            System.out.println(e.getMessage());
        }
        return null;

    }


    private void getCoordsFromId(long id) {
        URI uri = URI.create("https://api.openweathermap.org/data/2.5/forecast?id="+id+"&cnt=1&appid="+APIKEY);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Controller::parseCoords)
                .join();
    }

    private static String parseCoords(String responseString) {
        JSONParser parser = new JSONParser();

        try {
            JSONObject response = (JSONObject) parser.parse(responseString);
            JSONObject city = (JSONObject) response.get("city");
            JSONObject coords = (JSONObject) city.get("coord");

           lat = (double) coords.get("lat");
           lon = (double) coords.get("lon");
        }
        catch (ParseException e) { System.out.println(e.getMessage()); }

        return null;
    }

}
