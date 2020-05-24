package sk.uher.weather;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;

public class Controller {
    private final String APIKey = "5002fd410cb289dcdfe844fe50f2649f";

    @FXML
    private TextField citySearchField;
    @FXML
    private TextField citySearchField2;
    @FXML
    private Label dateLabel;
    @FXML
    private TableView<Weather> tableView;
    @FXML
    private TableView<Weather> tableView2;
    @FXML
    SQLiteJDBC connector;

    private CitySupplier citySupplier;
    private CityService cityService;
    private WeatherSupplier weatherSupplier;
    private WeatherService weatherService;
    private TableView focusedTable;

    public Controller(CityProvider citySupplier) {
        this.citySupplier = citySupplier;
        cityService = new CityService();
        weatherService = new WeatherService();
    }

    @FXML
    protected void initialize() {

    }

    public void onSearch() {
        if (this.citySearchField.isFocused()) {
            cityService.cityName = citySearchField.getText();
            focusedTable = tableView;
            cityService.restart();
        } else if (this.citySearchField2.isFocused()) {
            cityService.cityName = citySearchField2.getText();
            focusedTable = tableView2;
            cityService.restart();
        }
    }

    static LinkedList<Weather> weatherLinkedList = new LinkedList<>();

    private void findResults(City city) {
        String uri = buildURI(city.getId());
        downloadWeather(uri, city);
    }

    private String buildURI(int cityId) {
        String uri = "http://api.openweathermap.org/data/2.5/forecast?id=" + cityId + "&units=metric&appid=" + APIKey;
        return uri;
    }

    private void downloadWeather(String uri, City city) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(t -> jsonParse(t, city))
                .join();

        fillFocusedTable(weatherLinkedList);
    }

    void fillFocusedTable(LinkedList<Weather> weathers) {
        focusedTable.getItems().clear();
        ObservableList<Weather> data = focusedTable.getItems();

        for (Weather weather : weathers) {
            data.add(weather);
        }

        changeBgColor(focusedTable);
    }

    private void changeBgColor(TableView focusedTable) {
        String bgColor = "-fx-border-width: 5; -fx-border-color: ";
        ObservableList<Weather> data = focusedTable.getItems();
        if (data.get(0).getWeather().contains("clouds")) {
            bgColor += "#828282";
        } else if (data.get(0).getWeather().contains("drizzle")) {
            bgColor += "#99adc4";
        } else if (data.get(0).getWeather().contains("rain")) {
            bgColor += "#0061d1";
        } else if (data.get(0).getWeather().contains("storm")) {
            bgColor += "#0c3666";
        } else if (data.get(0).getWeather().contains("snow")) {
            bgColor += "#ffffff";
        } else if (data.get(0).getWeather().contains("mist")) {
            bgColor += "#c2c2c2";
        } else if (data.get(0).getWeather().contains("clear")) {
            bgColor += "#4fcdff";
        } else if (data.get(0).getWeather().contains("sun")) {
            bgColor += "#ffff00";
        }

        focusedTable.setStyle(bgColor);
    }

    private static String jsonParse(String responseString, City city) {
        JSONObject result = new JSONObject(responseString);
        JSONArray resultDates = result.getJSONArray("list");
        weatherLinkedList.clear();

        for (int i = 0; i < 24; i += 8) {
            String description = resultDates.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
            Double temperature = resultDates.getJSONObject(i).getJSONObject("main").getDouble("temp");
            String date = resultDates.getJSONObject(i).getString("dt_txt");

            weatherLinkedList.add(new Weather(city.getId(), city.getCityName(), temperature, date, description));
        }

        return null;
    }

    private class CityService extends Service<City> {

        private String cityName;

        @Override
        protected Task<City> createTask() {
            return new Task<City>() {
                @Override
                protected City call() throws Exception {
                    return citySupplier.get(cityName);
                }

                @Override
                protected void succeeded() {
                    City city = getValue();
                    dateLabel.setText(city.getId() + " " + city.getCityName());
                    findResults(city);
                }

                @Override
                protected void failed() {
                    Throwable err = getException();

                    dateLabel.setText("Error: " + err);
                }
            };
        }
    }

    private class WeatherService extends Service<Weather> {

        private String cityName;

        @Override
        protected Task<Weather> createTask() {
            return new Task<Weather>() {
                @Override
                protected Weather call() throws Exception {
                    return weatherSupplier.get(cityName);
                }

                @Override
                protected void succeeded() {
                    Weather weather = getValue();
                    dateLabel.setText(weather.getId() + " " + weather.getCityName() + " " + weather.getTemp());
                }

                @Override
                protected void failed() {
                    Throwable err = getException();

                    dateLabel.setText("Error: " + err);
                }
            };
        }
    }
}
