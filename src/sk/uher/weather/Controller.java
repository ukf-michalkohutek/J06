package sk.uher.weather;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class Controller {
    @FXML private TextField citySearchField;
    @FXML private TextField citySearchField2;
    @FXML private Label dateLabel;
    @FXML private TableView<Weather> tableView;
    @FXML private TableView<Weather> tableView2;
    @FXML SQLiteJDBC connector;

    private CitySupplier citySupplier;
    private CityService cityService;
    private WeatherSupplier weatherSupplier;
    private WeatherService weatherService;

    public Controller(CityProvider citySupplier) {
        this.citySupplier = citySupplier;
        cityService = new CityService();
        weatherService = new WeatherService();
    }

    @FXML protected void initialize() {
//        try {
//            parseCities();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void onSearch() {
        cityService.cityName = citySearchField.getText();
        cityService.restart();
//        weatherService.cityName = citySearchField.getText();
//        weatherService.restart();
    }

    private void parseCities() throws Exception {
        String citiesFileName = "src/sample/city.list.json";
        JSONArray citiesArray = (JSONArray) readJsonFromFile(citiesFileName);
        System.out.println(citiesArray.get(0));
    }

    private static Object readJsonFromFile(String citiesFileName) throws Exception {
        FileReader reader = new FileReader(citiesFileName);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
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
