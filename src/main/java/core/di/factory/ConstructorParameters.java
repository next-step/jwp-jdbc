package core.di.factory;

import java.lang.reflect.Constructor;
import java.util.List;

public class ConstructorParameters extends ParameterNameDiscoverUtils.Parameters {
    private final Constructor constructor;

    public ConstructorParameters(Constructor constructor, List<ParameterNameDiscoverUtils.ParameterTypeName> parameterTypeNames) {
        super(parameterTypeNames);
        this.constructor = constructor;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    @Override
    public String toString() {
        return "ConstructorParameters{" +
                "constructor=" + constructor +
                ", parameterTypeNames=" + parameterTypeNames +
                '}';
    }
}
