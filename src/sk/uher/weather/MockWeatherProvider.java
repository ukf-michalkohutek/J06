package sk.uher.weather;

public class MockWeatherProvider implements WeatherSupplier {

    @Override
    public Weather get() {
        return  new Weather(0, "Test", 0.0);
    }

    @Override
    public Weather get(String city) {
        return new Weather(1, city, 19.5);
    }
}