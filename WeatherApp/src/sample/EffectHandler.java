package sample;

import javafx.scene.paint.Color;
import sample.Engine.Core.GameObject;
import sample.Engine.Core.GameObject2D;
import sample.Engine.Core.GameSubScene;
import sample.Engine.Core.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class EffectHandler extends GameObject {

    public enum EffectType
    {
        NONE, THUNDERSTORM, RAIN, SNOW, FOG, CLEAR, CLOUDS
    }

    private GameSubScene targetSubScene;
    private List<GameObject2D> effectObjects = new ArrayList<GameObject2D>();

    public EffectHandler(GameSubScene _targetSubScene)
    {
        targetSubScene = _targetSubScene;
    }

    public void changeEffect(EffectType effectType)
    {
        clearEffects();

        switch (effectType)
        {
            case NONE: return;

            case THUNDERSTORM:
                EffectSky thunderstormEffectSky = new EffectSky(Color.DARKGRAY);
                EffectLand thunderstormEffectLand = new EffectLand(Color.DARKGREEN, 420);
                EffectThunder thunderstormEffectThunder1 = new EffectThunder(Color.YELLOW, 80, new Vector2D<>(340d, 160d));
                EffectThunder thunderstormEffectThunder2 = new EffectThunder(Color.YELLOW, 120, new Vector2D<>(110d, 120d));
                EffectCloud thunderstormEffectCloud1 = new EffectCloud(Color.GREY, new Vector2D<>(25d, 50d));
                EffectCloud thunderstormEffectCloud2 = new EffectCloud(Color.GREY, new Vector2D<>(200d, 80d));
                EffectCloud thunderstormEffectCloud3 = new EffectCloud(Color.GREY, new Vector2D<>(300d, 100d));
                EffectCloud thunderstormEffectCloud4 = new EffectCloud(Color.GREY, new Vector2D<>(100d, 100d));
                addEffect(thunderstormEffectSky);
                addEffect(thunderstormEffectLand);
                addEffect(thunderstormEffectThunder1);
                addEffect(thunderstormEffectThunder2);
                addEffect(thunderstormEffectCloud1);
                addEffect(thunderstormEffectCloud2);
                addEffect(thunderstormEffectCloud3);
                addEffect(thunderstormEffectCloud4);
                for (int i = 0; i < 60; i++) addEffect(new EffectRain(Color.DARKBLUE));
                break;

            case RAIN:
                EffectSky rainEffectSky = new EffectSky(Color.DARKGRAY);
                EffectLand rainEffectLand = new EffectLand(Color.DARKGREEN, 420);
                EffectCloud rainEffectCloud1 = new EffectCloud(Color.GREY, new Vector2D<>(25d, 50d));
                EffectCloud rainEffectCloud2 = new EffectCloud(Color.GREY, new Vector2D<>(200d, 80d));
                EffectCloud rainEffectCloud3 = new EffectCloud(Color.GREY, new Vector2D<>(300d, 100d));
                EffectCloud rainEffectCloud4 = new EffectCloud(Color.GREY, new Vector2D<>(100d, 100d));
                addEffect(rainEffectSky);
                addEffect(rainEffectLand);
                addEffect(rainEffectCloud1);
                addEffect(rainEffectCloud2);
                addEffect(rainEffectCloud3);
                addEffect(rainEffectCloud4);
                for (int i = 0; i < 40; i++) addEffect(new EffectRain(Color.DARKBLUE));
                break;

            case SNOW:
                EffectSky snowEffectSky = new EffectSky(Color.TURQUOISE);
                EffectLand snowEffectLand = new EffectLand(Color.WHITESMOKE, 420);
                addEffect(snowEffectSky);
                addEffect(snowEffectLand);
                for (int i = 0; i < 25; i++) addEffect(new EffectSnow(Color.WHITESMOKE, 40 + Math.random() * 40));
                break;

            case FOG:
                EffectSky fogEffectSky = new EffectSky(Color.WHITESMOKE);
                EffectLand fogEffectLand = new EffectLand(Color.GREY, 420);
                addEffect(fogEffectSky);
                addEffect(fogEffectLand);
                break;

            case CLEAR:
                EffectSky clearEffectSky = new EffectSky(Color.TURQUOISE);
                EffectLand clearEffectLand = new EffectLand(Color.LIMEGREEN, 420);
                EffectSun clearEffectSunBig = new EffectSun(Color.ORANGE, 160, 90, new Vector2D<>(300d, 50d));
                EffectSun clearEffectSunSmall = new EffectSun(Color.ORANGERED, 120, -45, new Vector2D<>(320d, 70d));
                addEffect(clearEffectSky);
                addEffect(clearEffectLand);
                addEffect(clearEffectSunBig);
                addEffect(clearEffectSunSmall);
                break;

            case CLOUDS:
                EffectSky cloudsEffectSky = new EffectSky(Color.TURQUOISE);
                EffectLand cloudsEffectLand = new EffectLand(Color.LIMEGREEN, 420);
                EffectCloud cloudsEffectCloud1 = new EffectCloud(Color.LIGHTGREY, new Vector2D<>(25d, 50d));
                EffectCloud cloudsEffectCloud2 = new EffectCloud(Color.LIGHTGREY, new Vector2D<>(200d, 80d));
                EffectCloud cloudsEffectCloud3 = new EffectCloud(Color.LIGHTGREY, new Vector2D<>(300d, 100d));
                EffectCloud cloudsEffectCloud4 = new EffectCloud(Color.LIGHTGREY, new Vector2D<>(100d, 100d));
                addEffect(cloudsEffectSky);
                addEffect(cloudsEffectLand);
                addEffect(cloudsEffectCloud1);
                addEffect(cloudsEffectCloud2);
                addEffect(cloudsEffectCloud3);
                addEffect(cloudsEffectCloud4);
                break;
        }
    }

    private void clearEffects()
    {
        for (GameObject obj : effectObjects) { obj.destroy(); }
        effectObjects.clear();
    }

    private void addEffect(GameObject2D effect)
    {
        effectObjects.add(effect);
        addToScene(effect);
    }

}
