package study.jackson;

import java.util.Objects;

public class Car {
    private Color color;
    private String type;

    private Car() {}

    public Car(Color color, String type) {
        this.color = color;
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return color.equals(car.color) &&
                type.equals(car.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    @Override
    public String toString() {
        return "Car{" +
                "color='" + color + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
