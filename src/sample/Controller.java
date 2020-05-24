package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class Controller {
    public TextField city;
    public Button show;
    public ImageView image;
    public Text temperature;
    public Text description;
    public ComboBox cities;

    @FXML
    protected void initialize()
    {
       try {
           parseCities();
       } catch (Exception e){System.out.println(e.getMessage());}
    }
    private void parseCities() throws Exception {
        String citiesFileName = "src/sample/city.list.json";
        JSONArray citiesArray = (JSONArray) readJsonFromFile(citiesFileName);
        System.out.println(citiesArray.get(0));
    }

    @FXML
    void btnAction()
    {

    }


    private static Object readJsonFromFile(String filename) throws Exception
    {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }
}
