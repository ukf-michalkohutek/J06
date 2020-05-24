package sample;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.Engine.Core.GameObject;
import sample.Engine.TextObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class APIHandler extends GameObject {

    private String apiKey;
    private EffectHandler effectHandler;
    private LocationTextField locationTextField;

    private TextObject cityTextObject;
    private TextObject temperatureTextObject;
    private TextObject weatherTextObject;

    private TextObject day1TemperatureTextObject;
    private TextObject day2TemperatureTextObject;
    private TextObject day3TemperatureTextObject;

    private static Weather currentWeather;
    private static Weather[] forecastedWeather;

    public APIHandler(
            String _apiKey, EffectHandler _effectHandler, LocationTextField _locationTextField,
            TextObject _cityTextObject, TextObject _temperatureTextObject, TextObject _weatherTextObject,
            TextObject _day1TemperatureTextObject, TextObject _day2TemperatureTextObject ,TextObject _day3TemperatureTextObject
            )
    {
        super();

        apiKey = _apiKey;
        effectHandler = _effectHandler;
        locationTextField = _locationTextField;

        cityTextObject = _cityTextObject;
        temperatureTextObject = _temperatureTextObject;
        weatherTextObject = _weatherTextObject;

        day1TemperatureTextObject = _day1TemperatureTextObject;
        day2TemperatureTextObject = _day2TemperatureTextObject;
        day3TemperatureTextObject = _day3TemperatureTextObject;
    }

    public void requestWeatherInfo()
    {
        String cityName = locationTextField.getText().replaceAll(" ", "%20");
        String apiURL = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey;

        if (cityName.length() < 1)
        {
            return;
        }

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiURL)).build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(APIHandler::processJsonCurrentWeather)
                    .join();

            apiURL = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&cnt=4&appid=" + apiKey;

            request = HttpRequest.newBuilder().uri(URI.create(apiURL)).build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(APIHandler::processJsonForecastedWeather)
                    .join();
        } catch (Exception e) { temperatureTextObject.setText(""); weatherTextObject.setText("No Connection!"); }

        if (currentWeather == null || forecastedWeather == null)
        {
            return;
        }

        cityTextObject.setText(currentWeather.cityName);
        temperatureTextObject.setText(String.format("%.1f", currentWeather.temperatureCelsius) + "°C");
        weatherTextObject.setText(currentWeather.weatherType);

        LocalDate localDate = LocalDate.now();
        day1TemperatureTextObject.setText("Tomorrow\n" + String.format("%.1f", forecastedWeather[0].temperatureCelsius) + "°C");
        localDate = localDate.plusDays(2);
        day2TemperatureTextObject.setText(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH).format(localDate) + "\n" + String.format("%.1f", forecastedWeather[1].temperatureCelsius) + "°C");
        localDate = localDate.plusDays(1);
        day3TemperatureTextObject.setText(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH).format(localDate) + "\n" + String.format("%.1f", forecastedWeather[2].temperatureCelsius) + "°C");

        switch (currentWeather.weatherType)
        {
            case "Thunderstorm":
                effectHandler.changeEffect(EffectHandler.EffectType.THUNDERSTORM);
                break;

            case "Drizzle":
            case "Rain":
                effectHandler.changeEffect(EffectHandler.EffectType.RAIN);
                break;

            case "Snow":
                effectHandler.changeEffect(EffectHandler.EffectType.SNOW);
                break;

            case "Atmosphere":
                effectHandler.changeEffect(EffectHandler.EffectType.FOG);
                break;

            case "Clear":
                effectHandler.changeEffect(EffectHandler.EffectType.CLEAR);
                break;

            case "Clouds":
                effectHandler.changeEffect(EffectHandler.EffectType.CLOUDS);
                break;
        }
    }

    private static String processJsonCurrentWeather(String response)
    {
        currentWeather = null;

        if (response.equalsIgnoreCase("{\"cod\":\"404\",\"message\":\"city not found\"}"))
        {
            return null;
        }

        JSONObject cityInfo = new JSONObject(response);

        String cityName = cityInfo.getString("name");
        String weatherType = cityInfo.getJSONArray("weather").getJSONObject(0).getString("main");
        double temperatureCelsius = cityInfo.getJSONObject("main").getDouble("temp") - 273.15;

        currentWeather = new Weather(cityName, weatherType, temperatureCelsius);

        return null;
    }

    private static String processJsonForecastedWeather(String response)
    {
        forecastedWeather = null;

        if (response.equalsIgnoreCase("{\"cod\":\"404\",\"message\":\"city not found\"}"))
        {
            return null;
        }

        JSONObject cityInfo = new JSONObject(response);
        JSONArray list = cityInfo.getJSONArray("list");

        forecastedWeather = new Weather[3];
        forecastedWeather[0] = new Weather(list.getJSONObject(1).getJSONObject("main").getDouble("temp") - 273.15);
        forecastedWeather[1] = new Weather(list.getJSONObject(2).getJSONObject("main").getDouble("temp") - 273.15);
        forecastedWeather[2] = new Weather(list.getJSONObject(3).getJSONObject("main").getDouble("temp") - 273.15);

        return null;
    }

}
