package sample;

public class City {

    private int id = 0;
    private String name = "";

    public City() {
        this(0, "");
    }
    public City(int id, String name) {
        this.id=id;this.name=name;
    }

    //Setters
    public void setId(int id) {
        this.id=id;
    }
    public void setName(String name) {
        this.name=name;
    }
    //Getters
    public long getId() {
        return this.id;
    }
    public String getName() {
        return name;
    }

}


