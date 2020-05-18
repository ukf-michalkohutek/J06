package sample;

import javafx.beans.property.SimpleStringProperty;

public class Result {
    private final SimpleStringProperty dateTime = new SimpleStringProperty("");
    private final SimpleStringProperty temperature = new SimpleStringProperty("");
    private final SimpleStringProperty feels_like = new SimpleStringProperty("");
    private final SimpleStringProperty temp_min = new SimpleStringProperty("");
    private final SimpleStringProperty temp_max = new SimpleStringProperty("");
    private final SimpleStringProperty pressure = new SimpleStringProperty("");
    private final SimpleStringProperty humidity = new SimpleStringProperty("");

    public Result() { this("","","","","","",""); }

    public Result(String dateTime, String temperature, String feels_like, String temp_min, String temp_max, String pressure, String humidity) {
        setDateTime(dateTime);
        setTemperature(temperature);
        setFeels_like(feels_like);
        setTemp_min(temp_min);
        setTemp_max(temp_max);
        setPressure(pressure);
        setHumidity(humidity);
    }

    public String getDateTime(){return dateTime.get();}

    private void setDateTime(String dateTime){this.dateTime.set(dateTime);}

    public String getTemperature(){return temperature.get();}

    private void setTemperature(String temperature){this.temperature.set(temperature);}

    public String getFeels_like(){return feels_like.get();}

    private void setFeels_like(String feels_like){this.feels_like.set(feels_like);}

    public String getTemp_min(){return temp_min.get();}

    private void setTemp_min(String temp_min){this.temp_min.set(temp_min);}

    public String getTemp_max(){return temp_max.get();}

    private void setTemp_max(String temp_max){this.temp_max.set(temp_max);}

    public String getPressure(){return pressure.get();}

    private void setPressure(String pressure){this.pressure.set(pressure);}

    public String getHumidity(){return humidity.get();}

    private void setHumidity(String humidity){this.humidity.set(humidity);}
}
