package sk.uher.weather;

public class City {
    private String cityName;
    private int id;

    public City(int id, String cityName) {
        this.id = id;
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public int getId() {
        return id;
    }
}
