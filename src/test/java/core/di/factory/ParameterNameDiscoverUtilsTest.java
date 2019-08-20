package core.di.factory;

import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ParameterNameDiscoverUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger( ParameterNameDiscoverUtilsTest.class );

    @DisplayName("생성자 파라미터 조회")
    @Test
    public void getConstructorsParameters() {
        Class<?> clazz = User.class;
        List<ConstructorParameters> constructorParameters = Arrays.asList(clazz.getConstructors())
                .stream()
                .map(ParameterNameDiscoverUtils::toConstructorParameters)
                .collect(toList());

        assertThat(constructorParameters).isNotNull();
        assertThat(constructorParameters).isNotEmpty();

        constructorParameters.stream()
                .forEach(cp -> logger.debug("{}", cp));
    }


    @DisplayName("생성자 파라미터 조회 파라미터 이름기준 후보조회")
    @Test
    public void getConstructorsParameters2() {
        Class<?> clazz = User.class;
        Optional<ConstructorParameters> constructorParameters = Arrays.asList(clazz.getConstructors())
                .stream()
                .map(ParameterNameDiscoverUtils::toConstructorParameters)
                .filter(cp -> cp.isMatchedParamNames(Arrays.asList("userId", "name", "email", "password")))
                .findFirst();

        assertThat(constructorParameters.isPresent()).isTrue();
    }

    @DisplayName("생성자 파라미터 조회 파라미터 이름기준 후보조회 실패")
    @Test
    public void getConstructorsParameters3() {
        Class<?> clazz = User.class;
        Optional<ConstructorParameters> constructorParameters = Arrays.asList(clazz.getConstructors())
                .stream()
                .map(ParameterNameDiscoverUtils::toConstructorParameters)
                .filter(cp -> cp.isMatchedParamNames(Arrays.asList("userId", "name", "email")))
                .findFirst();

        assertThat(constructorParameters.isPresent()).isFalse();
    }
}
