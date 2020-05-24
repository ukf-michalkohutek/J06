package sk.uher.weather;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Weather extends City{
    private final SimpleDoubleProperty temp = new SimpleDoubleProperty(0.0);
    private final SimpleStringProperty date = new SimpleStringProperty("");
    private final SimpleStringProperty weather = new SimpleStringProperty("");

    public Weather(int id, String cityName, double temp, String date, String weather) {
        super(id, cityName);
        setTemp(temp);
        setDate(date);
        setWeather(weather);
    }

    public String getWeather() {
        return weather.get();
    }

    public void setWeather(String weather) {
        this.weather.set(weather);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setTemp(double temp) {
        this.temp.set(temp);
    }

    public double getTemp() {
        return this.temp.get();
    }
}
