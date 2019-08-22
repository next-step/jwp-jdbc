package study.jackson;

import java.util.Objects;

public class Car {
    private Color color;
    private String type;

    private Car() {
    }

    public Car(final Color color, final String type) {
        this.color = color;
        this.type = type;
    }

    public Color getColor() {
        return this.color;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final Car car = (Car) o;
        return this.color.equals(car.color) &&
                this.type.equals(car.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.color, this.type);
    }

    @Override
    public String toString() {
        return "Car{" +
                "color='" + this.color + '\'' +
                ", type='" + this.type + '\'' +
                '}';
    }
}
