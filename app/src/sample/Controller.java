package sample;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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
    SQLiteJDBC sqlConnector;
    static String compressedCity="",hex="#ffffff";

    // Labels
    @FXML private GridPane grid;
    @FXML private Label fcity;
    @FXML private Label fcfd;
    @FXML private Label fcsd;
    @FXML private Label fctd;
    @FXML private Label scity;
    @FXML private Label scfd;
    @FXML private Label scsd;
    @FXML private Label sctd;
    @FXML private TextField fi;
    @FXML private TextField si;

    protected void connectDatabase() {
        sqlConnector = new SQLiteJDBC();
    }

    @FXML
    protected void initialize() throws SQLException {
        connectDatabase();

        boolean created = false;

        fcity.setText("No City");
        fcfd.setText("");
        fcsd.setText("");
        fctd.setText("");
        scity.setText("No City");
        scfd.setText("");
        scsd.setText("");
        sctd.setText("");

        File file = new File("last.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String l = br.readLine();
            if(!l.equals("NONE")){
                String[] ar = l.split(";");
                fcity.setText(ar[0]);
                fcfd.setText(ar[1]);
                fcsd.setText(ar[2]);
                fctd.setText(ar[3]);
            }
            l = br.readLine();
            if(!l.equals("NONE")){
                String[] ar = l.split(";");
                scity.setText(ar[0]);
                scfd.setText(ar[1]);
                scsd.setText(ar[2]);
                sctd.setText(ar[3]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        String f = "";
        String s = "";
        String t = "";
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

        String date1 = da1.toString().substring(0, 10);
        f = date1 + " - " + de1 + " - Teplota: " + temperature1;
        String c = "#ffee30";
        if(de1.equals("clear sky")) c="#ffffff";
        if(de1.contains("clouds")) c="#80aeb0";
        if(de1.contains("rain")) c="#9baaab";
        hex=c;

        Object da2 = dt2.get("dt_txt");
        JSONObject main2 = dt2.getJSONObject("main");
        Object temperature2 = main2.get("temp");

        // Teplota pre druhy den + farba textu

        String date2 = da2.toString().substring(0, 10);
        s = date2  + " - " + de2 + " - Teplota: " + temperature2;

        Object da3 = dt3.get("dt_txt");
        JSONObject main3 = dt3.getJSONObject("main");
        Object temperature3 = main3.get("temp");

        // Teplota pre treti den + farba textu

        String date3 = da3.toString().substring(0, 10);
        t = date3 + " - " + de3 + " - Teplota: " + temperature3;

        compressedCity = f+";"+s+";"+t;
        return null;
    }

    public void setFirst(ActionEvent event){
        String input = this.fi.getText();

        try {
            downloadCurrentWeatherDataByID(sqlConnector.getIdFromName(input));
            String content = input + ";" + compressedCity;
            if(content.equals(input+";")) content="NONE";

            BufferedReader br = new BufferedReader(new FileReader(new File("last.txt")));
            String other = br.readLine(); other = br.readLine();
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("last.txt")));
            bw.write(content);
            bw.newLine();
            bw.write(other);
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        String[] ar = compressedCity.split(";");
        if(ar.length!=3) ar = new String[]{"","",""};
        fcity.setText(input);
        fcfd.setText(ar[0]);
        fcsd.setText(ar[1]);
        fctd.setText(ar[2]);
        fi.setText("");
        compressedCity="";
        setGrid(hex);
    }

    public void setSecond(ActionEvent event){
        String input = this.si.getText();

        try {
            downloadCurrentWeatherDataByID(sqlConnector.getIdFromName(input));
            String content = input + ";" + compressedCity;
            if(content.equals(input+";")) content="NONE";

            BufferedReader br = new BufferedReader(new FileReader(new File("last.txt")));
            String other = br.readLine();
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("last.txt")));
            bw.write(other);
            bw.newLine();
            bw.write(content);
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        String[] ar = compressedCity.split(";");
        if(ar.length!=3) ar = new String[]{"","",""};
        scity.setText(input);
        scfd.setText(ar[0]);
        scsd.setText(ar[1]);
        sctd.setText(ar[2]);
        si.setText("");
        compressedCity="";
        setGrid(hex);
    }

    private void setGrid(String c){
        grid.setStyle("-fx-background-color: "+c);
    }

}
