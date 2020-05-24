package sk.uher.weather;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    //TODO:
    // zobrazovat aktualny datetime
    // ukladanie poslednych vyhladavanych miest v JSONe, zobrazit ich aj po spusteni aplikacie
    // pocasie na 3 dni
    // zmena farby pozadia podla teploty maybe?
    // vyuzit metody z predchadzajucich zadani, len zmenit url
    // API KEY: 5002fd410cb289dcdfe844fe50f2649f
    //
    // PreparedStatement ps;
    //        Connection con = this.getConnection();
    //        String sqlSelect = "SELECT id FROM city WHERE name LIKE ?";
    //        ps = con.prepareStatement(sqlSelect);
    //        ps.setString(1,name);
    //        ResultSet rs = ps.executeQuery();
    //

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
        loader.setControllerFactory(t -> new Controller(new CityProvider()));
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root, 1100, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
