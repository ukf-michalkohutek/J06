package sk.uher.weather;

import java.util.function.Supplier;

public interface CitySupplier extends Supplier<City> {

    City get(String city);
}