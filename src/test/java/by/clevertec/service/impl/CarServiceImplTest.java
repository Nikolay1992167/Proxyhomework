package by.clevertec.service.impl;

import by.clevertec.dao.CarDAO;
import by.clevertec.dto.CarDto;
import by.clevertec.dto.InfoCarDto;
import by.clevertec.entity.Car;
import by.clevertec.exception.NotFoundException;
import by.clevertec.mapper.CarMapper;
import by.clevertec.proxy.MyInvocationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.CarTestData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarMapper mapper;

    @Mock
    private CarDAO carDAOproxy;

    @Mock
    private MyInvocationHandler handler;

    @InjectMocks
    private CarServiceImpl carService;

    @Captor
    private ArgumentCaptor<Car> captor;

    CarServiceImplTest() {
    }

    @BeforeEach
    public void init() {
        carService.setCarDAOproxy(carDAOproxy);
    }

    @Nested
    class FindByIdTest {

        @Test
        void shouldThrowCarNotFoundExceptionWithExpectedMessage() {
            // given
            UUID id = UUID.fromString("33e4b6c3-c84d-47b7-ac0b-a9f8566d7950");
            String expectedMessage = "Car with 33e4b6c3-c84d-47b7-ac0b-a9f8566d7950 not found!";

            // when
            Exception exception = assertThrows(NotFoundException.class, () -> carService.findById(id));
            String actualMessage = exception.getMessage();

            // then
            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        void shouldReturnExpectedResponse() {
            // given
            InfoCarDto expected = CarTestData.builder()
                    .build()
                    .buildInfoCarDto();
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            UUID id = expected.id();

            when(carDAOproxy.getById(id)).thenReturn(Optional.of(car));
            when(mapper.toInfoCarDto(car)).thenReturn(expected);

            // when
            InfoCarDto actual = carService.findById(id);

            // then
            assertThat(actual).isEqualTo(expected);
            verify(carDAOproxy, times(1)).getById(id);
            verify(mapper, times(1)).toInfoCarDto(car);
        }
    }

    @Nested
    class FindAllTest {

        @Test
        void shouldReturnListOfSizeOne() {
            // given
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            int expectedSize = 1;
            doReturn(List.of(car))
                    .when(carDAOproxy)
                    .findAll(0, 10);

            // when
            List<InfoCarDto> actual = carService.findAll(1, 10);

            // then
            assertThat(actual).hasSize(expectedSize);
        }

        @Test
        void shouldReturnListThatContainsExpectedResponse() {
            // given
            InfoCarDto expected = CarTestData.builder()
                    .build()
                    .buildInfoCarDto();
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();

            doReturn(expected)
                    .when(mapper)
                    .toInfoCarDto(car);
            doReturn(List.of(car))
                    .when(carDAOproxy)
                    .findAll(0, 10);

            // when
            List<InfoCarDto> actual = carService.findAll(1, 10);

            // then
            assertThat(actual.get(0)).isEqualTo(expected);
        }

        @Test
        void shouldReturnEmptyList() {
            // given
            doReturn(List.of())
                    .when(carDAOproxy)
                    .findAll(0, 10);

            // when
            List<InfoCarDto> actual = carService.findAll(1, 10);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class CreateTest {

        @ParameterizedTest
        @MethodSource("by.clevertec.service.impl.CarServiceImplTest#getArgumentsForCreateTest")
        void shouldCaptureValue(Car expected) {
            CarDto carDto = CarTestData.builder()
                    .build()
                    .buildCarDto();

            doReturn(expected)
                    .when(mapper)
                    .toCar(carDto);
            doReturn(expected)
                    .when(carDAOproxy)
                    .save(expected);

            carService.create(carDto);
            verify(carDAOproxy).save(captor.capture());

            Car captorCar = captor.getValue();
            assertThat(captorCar).isEqualTo(expected);
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void shouldUpdateCarWhenCarExists() {
            // given
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            UUID id = car.getId();
            CarDto carDto = CarTestData.builder()
                    .withName("БМВ")
                    .build().buildCarDto();
            Car updatedCar = CarTestData.builder()
                    .withName("БМВ")
                    .build()
                    .buildCar();

            when(carDAOproxy.getById(id)).thenReturn(Optional.of(car));
            when(mapper.merge(car, carDto)).thenReturn(updatedCar);

            // when
            carService.update(id, carDto);

            // then
            verify(carDAOproxy).update(captor.capture());
            Car capturedCar = captor.getValue();
            assertThat(capturedCar).isEqualTo(updatedCar);
        }

        @Test
        void shouldThrowExceptionWhenCarDoesNotExist() {
            // given
            UUID id = UUID.randomUUID();
            CarDto carDto = CarTestData.builder()
                    .build()
                    .buildCarDto();

            when(carDAOproxy.getById(id)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> carService.update(id, carDto))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Car with %s not found!");
        }
    }

    @Nested
    class DeleteTest {
        @Test
        void testShouldReturnExpectedResponse() {
            // given
            UUID id = UUID.randomUUID();

            // when
            carService.delete(id);

            // then
            verify(carDAOproxy).delete(id);
        }
    }

    private static Stream<Arguments> getArgumentsForCreateTest() {
        return Stream.of(
                Arguments.of(CarTestData.builder()
                        .withId(UUID.fromString("0338369d-dfde-4a5a-82cc-8d42aad7ed9d"))
                        .withName("МАЗ")
                        .withDescription("Почти автомобиль.")
                        .withPrice(BigDecimal.valueOf(15000))
                        .build()
                        .buildCar()),
                Arguments.of(CarTestData.builder()
                        .withId(UUID.fromString("e500e343-0d7d-450f-962d-ed2f98ed79b4"))
                        .withName("ЗАЗ")
                        .withDescription("Лучший автомобиль.")
                        .withPrice(BigDecimal.valueOf(10000))
                        .build()
                        .buildCar()));
    }
}