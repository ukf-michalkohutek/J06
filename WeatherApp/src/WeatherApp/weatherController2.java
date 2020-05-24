package WeatherApp;

import javafx.animation.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class weatherController2 {

    private final String APIKEY = "b81ee22f7ee3309db07b5fa363eeb6fe";

    @FXML private AnchorPane anchorPane2;
    @FXML private Label toPage1Button;

    @FXML private ImageView background;
    @FXML private Label cityLabel;
    @FXML private Label currentDateTimeLabel;

    @FXML private Label currentDayLabel;
    @FXML private Label day1DayLabel;
    @FXML private Label day2DayLabel;
    @FXML private Label day3DayLabel;

    @FXML private Label currentTempLabel;
    @FXML private ImageView currentWeatherImg;
    @FXML private Label currentWeatherLabel;

    @FXML private ImageView day1WeatherImg;
    @FXML private Label day1WeatherLabel;
    @FXML private Label day1TempLabel;

    @FXML private ImageView day2WeatherImg;
    @FXML private Label day2WeatherLabel;
    @FXML private Label day2TempLabel;

    @FXML private ImageView day3WeatherImg;
    @FXML private Label day3WeatherLabel;
    @FXML private Label day3TempLabel;

    private String[] previousCities = new String[3];
    private static Result[] results = new Result[4];
    private static String cityName;
    private static City city;
    private static double lat;
    private static double lon;
    private LocalDateTime now;
    private Timeline tl;

    private SQLiteJDBC sqlConnector;

    // Connects to the SQL database containing city ID's and names
    protected void connectDatabase() {
        sqlConnector = new SQLiteJDBC();
    }

    // Intial setup
    @FXML protected void initialize() {
        connectDatabase();
        initCityChooser();
        updatePreviousCitiesArray();
        toPage1Button.setOnMousePressed(e->loadFirstPage());

        newCitySelected(previousCities[2]);
    }

    // Initializes the "city changing" dialog box
    private void initCityChooser() {
        cityLabel.setOnMousePressed(evt->{

            ChoiceDialog<String> dialog = new ChoiceDialog<>("");
            dialog.setTitle("Changing the city...");
            dialog.setContentText("Enter your city:");
            dialog.setHeaderText(null);
            dialog.setGraphic(null);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.getDialogPane().setOnKeyPressed(Event::consume);

            ComboBox<String> comboBox = (ComboBox) (((GridPane) dialog.getDialogPane()
                    .getContent())
                    .getChildren().get(1));

            comboBox.setEditable(true);

            for (int i=2; i>=0; i--)
                comboBox.getItems().add(previousCities[i]);

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().equals("")) {
                newCitySelected(result.get());
            }
        });
    }

    // Tries to download and display information about a new city, shows an error if the city doesn't exist
    private void newCitySelected(String city) {
        cityName = formatCityName(city);
        long id = sqlConnector.getIdFromName(cityName);

        try {
            getCoordsFromId(id);

            cityLabel.setText(cityName);
            updatePreviousCitiesJSON();
            updatePreviousCitiesArray();
            downloadWeatherDataByCoords();
            startDateTimeTimer();
            chooseBackground();
            updateDisplayedData();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Oops!");
            alert.setContentText("Sorry, this city isn't in our database.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    // Puts the data from previousCities.json into an array
    private void updatePreviousCitiesArray() {
        JSONParser parser = new JSONParser();
        try {
            JSONArray previousCities = (JSONArray) parser.parse(new FileReader("previousCities2.json"));
            for (int i=0; i<3; i++)
                this.previousCities[i] = (String) previousCities.get(i);

        } catch (Exception e) { e.printStackTrace(); }
    }

    // Updates the last 3 selected cities in a JSON file
    private void updatePreviousCitiesJSON() {
        boolean isFull = true;
        boolean isAlreadyIn = false;
        JSONParser parser = new JSONParser();
        JSONArray previousCities;

        try {
            previousCities = (JSONArray) parser.parse(new FileReader("previousCities2.json"));

            for (int i=0; i<3; i++) {
                String city = (String) previousCities.get(i);

                if (city.equals("")) {
                    previousCities.set(i, cityName);
                    isFull = false;
                    break;
                }

                if (city.equals(cityName)) {
                    if (i==0) {
                        previousCities.set(0, previousCities.get(1));
                        previousCities.set(1, previousCities.get(2));
                        previousCities.set(2, cityName);
                    }
                    else if (i==1) {
                        previousCities.set(1, previousCities.get(2));
                        previousCities.set(2, cityName);
                    }
                    isAlreadyIn = true;
                    break;
                }
            }

            // If the 3 indexes are full, we delete the oldest city, shift 2 and add a new one
            if (isFull && !isAlreadyIn) {
                previousCities.set(0, previousCities.get(1));
                previousCities.set(1, previousCities.get(2));
                previousCities.set(2, cityName);
            }

            FileWriter write = new FileWriter("previousCities2.json");
            write.write(previousCities.toJSONString());
            write.flush();

        } catch (Exception e) { e.printStackTrace(); }
    }

    // Updates the FXML components with new data
    private void updateDisplayedData() {
        // Updating current weather data
        currentTempLabel.setText(results[0].getTempDay()+"°");
        currentWeatherImg.setImage(new Image(getWeatherIconPath(results[0].getIcon()),100,100,true,true));
        currentWeatherLabel.setText(results[0].getDescription());

        // Updating forecast data
        day1WeatherImg.setImage(new Image("assets/images/day/icons/"+results[1].getIcon()+".png",100,100,true,true));
        day1WeatherLabel.setText(results[1].getWeather());
        day1TempLabel.setText(results[1].getTempDay()+"° / "+results[1].getTempNight()+"°");

        day2WeatherImg.setImage(new Image("assets/images/day/icons/"+results[2].getIcon()+".png",100,100,true,true));
        day2WeatherLabel.setText(results[2].getWeather());
        day2TempLabel.setText(results[2].getTempDay()+"° / "+results[2].getTempNight()+"°");

        day3WeatherImg.setImage(new Image("assets/images/day/icons/"+results[3].getIcon()+".png",100,100,true,true));
        day3WeatherLabel.setText(results[3].getWeather());
        day3TempLabel.setText(results[3].getTempDay()+"° / "+results[3].getTempNight()+"°");
    }

    // Returns the path to a weather icon based on the icons name and time of day
    private String getWeatherIconPath(String icon) {
        String path = "assets/images";

        if (now.isAfter(city.getSunset()) || now.isBefore(city.getSunrise())) path+="/night";
        else path+="/day";

        path+="/icons/" + icon + ".png";

        return path;
    }

    // Displays the correct background based on time of day and weather condition
    private void chooseBackground() {
        String path = "assets/images";
        if (now.isAfter(city.getSunset()) || now.isBefore(city.getSunrise())) path+="/night";
        else path+="/day";

        path+="/backgrounds";
        switch (results[0].getWeather()) {
            case "Thunderstorm": path+="/thunderstorm.png"; break;
            case "Drizzle": case "Rain": path+="/rain.png"; break;
            case "Clear": path+="/clear.png"; break;
            case "Clouds": path+="/clouds.png"; break;
            default: path+="/snow.png";
        }
        background.setImage(new Image(path,400,600,true,true));
    }

    // Starts a timer that updates date, time and day of week
    private void startDateTimeTimer() {
        // Initial update of date, time and day
        now = LocalDateTime.ofEpochSecond(System.currentTimeMillis()/1000,0,city.getTimezoneOffset());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd. MM. yyyy   HH:mm:ss");
        currentDateTimeLabel.setText(now.format(format));

        String initialDay = now.getDayOfWeek().toString().toLowerCase();
        currentDayLabel.setText(initialDay.substring(0,1).toUpperCase() + initialDay.substring(1));
        day1DayLabel.setText(initialDay.substring(0,1).toUpperCase() + initialDay.substring(1));

        String day2 = now.plusDays(1).getDayOfWeek().toString().toLowerCase();
        day2DayLabel.setText(day2.substring(0,1).toUpperCase() + day2.substring(1));

        String day3 = now.plusDays(2).getDayOfWeek().toString().toLowerCase();
        day3DayLabel.setText(day3.substring(0,1).toUpperCase() + day3.substring(1));

        tl = new Timeline(new KeyFrame(Duration.seconds(1),e->{
            now = LocalDateTime.ofEpochSecond(System.currentTimeMillis()/1000,0,city.getTimezoneOffset());
            currentDateTimeLabel.setText(now.format(format));

            String currentDay = now.getDayOfWeek().toString().toLowerCase();
            currentDayLabel.setText(currentDay.substring(0,1).toUpperCase() + currentDay.substring(1));
        }));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();
    }

    // Downloading data from openweathermap One Call API
    private void downloadWeatherDataByCoords() {
        URI uri = URI.create("https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=hourly,minutely&units=metric&appid="+APIKEY);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(weatherController2::parseWeatherData)
                .join();
    }

    // Parsing the weather data into an array of results / Parsing information about the city
    private static String parseWeatherData(String responseString) {
        JSONParser parser = new JSONParser();

        try {
            JSONObject response = (JSONObject) parser.parse(responseString);
            JSONObject current = (JSONObject) response.get("current");

            // Parsing information about the city
            String name = cityName;
            long timezoneOffsetInSeconds = (long) response.get("timezone_offset");
            long sunriseEpoch = (long) current.get("sunrise");
            long sunsetEpoch = (long) current.get("sunset");

            city = new City(name, timezoneOffsetInSeconds, sunriseEpoch, sunsetEpoch);

            // Parsing the current weather information
            JSONObject weather = (JSONObject) ((JSONArray)current.get("weather")).get(0);

            int tempNight = Integer.MIN_VALUE;
            int tempDay;
            try { tempDay = (int) Math.round((double) current.get("temp")); }
            catch (Exception e) { tempDay = (int) ((long) current.get("temp")); }
            String main = (String) weather.get("main");
            String description = (String) weather.get("description");
            String icon = (String) weather.get("icon");

            results[0] = new Result(tempDay, tempNight, main, description, icon);

            // Parsing forecast for today + 2 more days
            JSONArray daily = (JSONArray) response.get("daily");

            for (int i=0; i<3; i++) {
                JSONObject day = (JSONObject) daily.get(i);
                JSONObject temp = (JSONObject) day.get("temp");

                try { tempDay = (int) Math.round((double)temp.get("day")); }
                catch (Exception e) { tempDay = (int)((long)temp.get("day")); }
                try { tempNight = (int) Math.round((double)temp.get("night")); }
                catch (Exception e) { tempNight = (int)((long)temp.get("night")); }
                weather = (JSONObject) ((JSONArray)day.get("weather")).get(0);
                main = (String) weather.get("main");
                description = (String) weather.get("description");
                icon = (String) weather.get("icon");

                results[i+1] = new Result(tempDay, tempNight, main, description, icon);
            }
        }
        catch (ParseException e) { System.out.println(e.getMessage()); }

        return null;
    }

    // Using openweathermap to grab the lat and lon coordinates of a city, from the city ID
    private void getCoordsFromId(long id) {
        URI uri = URI.create("https://api.openweathermap.org/data/2.5/forecast?id="+id+"&cnt=1&appid="+APIKEY);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(weatherController2::parseCoords)
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

    // Scrolls to the left to return to the first page
    private void loadFirstPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("weather.fxml"));
            Scene scene = toPage1Button.getScene();

            root.translateXProperty().set(-scene.getWidth());
            StackPane stackPane = (StackPane) scene.getRoot();
            stackPane.getChildren().add(root);

            Timeline sceneanim = new Timeline();
            KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_BOTH);
            KeyValue kv2 = new KeyValue(anchorPane2.translateXProperty(), scene.getWidth(), Interpolator.EASE_BOTH);
            KeyFrame kf = new KeyFrame(Duration.millis(750), kv, kv2);
            sceneanim.getKeyFrames().add(kf);
            sceneanim.setOnFinished(e-> stackPane.getChildren().remove(anchorPane2));
            sceneanim.play();

        } catch (Exception e) { e.printStackTrace(); }
    }

    // Capitalizes every word in a String
    private String formatCityName(String city) {
        StringBuilder result = new StringBuilder();
        String[] arr = city.split(" ");
        for (int i=0; i<arr.length; i++) {
            result.append(arr[i].substring(0, 1).toUpperCase()).append(arr[i].substring(1).toLowerCase());
            if (i!=arr.length-1) result.append(" ");
        }

        return result.toString();
    }
}
