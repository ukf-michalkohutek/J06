package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.util.Duration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class WeatherAppController implements Initializable {
    //lava strana
    @FXML
    private ImageView imageViewTop;
    @FXML
    private Text mestoTop;
    @FXML
    private Text tempTop;
    @FXML
    private Text den1;
    @FXML
    private Text den1Temp;
    @FXML
    private Text den2;
    @FXML
    private Text den2Temp;
    @FXML
    private Text den3;
    @FXML
    private Text den3Temp;
    @FXML
    private TextField textFieldL;
    @FXML
    private Button buttonL;
    //prava strana
    @FXML
    private ImageView imageViewTopR;
    @FXML
    private Text mestoTopR;
    @FXML
    private Text tempTopR;
    @FXML
    private Text den1R;
    @FXML
    private Text den1TempR;
    @FXML
    private Text den2R;
    @FXML
    private Text den2TempR;
    @FXML
    private Text den3R;
    @FXML
    private Text den3TempR;
    @FXML
    private TextField textFieldR;
    @FXML
    private Button buttonR;
    //center
    @FXML
    private Text dateTime;
    //other
    String apiKey = "5eb4a7c698f16ae23f98c0702ee02950";
    SQLiteJDBC sqlConnector;
    static double lat;
    static double lon;
    static conclusion[] conclusionArray;
    static String currentTempPom;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDateTime();
        sqlConnector = new SQLiteJDBC();
        buttonL.setOnAction(e -> infoGen());
        buttonR.setOnAction(e-> infoGenR());
        textFieldL.setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.ENTER) {
                infoGen();
            }

        });
        textFieldR.setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.ENTER) {
                infoGenR();
            }

        });
        dni();
        conclusionArray = new conclusion[3];
        currentTempPom = "";
        infoGenAuto();
        infoGenRAuto();
    }

    public void setDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = now.format(format);
        dateTime.setText(formatDateTime);
        Timeline t = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalDateTime now1 = LocalDateTime.now();
            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formatDateTime1 = now1.format(format1);
            dateTime.setText(formatDateTime1);
        }));
        t.setCycleCount(Animation.INDEFINITE);
        t.play();
    }

    private void downloadCurrentWeatherDataByID(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=hourly,minutely&units=metric&appid="+apiKey)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(WeatherAppController::parse)
                .join();

    }

    private void infoGen() {
        if (textFieldL.getText()!=null) {
            long mesto = sqlConnector.getIdFromName(textFieldL.getText());
            getCoordsFromId(mesto);
            downloadCurrentWeatherDataByID();
            display();
        }
    }
    private void infoGenAuto() {
        textFieldL.setText("Nitra");
            long mesto = sqlConnector.getIdFromName("Nitra");
            getCoordsFromId(mesto);
            downloadCurrentWeatherDataByID();
            display();
    }

    private void infoGenR() {
        if (textFieldR.getText()!=null) {
            long mesto = sqlConnector.getIdFromName(textFieldR.getText());
            getCoordsFromId(mesto);
            downloadCurrentWeatherDataByID();
            displayR();
        }
    }
    private void infoGenRAuto() {
        textFieldR.setText("Žiar nad Hronom");
            long mesto = sqlConnector.getIdFromName("Žiar nad Hronom");
            getCoordsFromId(mesto);
            downloadCurrentWeatherDataByID();
            displayR();
    }

    private static String parse(String responseString){
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
                JSONObject weather = (JSONObject) ((JSONArray) day.get("weather")).get(0);
                String icon = (String) weather.get("icon");

                conclusionArray[i] = new conclusion(tempDay, icon);
            }
        } catch (Exception e ) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void getCoordsFromId(long id) {
        URI uri = URI.create("https://api.openweathermap.org/data/2.5/forecast?id="+id+"&cnt=1&appid="+apiKey);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(WeatherAppController::parseCoords)
                .join();
    }

    // Parsing the two coordinates into variables
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

    public void dni() {
        den1.setText(LocalDateTime.now().getDayOfWeek().toString().toLowerCase());
        den2.setText(LocalDateTime.now().getDayOfWeek().plus(1).toString().toLowerCase());
        den3.setText(LocalDateTime.now().getDayOfWeek().plus(2).toString().toLowerCase());
        den1R.setText(LocalDateTime.now().getDayOfWeek().toString().toLowerCase());
        den2R.setText(LocalDateTime.now().getDayOfWeek().plus(1).toString().toLowerCase());
        den3R.setText(LocalDateTime.now().getDayOfWeek().plus(2).toString().toLowerCase());
    }

    private void displayHlavne(Text mestoTop, TextField textFieldL, Text tempTop, Text den1Temp, Text den2Temp, Text den3Temp) {
        try {
            mestoTop.setText(textFieldL.getText().substring(0,1).toUpperCase()+ textFieldL.getText().substring(1));
            tempTop.setText(currentTempPom+" °C");
            textFieldL.setText("");
            den1Temp.setText(conclusionArray[0].getTemp() + " °C");
            den2Temp.setText(conclusionArray[1].getTemp() + " °C");
            den3Temp.setText(conclusionArray[2].getTemp() + " °C");
            System.out.println(conclusionArray[0].getIcon());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayR() {
        displayHlavne(mestoTopR, textFieldR, tempTopR, den1TempR, den2TempR, den3TempR);
        iconChanger(conclusionArray[0].getIcon(),imageViewTopR);
    }

    public void display() {
        displayHlavne(mestoTop, textFieldL, tempTop, den1Temp, den2Temp, den3Temp);
        iconChanger(conclusionArray[0].getIcon(),imageViewTop);
    }

    public void iconChanger(String icon, ImageView imgv) {
        String icoFinal = icon+"@2x.png";
        imgv.setImage(new Image("assets\\icons\\"+icoFinal));
    }
}
