package WeatherApp;

import javafx.fxml.FXML;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class Controller {

    JSONArray citiesArray;

    @FXML
    protected void initialize() {
        try {
            parseCities();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void parseCities() throws Exception {
        String citiesFileName = "src/city.list.json";
        citiesArray = (JSONArray) readJsonFromFile(citiesFileName);
    }

    private static Object readJsonFromFile(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }

    public void genData() {



    }
}