package WeatherApp;

public class Result {

    private int tempDay;
    private int tempNight;
    private String weather;
    private String description;
    private String icon;

    public Result(int tempDay, int tempNight, String weather, String description, String icon) {
        setTempDay(tempDay);
        setTempNight(tempNight);
        setWeather(weather);
        setDescription(description.substring(0,1).toUpperCase()+description.substring(1));
        setIcon(icon);
    }

    // Getters
    public int getTempDay() { return tempDay; }
    public int getTempNight() { return tempNight; }
    public String getWeather() { return weather; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }

    // Setters
    public void setTempDay(int tempDay) { this.tempDay = tempDay; }
    public void setTempNight(int tempNight) { this.tempNight = tempNight; }
    public void setWeather(String weather) { this.weather = weather; }
    public void setDescription(String description) { this.description = description; }
    public void setIcon(String icon) { this.icon = icon; }
}
