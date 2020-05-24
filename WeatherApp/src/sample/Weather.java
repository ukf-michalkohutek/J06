package sample;

public class Weather {

    public String cityName;
    public String weatherType;
    public double temperatureCelsius;

    public Weather(String _cityName, String _weatherType, double _temperatureCelsius)
    {
        cityName = _cityName;
        weatherType = _weatherType;
        temperatureCelsius = _temperatureCelsius;
    }

    public Weather(double _temperatureCelsius)
    {
        temperatureCelsius = _temperatureCelsius;
    }

}
