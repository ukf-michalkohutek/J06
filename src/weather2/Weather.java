package weather2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.JSONArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;

public class Weather extends Group {

    int resX = 800;
    int resY = 600;

    SQLiteJDBC sqlConnector;

    private String APIKEY = "4bd868975ea32a8d2906308e5b3318ca";

    public Weather(int x, int y) throws Exception{
        Parent par = FXMLLoader.load(getClass().getResource("weather2.fxml"));

        sqlConnector = new SQLiteJDBC();

        ImageView backgroundIW = new ImageView();

        TextField searchBox = new TextField();
        searchBox.setPromptText("mesto");
        searchBox.setLayoutX(20);
        searchBox.setLayoutY(20);
        searchBox.setMinWidth(700);

        Button searchButton = new Button("Hľadaj");
        searchButton.setLayoutX(730);
        searchButton.setLayoutY(20);
        searchButton.setMinWidth(40);

        DropShadow ds = new DropShadow();
        ds.setRadius(5);

        ImageView mainIcon = new ImageView();
        mainIcon.setScaleX(3);
        mainIcon.setScaleY(3);
        mainIcon.setLayoutY(100);
        mainIcon.setLayoutX(100);
        mainIcon.setEffect(ds);
        mainIcon.setVisible(false);

        int gap = 40;

        ImageView icon1 = new ImageView();
        icon1.setScaleX(0.5);
        icon1.setScaleY(0.5);
        icon1.setLayoutY(240);
        icon1.setLayoutX(610);
        icon1.setEffect(ds);
        icon1.setVisible(false);

        ImageView icon2 = new ImageView();
        icon2.setScaleX(0.5);
        icon2.setScaleY(0.5);
        icon2.setLayoutY(240+gap*1);
        icon2.setLayoutX(610);
        icon2.setEffect(ds);
        icon2.setVisible(false);

        ImageView icon3 = new ImageView();
        icon3.setScaleX(0.5);
        icon3.setScaleY(0.5);
        icon3.setLayoutY(240+gap*2);
        icon3.setLayoutX(610);
        icon3.setEffect(ds);
        icon3.setVisible(false);

        ImageView icon4 = new ImageView();
        icon4.setScaleX(0.5);
        icon4.setScaleY(0.5);
        icon4.setLayoutY(240+gap*3);
        icon4.setLayoutX(610);
        icon4.setEffect(ds);
        icon4.setVisible(false);

        Font big = new Font(40);
        Font medium = new Font(30);

        Text mainText = new Text("Zadaj hore mesto");
        mainText.setFont(big);
        mainText.setLayoutX(50);
        mainText.setLayoutY(200);
        mainText.setFill(Color.WHITE);

        String days = ""+new Date().toString().substring(0,10)+"\n"+
                new Date(new Date().getTime()+(1000*60*60*24)).toString().substring(0,10)+"\n"+
                new Date(new Date().getTime()+(1000*60*60*48)).toString().substring(0,10)+"\n"+
                new Date(new Date().getTime()+(1000*60*60*72)).toString().substring(0,10)+"\n";

        Text daysText = new Text(days);
        daysText.setFont(medium);
        daysText.setLayoutX(50);
        daysText.setLayoutY(300);
        daysText.setVisible(false);

        Text tempText = new Text("");
        tempText.setFont(medium);
        tempText.setLayoutX(700);
        tempText.setLayoutY(300);

        setLayoutX(x);
        setLayoutY(y);

        connectDatabase();
        downloadCurrentWeatherDataByID(sqlConnector.getIdFromName("Nitra"));

        Rectangle r1 = new Rectangle(0,0,resX,250);
        r1.setOpacity(0.4);
        r1.setFill(Color.BLACK);

        searchButton.setOnMouseClicked(e->{
            long mesto = sqlConnector.getIdFromName(searchBox.getText());
            if(mesto == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Chyba");
                alert.setHeaderText("Chyba");
                alert.setContentText("Mesto neexistuje v databáze");

                alert.showAndWait();
            }
            else{
                downloadCurrentWeatherDataByID((mesto));

                //LEPIACA PASKA NAS OPAT ZACHRANILA
                String time = resultDates.toString().substring((resultDates.toString().indexOf("\"dt_txt\""))+21,resultDates.toString().indexOf("\"dt_txt\"")+29);
                System.out.println("čas: "+time);

                String temp1 = resultDates.toString().substring((resultDates.toString().indexOf("\"temp\""))+7,resultDates.toString().length());
                String temp2 = temp1.substring(temp1.indexOf(time),temp1.length());
                temp2 = temp2.substring(temp2.indexOf("\"temp\"")+7,temp2.length());
                String temp3 = temp2.substring(temp2.indexOf(time),temp2.length());
                temp3 = temp3.substring(temp3.indexOf("\"temp\"")+7,temp3.length());
                String temp4 = temp3.substring(temp3.indexOf(time),temp3.length());
                temp4 = temp4.substring(temp4.indexOf("\"temp\"")+7,temp4.length());

                mainText.setText(searchBox.getText() +" ("+mesto+"): "+(Integer.parseInt(temp1.substring(0,3))-272)+" °c\n");

                tempText.setText(""+    (Integer.parseInt(temp1.substring(0,3))-272)+" °c\n"+
                                        (Integer.parseInt(temp2.substring(0,3))-272)+" °c\n"+
                                        (Integer.parseInt(temp3.substring(0,3))-272)+" °c\n"+
                                        (Integer.parseInt(temp4.substring(0,3))-272)+" °c\n"
                );

                String i1 = resultDates.toString().substring((resultDates.toString().indexOf("\"icon\""))+8,resultDates.toString().length());
                String i2 = i1.substring(i1.indexOf("\"icon\"")+8,i1.length());
                String i3 = i2.substring(i2.indexOf("\"icon\"")+8,i2.length());
                String i4 = i3.substring(i3.indexOf("\"icon\"")+8,i3.length());
                i1=i1.substring(0,3);
                i2=i2.substring(0,3);
                i3=i3.substring(0,3);
                i4=i4.substring(0,3);

                System.out.println("ikony: "+i1+","+i2+","+i3+","+i4);
                mainIcon.setImage(new Image("/images/"+i1+".png",100,100,true,true));
                icon1.setImage(new Image("/images/"+i1+".png",100,100,true,true));
                icon2.setImage(new Image("/images/"+i2+".png",100,100,true,true));
                icon3.setImage(new Image("/images/"+i3+".png",100,100,true,true));
                icon4.setImage(new Image("/images/"+i4+".png",100,100,true,true));

                switch(i1){
                    case "01d":
                    case "01n": backgroundIW.setImage(new Image("/images/bgsunny.jpg",800,600,false,false));System.out.println("bg: sunny");
                                tempText.setFill(Color.BLACK);tempText.setEffect(null);
                                daysText.setFill(Color.BLACK);daysText.setEffect(null);
                                break;
                    case "09d":
                    case "09n":
                    case "10d":
                    case "10n": backgroundIW.setImage(new Image("/images/bgrain.jpg",800,600,false,false));System.out.println("bg: rain");
                                tempText.setFill(Color.WHITE);tempText.setEffect(ds);
                                daysText.setFill(Color.WHITE);daysText.setEffect(ds);
                                break;
                    case "11d":
                    case "11n": backgroundIW.setImage(new Image("/images/bgthunger.jpg",800,600,false,false));System.out.println("bg: thunder");
                                tempText.setFill(Color.WHITE);tempText.setEffect(ds);
                                daysText.setFill(Color.WHITE);daysText.setEffect(ds);
                                break;
                    default: backgroundIW.setImage(new Image("/images/bggeneric.png",800,600,false,false));System.out.println("bg: cloudy");
                                tempText.setFill(Color.BLACK);tempText.setEffect(null);
                                daysText.setFill(Color.BLACK);daysText.setEffect(null);
                                break;
                }

                mainIcon.setVisible(true);
                icon1.setVisible(true);
                icon2.setVisible(true);
                icon3.setVisible(true);
                icon4.setVisible(true);
                daysText.setVisible(true);
                mainText.setLayoutX(270);
            }
        });

        getChildren().addAll(backgroundIW,r1,searchBox,searchButton,mainText,mainIcon,icon1,icon2,icon3,icon4,daysText,tempText,par);

    }

    private void downloadCurrentWeatherDataByID(long id) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openweathermap.org/data/2.5/forecast?id="+id+"&appid="+APIKEY)).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Weather::parseCurrentWeather)
                .join();

    }

    static JSONArray resultDates;

    private static String parseCurrentWeather(String response)  {
        resultDates = new JSONArray("[" + response + "]" );
        //System.out.println(resultDates);

        return null;
    }

    protected void connectDatabase() {
        sqlConnector = new SQLiteJDBC();
    }

}
