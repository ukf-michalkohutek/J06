package sample;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

public class GetWeather {

    private double day0, day1, day2;
    private int w, w1, w2, x, y;
    private LocalDateTime today = LocalDateTime.now();

    public GetWeather(String city) throws Exception {
        getWeather(city);
    }

    public double getDay0() {
        return Math.round(((day0 - 273) * 10)/ 10.0);
    }

    public double getDay1() {
        return Math.round(((day1 - 273) * 10)/ 10.0);
    }

    public double getDay2() {
        return Math.round(((day2 - 273) * 10)/ 10.0);
    }

    public int getW() {
        return w;
    }

    public int getW1() {
        return w1;
    }

    public int getW2() {
        return w2;
    }

    public void getWeather(String city) throws Exception {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=e1f4b1e7471f2ea27d393669a3c1d31a";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();

        System.out.println("\nSending GET request to URL : " + url);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String longtetxt = response.toString();

        System.out.println(today);

        String findday [] = String.valueOf(today).split("T");
        String string [] = findday[1].split(":");

        int i = Integer.valueOf(string[0]);


        if (i >= 0 && i < 3){
            x = 8 + 5;
            y = x + 8;
        } else if (i >= 3 && i < 6){
            x = 8 + 4;
            y = x + 8;
        } else if (i >= 6 && i < 9){
            x = 8 + 3;
            y = x + 8;
        } else if (i >= 9 && i < 12){
            x = 8 + 2;
            y = x + 8;
        } else if (i >= 12 && i < 15){
            x = 8 + 1;
            y = x + 8;
        } else if (i >= 15 && i < 18){
            x = 8;
            y = x + 8;
        } else if (i >= 18 && i < 21){
            x = 7;
            y = x + 8;
        } else if (i >= 21 && i < 24){
            x = 6;
            y = x + 8;
        }

        JSONObject outputjson = new JSONObject(longtetxt);
        JSONObject list = outputjson.getJSONArray("list").getJSONObject(0);
        long dt1 = list.getLong("dt");

        JSONObject list1 = outputjson.getJSONArray("list").getJSONObject(x);
        long dt2 = list1.getLong("dt");

        JSONObject list2 = outputjson.getJSONArray("list").getJSONObject(y);
        long dt3 = list2.getLong("dt");

        String temp1 [] = longtetxt.split("\"dt\":" + dt1 + ",\"main\":\\{\"temp\":");
        String temp11 [] = temp1[1].split(",\"feels_like\":");
        String temp12 [] = temp11[1].split(",\"temp_min\"");
        String temp13 [] = temp12[1].split("\"weather\":\\[\\{\"id\":");
        String temp14 [] = temp13[1].split(",\"main\":");

        String temp2 [] = longtetxt.split("\"dt\":" + dt2 + ",\"main\":\\{\"temp\":");
        String temp21 [] = temp2[1].split(",\"feels_like\"");
        String temp22 [] = temp21[1].split("\"weather\":\\[\\{\"id\":");
        String temp23 [] = temp22[1].split(",\"main\":");

        String temp3 [] = longtetxt.split("\"dt\":" + dt3 + ",\"main\":\\{\"temp\":");
        String temp31 [] = temp3[1].split(",\"feels_like\"");
        String temp32 [] = temp31[1].split("\"weather\":\\[\\{\"id\":");
        String temp33 [] = temp32[1].split(",\"main\":");


        String tempnow = temp11[0];
        String tempday1 = temp21[0];
        String tempday2 = temp31[0];
        String weather = temp14[0];
        String weather1 = temp23[0];
        String weather2 = temp33[0];

        day0 = Double.valueOf(tempnow);
        day1 = Double.valueOf(tempday1);
        day2 = Double.valueOf(tempday2);
        w = Integer.valueOf(weather);
        w1 = Integer.valueOf(weather1);
        w2 = Integer.valueOf(weather2);
    }
}
