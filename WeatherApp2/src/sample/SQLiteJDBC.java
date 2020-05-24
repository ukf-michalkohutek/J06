package sample;
import java.sql.*;

public class SQLiteJDBC {

    public SQLiteJDBC(){
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = getConnection();

            stmt = c.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS city" +
                    " (id  INTEGER    NOT NULL UNIQUE, " +
                    " name  TEXT    NOT NULL)";
            stmt.executeUpdate(createTable);
            stmt.close();
            c.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );

        }
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




    public void insertCity(long id, String name) {
        try {
            PreparedStatement preparedStatement;
            Connection connection =  this.getConnection();
            String sqlInsert = "INSERT INTO city (id, name)" +
                    "VALUES (?,?)";
            preparedStatement = connection.prepareStatement(sqlInsert);
            preparedStatement.setLong(1,id);
            preparedStatement.setString(2,name);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println( e.getMessage() );
        }
    }

    public long getIdFromName(String name) {
        try {
        PreparedStatement ps;
        Connection con = this.getConnection();
        String sqlSelect = "SELECT id FROM city WHERE name LIKE ?";
        ps = con.prepareStatement(sqlSelect);
        ps.setString(1,name);
        ResultSet rs = ps.executeQuery();
            while ( rs.next() )
            {
               return rs.getLong("id");
            }
        } catch (SQLException e) {
            System.err.println( e.getMessage() );
        }
return 0;
    }



    public ResultSet getResultSet() throws SQLException {
        String query = "SELECT * FROM city";
        ResultSet resultSet = this.getConnection().createStatement().executeQuery(query);
        return resultSet;

    }

}