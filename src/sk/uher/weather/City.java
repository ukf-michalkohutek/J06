package sk.uher.weather;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class City {
    private final SimpleStringProperty cityName = new SimpleStringProperty("Item Not Found");
    private final SimpleIntegerProperty id = new SimpleIntegerProperty(-1);

    public City(int id, String cityName) {
        setId(id);
        setCityName(cityName);
    }

    public void setCityName(String cityName) {
        this.cityName.set(cityName);
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getCityName() {
        return cityName.get();
    }

    public int getId() {
        return id.get();
    }
}
