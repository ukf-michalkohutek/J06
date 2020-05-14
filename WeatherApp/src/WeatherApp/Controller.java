package WeatherApp;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class Controller {

    @FXML private TextField polePreID;

    JSONArray citiesArray;

    @FXML
    protected void initialize() {
        try {
            parseCities();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        testy();
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

    public void testy() {
        polePreID.setPromptText("Testy");
    }
}