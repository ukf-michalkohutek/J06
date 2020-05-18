package sample;

import java.sql.*;

public class SQLiteJDBC {

    public SQLiteJDBC(){
        Connection c;
        Statement stmt;

        try{
            Class.forName("org.sqlite.JDBC");
            c = getConnection();
            System.out.println("Opened database sucessfully");
            stmt = c.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS city(id INTEGER    NOT NULL UNIQUE, name   TEXT    NOT NULL)";
            stmt.executeUpdate(createTable);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Table created successfully");
    }

    public Connection getConnection(){
        String url = "jdbc:sqlite:cities.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insertCity(long id, String name){
        try {
            PreparedStatement preparedStatement;
            Connection connection = this.getConnection();
            String sqlInsert = "INSERT INTO city (id, name)VALUES (?,?)";
            preparedStatement = connection.prepareStatement(sqlInsert);
            preparedStatement.setLong(1,id);
            preparedStatement.setString(1,name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public long getIdFromName(String name) {
        long x = 0;
        try {
            PreparedStatement ps;
            Connection con = this.getConnection();
            String sqlSelect = "SELECT id FROM city WHERE name LIKE ?";
            ps = con.prepareStatement(sqlSelect);
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();
            x = rs.getLong(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return x;
    }
}
