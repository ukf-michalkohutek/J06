package weather2;


import javafx.beans.property.SimpleStringProperty;

public class City {
    private long id = 0;
    private final SimpleStringProperty name = new SimpleStringProperty("");

    public City() {
        this(0, "");
    }

    public City(long id, String name) {
        setId(id);
        setName(name);
    }


    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

}


