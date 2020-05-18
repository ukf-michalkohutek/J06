package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

public class Controller {
    @FXML private ImageView image;
    @FXML private ImageView image2;
    @FXML private TableView<Result> tableView;
    @FXML private TableView<Result> tableView2;
    @FXML private TextField cityField;
    @FXML private TextField cityField2;
    @FXML private Label toDate;
    SQLiteJDBC sqlConnector;
    static String picture;
    static String picture2;
    static HashMap<Integer, Result> resultHashMap = new HashMap<>();
    static HashMap<Integer, Result> resultHashMap2 = new HashMap<>();
    static JSONArray[] resultList = new JSONArray[3];

    protected void connectDatabase(){sqlConnector = new SQLiteJDBC();}

    @FXML
    protected void initialize(){
        resultList[0] = new JSONArray("[]");
        resultList[1] = new JSONArray("[]");
        resultList[2] = new JSONArray("[]");
        connectDatabase();
        String s = "";
        try {
            FileReader ff = new FileReader("weather.json");
            BufferedReader bR = new BufferedReader (ff);
            s = bR.readLine();
            bR.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        JSONArray results = new JSONArray(s);
        JSONArray list = results.getJSONObject(0).getJSONArray("0");
        JSONObject city = list.getJSONObject(0).getJSONObject("city");
        String town = city.getString("name");
        downloadCurrentWeatherDataByID(sqlConnector.getIdFromName(town));
        downloadCurrentWeatherDataByID2(sqlConnector.getIdFromName("Nitra"));
    }

    private void downloadCurrentWeatherDataByID(long id) {
        HttpClient client = HttpClient.newHttpClient();
        String APIKEY = "8fbf9ab78f11485197cb76edced00b44";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openweathermap.org/data/2.5/forecast?id="+id+"&appid="+ APIKEY)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Controller::parseCurrentWeather)
                .join();
        resultHashMap.keySet().stream().map(date -> resultHashMap.get(date)).forEach(this::addResult);
        toDate.setText("Last update: " + LocalDateTime.now().toString().split("\\.")[0].replace('T',' '));
        try {
            image.setImage(new Image(picture+".jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadCurrentWeatherDataByID2(long id) {
        HttpClient client = HttpClient.newHttpClient();
        String APIKEY = "8fbf9ab78f11485197cb76edced00b44";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openweathermap.org/data/2.5/forecast?id="+id+"&appid="+ APIKEY)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Controller::parseCurrentWeather2)
                .join();
        resultHashMap2.keySet().stream().map(date -> resultHashMap2.get(date)).forEach(this::addResult2);
        toDate.setText("Last update: " + LocalDateTime.now().toString().split("\\.")[0].replace('T',' '));
        try {
            image2.setImage(new Image(picture2+".jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void updateCity(){
        tableView.getItems().clear();
        resultHashMap.clear();
        String country = cityField.getText();
        downloadCurrentWeatherDataByID(sqlConnector.getIdFromName(country));
    }

    @FXML
    protected void updateCity2(){
        tableView2.getItems().clear();
        resultHashMap2.clear();
        String country = cityField2.getText();
        downloadCurrentWeatherDataByID2(sqlConnector.getIdFromName(country));
    }

    private static String parseCurrentWeather(String response) {
        JSONArray results = new JSONArray("["+response+"]");
        resultList[2] = resultList[1];
        resultList[1] = resultList[0];
        resultList[0] = results;
        try (FileWriter file = new FileWriter("weather.json")) {
            file.write("[{\"0\":"+resultList[0] + ",\"1\":" + resultList[1] + ",\"2\":" + resultList[2]+"}]");
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray list = results.getJSONObject(0).getJSONArray("list");
        int i = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        while (true){
            try {
                date = df.parse(list.getJSONObject(i).getString("dt_txt").split(" ")[0]);
                if (!(df.format(date).equals(LocalDate.now().toString()) || df.format(date).equals(LocalDate.now().plusDays(1).toString()) || df.format(date).equals(LocalDate.now().plusDays(2).toString()))) break;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dt_txt = list.getJSONObject(i).getString("dt_txt");
            String temp = (int)(list.getJSONObject(i).getJSONObject("main").getDouble("temp")-272.15) + "°C";
            String feels_like = (int)(list.getJSONObject(i).getJSONObject("main").getDouble("feels_like")-272.15) + "°C";
            String temp_min = (int)(list.getJSONObject(i).getJSONObject("main").getDouble("temp_min")-272.15) + "°C";
            String temp_max = (int)(list.getJSONObject(i).getJSONObject("main").getDouble("temp_max")-272.15) + "°C";
            String pressure = list.getJSONObject(i).getJSONObject("main").getDouble("pressure")+"kPa";
            String humidity = list.getJSONObject(i).getJSONObject("main").getDouble("humidity")+"%";
            JSONArray list2 = list.getJSONObject(i).getJSONArray("weather");
            if (i == 0) picture = list2.getJSONObject(0).getString("main");
            resultHashMap.put(i, new Result(dt_txt, temp, feels_like, temp_min, temp_max, pressure, humidity));
            i++;
        }
        return null;
    }

    private static String parseCurrentWeather2(String response) {
        JSONArray results = new JSONArray("["+response+"]");
        resultList[2] = resultList[1];
        resultList[1] = resultList[0];
        resultList[0] = results;
        try (FileWriter file = new FileWriter("weather.json")) {
            file.write("[{\"0\":"+resultList[0] + ",\"1\":" + resultList[1] + ",\"2\":" + resultList[2]+"}]");
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray list = results.getJSONObject(0).getJSONArray("list");
        int i = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        while (true){
            try {
                date = df.parse(list.getJSONObject(i).getString("dt_txt").split(" ")[0]);
                if (!(df.format(date).equals(LocalDate.now().toString()) || df.format(date).equals(LocalDate.now().plusDays(1).toString()) || df.format(date).equals(LocalDate.now().plusDays(2).toString()))) break;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dt_txt = list.getJSONObject(i).getString("dt_txt");
            String temp = (int)(list.getJSONObject(i).getJSONObject("main").getDouble("temp")-272.15) + "°C";
            String feels_like = (int)(list.getJSONObject(i).getJSONObject("main").getDouble("feels_like")-272.15) + "°C";
            String temp_min = (int)(list.getJSONObject(i).getJSONObject("main").getDouble("temp_min")-272.15) + "°C";
            String temp_max = (int)(list.getJSONObject(i).getJSONObject("main").getDouble("temp_max")-272.15) + "°C";
            String pressure = list.getJSONObject(i).getJSONObject("main").getDouble("pressure")+"kPa";
            String humidity = list.getJSONObject(i).getJSONObject("main").getDouble("humidity")+"%";
            JSONArray list2 = list.getJSONObject(i).getJSONArray("weather");
            if (i == 0) picture2 = list2.getJSONObject(0).getString("main");
            resultHashMap2.put(i, new Result(dt_txt, temp, feels_like, temp_min, temp_max, pressure, humidity));
            i++;
        }
        return null;
    }

    void addResult(Result res) {
        ObservableList<Result> data = tableView.getItems();
        data.add(res);
    }

    void addResult2(Result res) {
        ObservableList<Result> data = tableView2.getItems();
        data.add(res);
    }
}
