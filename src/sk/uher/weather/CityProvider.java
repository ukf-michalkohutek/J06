package sk.uher.weather;

import javafx.fxml.FXML;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityProvider implements CitySupplier {
//    @FXML
    SQLiteJDBC sqlConnector;

    public CityProvider() {
        this.sqlConnector = new SQLiteJDBC();
    }

    @Override
    public City get() {
        return  new City(0, "Test");
    }

    @Override
    public City get(String cityName) {
        int cityId = -1;
        ResultSet resultSet = null;
        try {
            resultSet = sqlConnector.getCity(cityName);

            if (resultSet.next()) {
                cityId = resultSet.getInt("id");
//                cityName = resultSet.getString("name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (cityId != -1) {
            return new City(cityId, cityName);
        }

        return new City(-1, "City Not Found");
    }

    //TODO: ak bude treba, dorobit get aj na zaklade IDcka
}