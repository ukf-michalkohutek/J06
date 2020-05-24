package sample;

import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import sample.Engine.Core.*;
import sample.Engine.SolidBackground;
import sample.Engine.TextObject;

public class MainAppScene extends GameScene {

    public MainAppScene()
    {
        Vector2D<Double> resolution = Main.getCurrentResolution();

        SolidBackground blackBackground = new SolidBackground(Color.BLACK);

        GameSubScene effectScene = new GameSubScene();

        EffectHandler effectHandler = new EffectHandler(effectScene);
        effectScene.addObject(effectHandler);
        effectHandler.changeEffect(EffectHandler.EffectType.NONE);

        TextObject cityTextObject = new TextObject("", resolution.x, 100);
        cityTextObject.setTextColor(Color.WHITE);
        cityTextObject.setFontSize(60);
        cityTextObject.setTextAlignment(TextAlignment.CENTER);
        cityTextObject.setPosition(0, 100);

        TextObject temperatureTextObject = new TextObject("", resolution.x, 400);
        temperatureTextObject.setTextColor(Color.WHITE);
        temperatureTextObject.setFontSize(120);
        temperatureTextObject.setTextAlignment(TextAlignment.CENTER);
        temperatureTextObject.setPosition(0, 200);

        TextObject weatherTextObject = new TextObject("Start Typing", resolution.x, 80);
        weatherTextObject.setTextColor(Color.WHITE);
        weatherTextObject.setFontSize(40);
        weatherTextObject.setTextAlignment(TextAlignment.CENTER);
        weatherTextObject.setPosition(0, 340);

        TextObject day1TemperatureTextObject = new TextObject("", 160, 120);
        day1TemperatureTextObject.setTextColor(Color.WHITE);
        day1TemperatureTextObject.setFontSize(22);
        day1TemperatureTextObject.setTextAlignment(TextAlignment.CENTER);
        day1TemperatureTextObject.setPosition(0, 540);

        TextObject day2TemperatureTextObject = new TextObject("", resolution.x, 120);
        day2TemperatureTextObject.setTextColor(Color.WHITE);
        day2TemperatureTextObject.setFontSize(22);
        day2TemperatureTextObject.setTextAlignment(TextAlignment.CENTER);
        day2TemperatureTextObject.setPosition(0, 540);

        TextObject day3TemperatureTextObject = new TextObject("", 160, 120);
        day3TemperatureTextObject.setTextColor(Color.WHITE);
        day3TemperatureTextObject.setFontSize(22);
        day3TemperatureTextObject.setTextAlignment(TextAlignment.CENTER);
        day3TemperatureTextObject.setPosition(resolution.x - 160, 540);

        ColorRectangle whiteBg = new ColorRectangle(resolution.x, 40, Color.WHITE, Color.LIGHTGREY);
        LocationTextField locationTextField = new LocationTextField(resolution.x, 40);
        locationTextField.setPosition(0, 10);

        APIHandler apiHandler = new APIHandler(
                "db9c09e9bc37300f0bc1bcc95d4fcdf1", effectHandler, locationTextField,
                cityTextObject, temperatureTextObject, weatherTextObject,
                day1TemperatureTextObject, day2TemperatureTextObject, day3TemperatureTextObject
        );
        locationTextField.setOnEnter(new LocationTextField.TextFieldEnterEventListener() {
            @Override
            public void onEnterPress() {
                apiHandler.requestWeatherInfo();
            }
        });

        objects.add(blackBackground);
        objects.add(effectScene);
        objects.add(cityTextObject);
        objects.add(temperatureTextObject);
        objects.add(weatherTextObject);
        objects.add(day1TemperatureTextObject);
        objects.add(day2TemperatureTextObject);
        objects.add(day3TemperatureTextObject);
        objects.add(whiteBg);
        objects.add(locationTextField);
        objects.add(apiHandler);
    }

}