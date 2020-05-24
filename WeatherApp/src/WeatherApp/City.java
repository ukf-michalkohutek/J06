package WeatherApp;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class City {

    private String name;
    private ZoneOffset timezoneOffset;
    private LocalDateTime sunrise;
    private LocalDateTime sunset;

    public City(String name, long timezoneOffsetInSeconds, long sunriseEpoch, long sunsetEpoch) {
        setName(name);
        setTimezoneOffset(ZoneOffset.ofTotalSeconds((int) timezoneOffsetInSeconds));
        setSunrise(LocalDateTime.ofEpochSecond(sunriseEpoch,0,timezoneOffset));
        setSunset(LocalDateTime.ofEpochSecond(sunsetEpoch,0,timezoneOffset));
    }

    // Getters
    public String getName() { return name; }
    public ZoneOffset getTimezoneOffset() { return timezoneOffset; }
    public LocalDateTime getSunrise() { return sunrise; }
    public LocalDateTime getSunset() { return sunset; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setTimezoneOffset(ZoneOffset timezoneOffset) { this.timezoneOffset = timezoneOffset; }
    public void setSunrise(LocalDateTime sunrise) { this.sunrise = sunrise; }
    public void setSunset(LocalDateTime sunset) { this.sunset = sunset; }
}
