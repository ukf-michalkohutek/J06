package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Main extends Application {

    Image i0, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12;
    ImageView background1, background2, sicon, sicon2, wicon, wicon2;
    TextField search1, search2;
    Button b1, b2;
    public static int weatherID = 800, weatherID2 = 800;
    Label actW, actW2, actT1, actT2, actDateTime, aCity1, aCity2, d1L, d2L, d3L, d1L2, d2L2, d3L2;
    public static double actTemp1, actTemp2, day1T, day2T, day3T, day1T2, day2T2, day3T2;
    public  static String city, city2;
    int cIndex = 3, cIndex2 = 3;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root, 800, 500);

        i0 = new Image("search.png", 20, 20, false, false);
        i1 = new Image("cloudbg.png", 400, 500, false, false);
        i2 = new Image("sunbg.png", 400, 500, false, false);
        i3 = new Image("rainbg.png", 400, 500, false, false);
        i4 = new Image("snowbg.png", 400, 500, false, false);
        i5 = new Image("stormbg.png", 400, 500, false, false);
        i6 = new Image("cloud.png", 100, 100, false, false);
        i7 = new Image("sun.png", 100, 100, false, false);
        i8 = new Image("rain.png", 100, 100, false, false);
        i9 = new Image("snow.png", 100, 100, false, false);
        i10 = new Image("storm.png", 100, 100, false, false);
        i11 = new Image("fogbg.png", 100, 100, false, false);
        i12 = new Image("fog.png", 100, 100, false, false);

        background1 = new ImageView(i1);
        background2 = new ImageView(i2);
        background1.setLayoutX(0);
        background2.setLayoutX(400);

        wicon = new ImageView();
        wicon2 = new ImageView();
        search1 = new TextField();
        search2 = new TextField();
        b1 = new Button();
        b2 = new Button();
        sicon = new ImageView(i0);
        sicon2 = new ImageView(i0);
        b1.setGraphic(sicon);
        b2.setGraphic(sicon2);
        b1.setOnAction(e ->{
            searchCity();
        });
        b2.setOnAction(e ->{
            searchCity2();
        });

        HBox sbar1 = new HBox(10);
        sbar1.getChildren().addAll(search1, b1);
        HBox sbar2 = new HBox(10);
        sbar2.getChildren().addAll(search2, b2);
        sbar1.setLayoutX(100);
        sbar1.setLayoutY(50);
        sbar2.setLayoutX(500);
        sbar2.setLayoutY(50);

        actDateTime = new Label();
        LocalDateTime myDateTime = LocalDateTime.now();
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("E, dd. M. yyyy HH:mm");
        String formattedDate = myDateTime.format(myFormat);
        actDateTime.setText(formattedDate);
        actDateTime.setLayoutX(300);
        actDateTime.setLayoutY(100);
        actDateTime.setFont(new Font("Cambria", 22));
        actDateTime.setTextFill(Color.BLACK);
        Timeline time = new Timeline(new KeyFrame(Duration.seconds(1), e -> setDateTime()));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();

        actW = new Label("-");
        actW.setTextFill(Color.BLACK);
        actW.setFont(new Font("Cambria", 30));
        actW.setLayoutX(140);
        actW.setLayoutY(260);

        actW2 = new Label("-");
        actW2.setTextFill(Color.BLACK);
        actW2.setFont(new Font("Cambria", 30));
        actW2.setLayoutX(540);
        actW2.setLayoutY(260);

        actT1 = new Label("-");
        actT1.setTextFill(Color.BLACK);
        actT1.setFont(new Font("Cambria", 25));
        actT1.setLayoutX(145);
        actT1.setLayoutY(310);

        actT2 = new Label("-");
        actT2.setTextFill(Color.BLACK);
        actT2.setFont(new Font("Cambria", 25));
        actT2.setLayoutX(545);
        actT2.setLayoutY(310);

        aCity1 = new Label();
        aCity1.setTextFill(Color.BLACK);
        aCity1.setFont(new Font("Cambria", 25));
        aCity2 = new Label();
        aCity2.setTextFill(Color.BLACK);
        aCity2.setFont(new Font("Cambria", 25));

        d1L = new Label();
        d1L.setTextFill(Color.BLACK);
        d1L.setFont(new Font("Cambria", 20));
        d2L = new Label();
        d2L.setTextFill(Color.BLACK);
        d2L.setFont(new Font("Cambria", 20));
        d3L = new Label();
        d3L.setTextFill(Color.BLACK);
        d3L.setFont(new Font("Cambria", 20));
        d1L2 = new Label();
        d1L2.setTextFill(Color.BLACK);
        d1L2.setFont(new Font("Cambria", 20));
        d2L2 = new Label();
        d2L2.setTextFill(Color.BLACK);
        d2L2.setFont(new Font("Cambria", 20));
        d3L2 = new Label();
        d3L2.setTextFill(Color.BLACK);
        d3L2.setFont(new Font("Cambria", 20));

        HBox days = new HBox(20);
        days.getChildren().addAll(d1L, d2L, d3L);
        days.setLayoutX(60);
        days.setLayoutY(400);

        HBox days2 = new HBox(20);
        days2.getChildren().addAll(d1L2, d2L2, d3L2);
        days2.setLayoutX(460);
        days2.setLayoutY(400);

        search1.setText(loadCity1());
        searchCity();
        search2.setText(loadCity2());
        searchCity2();

        root.getChildren().addAll(background1, background2, sbar1, sbar2, actDateTime, wicon, actW, wicon2, actW2, actT1, actT2, aCity1, aCity2, days, days2);
        primaryStage.setTitle("Easy Weather");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    void searchCity(){
        if(search1.getText().equals("")) search1.setText("Nitra");
        city = search1.getText();
        saveCity1(getCityName());
        try {
            searchAPI();
        } catch (Exception exception) {
            System.out.println(exception.toString() + "\nError");
        }
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime next1 = today.plusDays(1);
        LocalDateTime next2 = today.plusDays(2);
        LocalDateTime next3 = today.plusDays(3);
        DateTimeFormatter daysFormat = DateTimeFormatter.ofPattern("EEEE");
        String nDay1 = daysFormat.format(next1);
        String nDay2 = daysFormat.format(next2);
        String nDay3 = daysFormat.format(next3);
        actT1.setText((Math.round(actTemp1 * 10) / 10.0) + " °C");
        d1L.setText(nDay1 + ":\n" + (Math.round(day1T * 10) / 10.0) + " °C");
        d2L.setText(nDay2 + ":\n" + (Math.round(day2T * 10) / 10.0) + " °C");
        d3L.setText(nDay3 + ":\n" + (Math.round(day3T * 10) / 10.0) + " °C");
        setBackground();
    }

    void searchCity2(){
        if(search2.getText().equals("")) search2.setText("Nitra");
        city2 = search2.getText();
        saveCity2(getCityName2());
        try {
            searchAPI2();
        } catch (Exception exception) {
            System.out.println(exception.toString() + "\nError");
        }
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime next1 = today.plusDays(1);
        LocalDateTime next2 = today.plusDays(2);
        LocalDateTime next3 = today.plusDays(3);
        DateTimeFormatter daysFormat = DateTimeFormatter.ofPattern("EEEE");
        String nDay1 = daysFormat.format(next1);
        String nDay2 = daysFormat.format(next2);
        String nDay3 = daysFormat.format(next3);
        actT2.setText((Math.round(actTemp2 * 10) / 10.0) + " °C");
        d1L2.setText(nDay1 + ":\n" + (Math.round(day1T2 * 10) / 10.0) + " °C");
        d2L2.setText(nDay2 + ":\n" + (Math.round(day2T2 * 10) / 10.0) + " °C");
        d3L2.setText(nDay3 + ":\n" + (Math.round(day3T2 * 10) / 10.0) + " °C");
        setBackground2();
    }

    public static void searchAPI() throws Exception {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+ city +"&appid=1324e31499d2260f4c0e45178f1b591a&lang=sk&units=metric";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String input;
        StringBuffer res = new StringBuffer();
        while ((input = br.readLine()) != null) {
            res.append(input);
        }
        br.close();
        String ans = res.toString();

        JSONObject output = new JSONObject(ans);
        JSONObject list1 = output.getJSONArray("list").getJSONObject(0);
        long dt1 = list1.getLong("dt");
        JSONObject list2 = output.getJSONArray("list").getJSONObject(8);
        long dt2 = list2.getLong("dt");
        JSONObject list3 = output.getJSONArray("list").getJSONObject(16);
        long dt3 = list3.getLong("dt");
        JSONObject list4 = output.getJSONArray("list").getJSONObject(24);
        long dt4 = list4.getLong("dt");

        String temp1[] = ans.split("\"dt\":" + dt1 + ",\"main\":\\{\"temp\":");
        String temp2[] = temp1[1].split(",\"feels_like");
        String temp3[] = ans.split("\"dt\":" + dt2 + ",\"main\":\\{\"temp\":");
        String temp4[] = temp3[1].split(",\"feels_like\"");
        String temp5[] = ans.split("\"dt\":" + dt3 + ",\"main\":\\{\"temp\":");
        String temp6[] = temp5[1].split(",\"feels_like\"");
        String temp7[] = ans.split("\"dt\":" + dt4 + ",\"main\":\\{\"temp\":");
        String temp8[] = temp7[1].split(",\"feels_like\"");
        String temp9[] = ans.split("\"weather\":\\[\\{\"id\":");
        String temp10[] = temp9[1].split(",\"main\":");


        String tempeture = temp2[0];
        String te1 = temp4[0];
        String te2 = temp6[0];
        String te3 = temp8[0];
        String wthr = temp10[0];

        actTemp1 = Double.valueOf(tempeture);
        day1T = Double.valueOf(te1);
        day2T = Double.valueOf(te2);
        day3T = Double.valueOf(te3);
        weatherID = Integer.valueOf(wthr);
    }

    public static void searchAPI2() throws Exception {
        //String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=2b753eec567dbf89c62d6b1dd4878c7a&units=metric";
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+ city2 +"&appid=1324e31499d2260f4c0e45178f1b591a&lang=sk&units=metric";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String input;
        StringBuffer res = new StringBuffer();
        while ((input = br.readLine()) != null) {
            res.append(input);
        }
        br.close();
        String ans = res.toString();

        JSONObject output = new JSONObject(ans);
        JSONObject list1 = output.getJSONArray("list").getJSONObject(0);
        long dt1 = list1.getLong("dt");
        JSONObject list2 = output.getJSONArray("list").getJSONObject(8);
        long dt2 = list2.getLong("dt");
        JSONObject list3 = output.getJSONArray("list").getJSONObject(16);
        long dt3 = list3.getLong("dt");
        JSONObject list4 = output.getJSONArray("list").getJSONObject(24);
        long dt4 = list4.getLong("dt");

        String temp1[] = ans.split("\"dt\":" + dt1 + ",\"main\":\\{\"temp\":");
        String temp2[] = temp1[1].split(",\"feels_like");
        String temp3[] = ans.split("\"dt\":" + dt2 + ",\"main\":\\{\"temp\":");
        String temp4[] = temp3[1].split(",\"feels_like\"");
        String temp5[] = ans.split("\"dt\":" + dt3 + ",\"main\":\\{\"temp\":");
        String temp6[] = temp5[1].split(",\"feels_like\"");
        String temp7[] = ans.split("\"dt\":" + dt4 + ",\"main\":\\{\"temp\":");
        String temp8[] = temp7[1].split(",\"feels_like\"");
        String temp9[] = ans.split("\"weather\":\\[\\{\"id\":");
        String temp10[] = temp9[1].split(",\"main\":");

        String tempeture = temp2[0];
        String te1 = temp4[0];
        String te2 = temp6[0];
        String te3 = temp8[0];
        String wthr = temp10[0];

        actTemp2 = Double.valueOf(tempeture);
        day1T2 = Double.valueOf(te1);
        day2T2 = Double.valueOf(te2);
        day3T2 = Double.valueOf(te3);
        weatherID2 = Integer.valueOf(wthr);
    }

    public void setBackground(){
        String cit1 = search1.getText();
        search1.clear();
        aCity1.setText(cit1);
        aCity1.setLayoutX(130);
        aCity1.setLayoutY(120);
        if(weatherID >= 200 && weatherID <= 232){
            background1.setImage(i5);
            wicon.setImage(i10);
            wicon.setLayoutX(130);
            wicon.setLayoutY(150);
            actW.setText("Búrka");
        }
        else if(weatherID >= 300 && weatherID <= 531){
            background1.setImage(i3);
            wicon.setImage(i8);
            wicon.setLayoutX(130);
            wicon.setLayoutY(150);
            actW.setText("Dážď");
        }
        else if(weatherID >= 600 && weatherID <= 622){
            background1.setImage(i4);
            wicon.setImage(i9);
            wicon.setLayoutX(130);
            wicon.setLayoutY(150);
            actW.setText("Sneh");
        }
        else if(weatherID >= 701 && weatherID <= 781){
            background1.setImage(i11);
            wicon.setImage(i12);
            wicon.setLayoutX(130);
            wicon.setLayoutY(150);
            actW.setText("Hmla");
        }
        else if(weatherID == 800){
            background1.setImage(i2);
            wicon.setImage(i7);
            wicon.setLayoutX(130);
            wicon.setLayoutY(150);
            actW.setText("Jasno");
        }
        else if(weatherID >= 801 && weatherID <= 804) {
            background1.setImage(i1);
            wicon.setImage(i6);
            wicon.setLayoutX(130);
            wicon.setLayoutY(150);
            actW.setText("Oblačno");
        }
    }

    public void setBackground2(){
        String cit2 = search2.getText();
        search2.clear();
        aCity2.setText(cit2);
        aCity2.setLayoutX(530);
        aCity2.setLayoutY(120);
        if(weatherID2 >= 200 && weatherID2 <= 232){
            background2.setImage(i5);
            wicon2.setImage(i10);
            wicon2.setLayoutX(530);
            wicon2.setLayoutY(150);
            actW2.setText("Búrka");
        }
        else if(weatherID2 >= 300 && weatherID2 <= 531){
            background2.setImage(i3);
            wicon2.setImage(i8);
            wicon2.setLayoutX(530);
            wicon2.setLayoutY(150);
            actW2.setText("Dážď");
        }
        else if(weatherID2 >= 600 && weatherID2 <= 622){
            background2 = new ImageView(i4);
            wicon2.setImage(i9);
            wicon2.setLayoutX(530);
            wicon2.setLayoutY(150);
            actW2.setText("Sneh");
        }
        else if(weatherID2 >= 701 && weatherID2 <= 781){
            background2.setImage(i11);
            wicon2.setImage(i12);
            wicon2.setLayoutX(130);
            wicon2.setLayoutY(150);
            actW2.setText("Hmla");
        }
        else if(weatherID2 == 800){
            background2.setImage(i2);
            wicon2.setImage(i7);
            wicon2.setLayoutX(530);
            wicon2.setLayoutY(150);
            actW2.setText("Jasno");
        }
        else if(weatherID2 >= 801 && weatherID2 <= 804) {
            background2.setImage(i1);
            wicon2.setImage(i6);
            wicon2.setLayoutX(530);
            wicon2.setLayoutY(150);
            actW2.setText("Oblačno");
        }
    }

    public void setDateTime(){
        LocalDateTime myDateTime = LocalDateTime.now();
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("E, dd. M. yyyy HH:mm");
        String formattedDate = myDateTime.format(myFormat);
        actDateTime.setText(formattedDate);
        actDateTime.setLayoutX(300);
        actDateTime.setLayoutY(100);
        actDateTime.setFont(new Font("Cambria", 22));
        actDateTime.setTextFill(Color.BLACK);
    }

    public void saveCity1(String cityName){
        JSONObject cities = new JSONObject();
        cities.put("city", cityName);
        cIndex--;
        if(cIndex == 0) cIndex = 3;
        try(FileWriter fw = new FileWriter("List1.json")) {
            fw.write(cities.toString());
            fw.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void saveCity2(String cityName){
        JSONObject cities = new JSONObject();
        cities.put("city", cityName);
        cIndex2--;
        if(cIndex2 == 0) cIndex2 = 3;
        try(FileWriter fw = new FileWriter("List2.json")) {
            fw.write(cities.toString());
            fw.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String loadCity1(){
        JSONParser par = new JSONParser();
        try{
            Object obj = par.parse(new FileReader("List1.json"));
            org.json.simple.JSONObject JO = (org.json.simple.JSONObject) obj;
            String cit = (String) JO.get("city");
            return cit;
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        return "Error";
    }

    public String loadCity2(){
        JSONParser par = new JSONParser();
        try{
            Object obj = par.parse(new FileReader("List2.json"));
            org.json.simple.JSONObject JO = (org.json.simple.JSONObject) obj;
            String cit = (String) JO.get("city");
            return cit;
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        return "Error";
    }

    public String getCityName(){
        return search1.getText();
    }

    public String getCityName2(){
        return search2.getText();
    }

    public static void main(String[] args) {
        launch(args);
    }
}