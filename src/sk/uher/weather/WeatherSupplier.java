package sk.uher.weather;

import java.util.function.Supplier;

public interface WeatherSupplier extends Supplier<Weather> {

    Weather get(String city);
}
