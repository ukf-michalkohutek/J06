package sk.uher.weather;

import javafx.beans.property.SimpleDoubleProperty;

public class Weather extends City{
    private final SimpleDoubleProperty temp = new SimpleDoubleProperty(0);

    public Weather(int id, String cityName, double temp) {
        super(id, cityName);
        setTemp(temp);
    }

    public void setTemp(double temp) {
        this.temp.set(temp);
    }

    public double getTemp() {
        return temp.get();
    }
}
