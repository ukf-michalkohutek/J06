package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.*;

public class SQLiteJDBC {

    public SQLiteJDBC(){
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = getConnection();
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS city" +
                    " (id  INTEGER    NOT NULL, " +
                    " name  TEXT    NOT NULL, " +
                    " state  TEXT    NOT NULL, " +
                    " country  TEXT    NOT NULL, " +
                    " lon  REAL    NOT NULL, " +
                    " lat  REAL    NOT NULL )";
            stmt.executeUpdate(createTable);
            stmt.close();
            c.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );

        }
        System.out.println("Table created successfully");
    }

    void populateCombo()
    {
        Connection conn = getConnection();

    }

    public Connection getConnection() {
        String url = "jdbc:sqlite:cities.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }




    public void insertCity(long id, String name, String state, String country, double lon, double lat) {
        try {
            PreparedStatement preparedStatement;
            Connection connection =  this.getConnection();
            String sqlInsert = "INSERT INTO city (id, name, state, country, lon, lat)" +
                    "VALUES (?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sqlInsert);
            preparedStatement.setLong(1,id);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,state);
            preparedStatement.setString(4,country);
            preparedStatement.setDouble(5,lon);
            preparedStatement.setDouble(6,lat);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println( e.getMessage() );
        }
    }

    public ResultSet getResultSet() throws SQLException {
        String query = "SELECT * FROM city";
        ResultSet resultSet = this.getConnection().createStatement().executeQuery(query);
        return resultSet;

    }

    void fillCombo(ComboBox<String> comboBox) throws SQLException {
        String query = "SELECT * FROM city";
        ResultSet resultSet = this.getConnection().createStatement().executeQuery(query);
        ObservableList data = FXCollections.observableArrayList();
        while (resultSet.next())
        {
            data.add(resultSet.getString("name"));
        }
        data = data.sorted();
        comboBox.setItems(data);
    }

}