package core.di.factory;

import java.util.Arrays;

public class ClassNewInstanceUtils {

    public static ConstructorParameters getContructorParameters(Class<?> clazz, String... parameterNames) {
        return Arrays.asList(clazz.getConstructors())
                .stream()
                .map(ParameterNameDiscoverUtils::toConstructorParameters)
                .filter(cp -> cp.isMatchedParamNames(Arrays.asList(parameterNames)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("가능한 생성자가 없습니다."));
    }

}
