package sk.uher.weather;

public class Weather extends City{
    private String cityName;
    private double temp;
    private int id;

    public Weather(int id, String cityName, double temp) {
        super(id, cityName);
        this.temp = temp;
    }

    public double getTemp() {
        return temp;
    }
}
