package by.clevertec.proxy.pattern;

import by.clevertec.cach.Cache;
import by.clevertec.dao.CarDAO;
import by.clevertec.entity.Car;
import by.clevertec.proxy.factory.Action;
import by.clevertec.proxy.factory.ActionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.CarTestData;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActionFactoryTest {

    @Mock
    private CarDAO carDAO;
    @Mock
    private Cache<UUID, Car> cache;

    @InjectMocks
    private ActionFactory actionFactory;

    @Test
    void shouldCheckAction() {
        // given
        Action actualGetById = actionFactory.getAction("getById");
        Action actualSave = actionFactory.getAction("save");
        Action actualUpdate = actionFactory.getAction("update");
        Action actualDelete = actionFactory.getAction("delete");

        // when, then
        assertThat(actualGetById).isNotNull();
        assertThat(actualSave).isNotNull();
        assertThat(actualUpdate).isNotNull();
        assertThat(actualDelete).isNotNull();
        assertThatThrownBy(() -> actionFactory.getAction("nonExistent"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported method: nonExistent");
    }

    @Test
    void shouldReturnOptionalWhenCacheAndDBNotExistCar() {
        // given
        Car car = CarTestData.builder()
                .build()
                .buildCar();
        when(cache.get(car.getId())).thenReturn(null);
        when(carDAO.getById(car.getId())).thenReturn(Optional.empty());

        // when
        Optional<Car> actual = actionFactory.getById(new Object[]{car.getId()});

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnCarForCache() {
        // given
        Car expected = CarTestData.builder()
                .build()
                .buildCar();
        when(cache.get(expected.getId())).thenReturn(expected);

        // when
        Optional<Car> actual = actionFactory.getById(new Object[]{expected.getId()});

        // then
        assertThat(actual).contains(expected);
    }

    @Test
    void shouldReturnCarForDBAndAddInCache() {
        // given
        Car expected = CarTestData.builder()
                .build()
                .buildCar();
        when(cache.get(expected.getId())).thenReturn(null);
        when(carDAO.getById(expected.getId())).thenReturn(Optional.of(expected));

        // when
        Optional<Car> actual = actionFactory.getById(new Object[]{expected.getId()});

        // then
        assertThat(actual).contains(expected);
        verify(cache).put(expected.getId(), expected);
    }


    @Test
    void shouldSaveCarInDBAndAddInCache() {
        // given
        Car expected = CarTestData.builder()
                .build()
                .buildCar();
        when(carDAO.save(expected)).thenReturn(expected);

        // when
        Car actual = actionFactory.save(new Object[]{expected});
        // when, then
        assertThat(actual).isEqualTo(expected);
        verify(carDAO).save(expected);
    }

    @Test
    void shouldUpdateCarInDBAndInCache() {
        // given
        Car expected = CarTestData.builder()
                .build()
                .buildCar();
        when(carDAO.update(expected)).thenReturn(expected);

        // when
        Car actual = actionFactory.update(new Object[]{expected});

        // then
        assertThat(actual).isEqualTo(expected);
        verify(carDAO).update(expected);
        verify(cache).put(expected.getId(), expected);
    }

    @Test
    void shouldDeleteCarWhenContainInCache() {
        // given
        Car car = CarTestData.builder()
                .build()
                .buildCar();
        when(cache.get(car.getId())).thenReturn(car);

        // when
        Car deletedCar = actionFactory.delete(new Object[]{car.getId()});

        // then
        assertThat(deletedCar).isNull();
        verify(carDAO).delete(car.getId());
        verify(cache).get(car.getId());
    }
}