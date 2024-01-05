package by.clevertec.mapper;

import by.clevertec.dto.CarDto;
import by.clevertec.dto.InfoCarDto;
import by.clevertec.entity.Car;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import util.CarTestData;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static util.Constants.*;

class CarMapperTest {

    CarMapper mapper = Mappers.getMapper(CarMapper.class);

    @Nested
    class ToCarTest {

        @Test
        void shouldReturnCarWhenCarDtoCorrect() {
            // given
            CarDto carDto = CarTestData.builder()
                    .build().buildCarDto();
            Car expected = CarTestData.builder()
                    .withId(null)
                    .build()
                    .buildCar();

            // when
            Car actual = mapper.toCar(carDto);

            // then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Car.Fields.id, null)
                    .hasFieldOrPropertyWithValue(Car.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Car.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Car.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Car.Fields.created, null);
        }

        @Test
        void shouldReturnCarWhenCarDtoHasEmptyField() {
            // given
            CarDto carDto = CarTestData.builder()
                    .withDescription(null)
                    .build()
                    .buildCarDto();
            Car expected = CarTestData.builder()
                    .withId(null)
                    .withDescription(null)
                    .build()
                    .buildCar();

            // when
            Car actual = mapper.toCar(carDto);

            // then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Car.Fields.id, null)
                    .hasFieldOrPropertyWithValue(Car.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Car.Fields.description, null)
                    .hasFieldOrPropertyWithValue(Car.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Car.Fields.created, null);
        }

        @Test
        void shouldReturnNullWhenCarDtoIsNull() {
            // given
            CarDto carDto = null;

            // when
            Car actual = mapper.toCar(carDto);

            // then
            assertThat(actual).isNull();
        }
    }

    @Nested
    class ToInfoCarDtoTest {

        @Test
        void shouldReturnExpectedInfoCarDto() {
            // given
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            InfoCarDto expected = CarTestData.builder()
                    .build()
                    .buildInfoCarDto();

            // when
            InfoCarDto actual = mapper.toInfoCarDto(car);

            // then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Car.Fields.id, expected.id())
                    .hasFieldOrPropertyWithValue(Car.Fields.name, expected.name())
                    .hasFieldOrPropertyWithValue(Car.Fields.description, expected.description())
                    .hasFieldOrPropertyWithValue(Car.Fields.price, expected.price());
        }

        @Test
        void shouldReturnInfoCarDtoWhenCarHasEmptyField() {
            // given
            Car car = CarTestData.builder()
                    .withName(null)
                    .build()
                    .buildCar();
            InfoCarDto expected = CarTestData.builder()
                    .withName(null)
                    .build()
                    .buildInfoCarDto();

            // when
            InfoCarDto actual = mapper.toInfoCarDto(car);

            // then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Car.Fields.id, expected.id())
                    .hasFieldOrPropertyWithValue(Car.Fields.name, expected.name())
                    .hasFieldOrPropertyWithValue(Car.Fields.description, expected.description())
                    .hasFieldOrPropertyWithValue(Car.Fields.price, expected.price());
        }

        @Test
        void shouldReturnNullWhenCarisNull() {
            // given
            Car car = null;

            // when
            InfoCarDto actual = mapper.toInfoCarDto(car);

            // then
            assertThat(actual).isNull();
        }
    }

    @Nested
    class MergeTest {

        @Test
        void shouldReturnExpectedUpdatedCar() {
            // given
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            CarDto carDto = CarTestData.builder()
                    .withName(UPDATE_CAR_NAME)
                    .withDescription(UPDATE_CAR_DESCRIPTION)
                    .withPrice(UPDATE_CAR_PRICE)
                    .build()
                    .buildCarDto();
            Car expected = CarTestData.builder()
                    .withName(UPDATE_CAR_NAME)
                    .withDescription(UPDATE_CAR_DESCRIPTION)
                    .withPrice(UPDATE_CAR_PRICE)
                    .build()
                    .buildCar();

            // when
            Car actual = mapper.merge(car, carDto);

            // then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Car.Fields.id, expected.getId())
                    .hasFieldOrPropertyWithValue(Car.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Car.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Car.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Car.Fields.created, expected.getCreated());
        }

        @Test
        void shouldReturnExpectedUpdatedCarIfProductDtoContainsEmptyField() {
            // given
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            CarDto carDto = CarTestData.builder()
                    .withName(UPDATE_CAR_NAME)
                    .withDescription(UPDATE_CAR_DESCRIPTION)
                    .build()
                    .buildCarDto();
            Car expected = CarTestData.builder()
                    .withName(UPDATE_CAR_NAME)
                    .withDescription(UPDATE_CAR_DESCRIPTION)
                    .build()
                    .buildCar();

            // then
            Car actual = mapper.merge(car, carDto);

            // when
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Car.Fields.id, expected.getId())
                    .hasFieldOrPropertyWithValue(Car.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Car.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Car.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Car.Fields.created, expected.getCreated());
        }

        @Test
        void shouldReturnExpectedUpdatedCarIfProductDtoContainsEmptyAllFields() {
            // given
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            CarDto carDto = CarTestData.builder()
                    .withName(null)
                    .withDescription(null)
                    .withPrice(null)
                    .withCreated(null)
                    .build()
                    .buildCarDto();
            Car expected = CarTestData.builder()
                    .build()
                    .buildCar();

            // when
            Car actual = mapper.merge(car, carDto);

            // then
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Car.Fields.id, expected.getId())
                    .hasFieldOrPropertyWithValue(Car.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Car.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Car.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Car.Fields.created, expected.getCreated());
        }

        @Test
        void shouldReturnNullPointerExceptionWhenCarIsNull() {
            // given
            Car car = null;
            CarDto carDto = CarTestData.builder()
                    .withName(UPDATE_CAR_NAME)
                    .withDescription(UPDATE_CAR_DESCRIPTION)
                    .withPrice(UPDATE_CAR_PRICE)
                    .build()
                    .buildCarDto();

            // when, then
            assertThrows(NullPointerException.class, () -> mapper.merge(car, carDto));
        }

        @Test
        void shouldReturnUpdatedCarWhenCarDtoIsNull() {
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            CarDto carDto = null;
            Car expected = CarTestData.builder()
                    .build()
                    .buildCar();

            Car actual = mapper.merge(car, carDto);

            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Car.Fields.id, expected.getId())
                    .hasFieldOrPropertyWithValue(Car.Fields.name, expected.getName())
                    .hasFieldOrPropertyWithValue(Car.Fields.description, expected.getDescription())
                    .hasFieldOrPropertyWithValue(Car.Fields.price, expected.getPrice())
                    .hasFieldOrPropertyWithValue(Car.Fields.created, expected.getCreated());
        }
    }
}