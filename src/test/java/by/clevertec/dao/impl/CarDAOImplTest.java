package by.clevertec.dao.impl;

import by.clevertec.entity.Car;
import by.clevertec.exception.CarSQLException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.CarTestData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CarDAOImplTest {

    @InjectMocks
    private CarDAOImpl carDAO;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @Nested
    class GetByIdTest {

        @Test
        @SneakyThrows
        void shouldThrowJDBCConnectionExceptionWithExpectedMessage() {
            //given
            String sql = "SELECT * FROM public_car.cars WHERE id = ?";
            UUID id = UUID.fromString("33e4b6c3-c84d-47b7-ac0b-a9f8566d7950");
            String expectedMessage = "Неверный id";
            doThrow(new SQLException(expectedMessage))
                    .when(connection)
                    .prepareStatement(sql);

            // when
            Exception exception = assertThrows(CarSQLException.class, () -> carDAO.getById(id));
            String actualMessage = exception.getMessage();
            expectedMessage = "Error connecting to the database:" + expectedMessage;

            // then
            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @SneakyThrows
        void testShouldReturnExpectedResponse() {
            // given
            String sql = "SELECT * FROM public_car.cars WHERE id = ?";
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            Optional<Car> expected = Optional.of(car);
            UUID id = car.getId();

            doReturn(preparedStatement)
                    .when(connection)
                    .prepareStatement(sql);
            doNothing()
                    .when(preparedStatement)
                    .setObject(1, id);
            doReturn(resultSet)
                    .when(preparedStatement)
                    .executeQuery();
            doReturn(true)
                    .when(resultSet)
                    .next();
            getMockedCarFromResultSet(car);

            // when
            Optional<Car> actual = carDAO.getById(id);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class FindAllTest {
        @Test
        @SneakyThrows
        void shouldThrowJDBCConnectionExceptionWithExpectedMessage() {
            // given
            String sql = "SELECT * FROM public_car.cars ORDER BY id LIMIT ? OFFSET ?";
            String expectedMessage = "Проблема соединения с сервером!";

            doThrow(new SQLException(expectedMessage))
                    .when(connection)
                    .prepareStatement(sql);

            // when
            Exception exception = assertThrows(CarSQLException.class, () -> carDAO.findAll(1, 10));
            String actualMessage = exception.getMessage();
            expectedMessage = "Error connecting to the database:" + expectedMessage;

            // then
            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @SneakyThrows
        void shouldReturnListOfSizeOne() {
            // given
            String sql = "SELECT * FROM public_car.cars ORDER BY id LIMIT ? OFFSET ?";
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            int expectedSize = 1;

            doReturn(preparedStatement)
                    .when(connection)
                    .prepareStatement(sql);
            doReturn(resultSet)
                    .when(preparedStatement)
                    .executeQuery();
            doReturn(true, false)
                    .when(resultSet)
                    .next();
            getMockedCarFromResultSet(car);

            // when
            List<Car> actual = carDAO.findAll(1, 10);

            // then
            assertThat(actual).hasSize(expectedSize);
        }

        @Test
        @SneakyThrows
        void shouldReturnListThatContainsExpectedResponse() {
            // given
            String sql = "SELECT * FROM public_car.cars ORDER BY id LIMIT ? OFFSET ?";
            Car expected = CarTestData.builder()
                    .build()
                    .buildCar();

            doReturn(preparedStatement)
                    .when(connection)
                    .prepareStatement(sql);
            doReturn(resultSet)
                    .when(preparedStatement)
                    .executeQuery();
            doReturn(true, false)
                    .when(resultSet)
                    .next();
            getMockedCarFromResultSet(expected);

            // when
            List<Car> actual = carDAO.findAll(1, 10);

            // then
            assertThat(actual.get(0)).isEqualTo(expected);
        }

        @Test
        @SneakyThrows
        void shouldReturnEmptyList() {
            // given
            String sql = "SELECT * FROM public_car.cars ORDER BY id LIMIT ? OFFSET ?";

            doReturn(preparedStatement)
                    .when(connection)
                    .prepareStatement(sql);
            doReturn(resultSet)
                    .when(preparedStatement)
                    .executeQuery();
            doReturn(false)
                    .when(resultSet)
                    .next();

            // when
            List<Car> actual = carDAO.findAll(1, 10);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class SaveTest {

        @Test
        @SneakyThrows
        void shouldThrowJDBCConnectionExceptionWithExpectedMessage() {
            // given
            String sql = "INSERT INTO public_car.cars (name, description, price) VALUES (?,?,?)";
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            String expectedMessage = "Проблема соединения с сервером!";


            doThrow(new SQLException(expectedMessage))
                    .when(connection)
                    .prepareStatement(sql);
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // when
            Exception exception = assertThrows(CarSQLException.class, () -> carDAO.save(car));
            String actualMessage = exception.getMessage();
            expectedMessage = "Error connecting to the database:" + expectedMessage;

            // then
            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @SneakyThrows
        void shouldReturnExpectedResponse() {
            // given
            String sql = "INSERT INTO public_car.cars (name, description, price) VALUES (?,?,?)";
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();

            doReturn(preparedStatement)
                    .when(connection)
                    .prepareStatement(sql);
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setMockedCarInStatement(car);
            doReturn(1)
                    .when(preparedStatement)
                    .executeUpdate();
            doReturn(resultSet)
                    .when(preparedStatement)
                    .getGeneratedKeys();
            doReturn(true)
                    .when(resultSet)
                    .next();
            doReturn(car.getId().toString())
                    .when(resultSet)
                    .getString(1);

            // then
            Car actual = carDAO.save(car);

            // when
            assertThat(actual).isEqualTo(car);
        }
    }

    @Nested
    class UpdateTest {

        @Test
        @SneakyThrows
        void shouldThrowJDBCConnectionExceptionWithExpectedMessage() {
            // given
            String sql = "UPDATE public_car.cars SET name = ?, description = ?, price = ? WHERE id = ?";
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            String expectedMessage = "Проблема соединения с сервером!";

            doThrow(new SQLException(expectedMessage))
                    .when(connection)
                    .prepareStatement(sql);
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // when
            Exception exception = assertThrows(CarSQLException.class, () -> carDAO.update(car));
            String actualMessage = exception.getMessage();
            expectedMessage = "Error connecting to the database:" + expectedMessage;

            // then
            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @SneakyThrows
        void shouldReturnExpectedResponse() {
            // given
            String sql = "UPDATE public_car.cars SET name = ?, description = ?, price = ? WHERE id = ?";
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();

            doReturn(preparedStatement)
                    .when(connection)
                    .prepareStatement(sql);
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setMockedCarInStatement(car);
            doNothing()
                    .when(preparedStatement)
                    .setObject(4, car.getId());
            doReturn(1)
                    .when(preparedStatement)
                    .executeUpdate();
            doReturn(resultSet)
                    .when(preparedStatement)
                    .getGeneratedKeys();
            doReturn(true)
                    .when(resultSet)
                    .next();
            doReturn(car.getId().toString())
                    .when(resultSet)
                    .getString(1);

            // when
            Car actual = carDAO.update(car);

            // then
            assertThat(actual).isEqualTo(car);
        }
    }

    @Nested
    class DeleteTest {

        @Test
        @SneakyThrows
        void testShouldThrowJDBCConnectionExceptionWithExpectedMessage() {
            // given
            String sql = "DELETE FROM public_car.cars WHERE id = ?";
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            UUID id = car.getId();
            String expectedMessage = "Проблема соединения с сервером!";

            doThrow(new SQLException(expectedMessage))
                    .when(connection)
                    .prepareStatement(sql);

            // when
            Exception exception = assertThrows(CarSQLException.class, () -> carDAO.delete(id));
            String actualMessage = exception.getMessage();
            expectedMessage = "Error connecting to the database:" + expectedMessage;

            // then
            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @SneakyThrows
        void testShouldReturnExpectedResponse() {
            // given
            String sql = "DELETE FROM public_car.cars WHERE id = ?";
            Car car = CarTestData.builder()
                    .build()
                    .buildCar();
            UUID id = car.getId();

            doReturn(preparedStatement)
                    .when(connection)
                    .prepareStatement(sql);
            doNothing()
                    .when(preparedStatement)
                    .setObject(1, id);
            doReturn(1)
                    .when(preparedStatement)
                    .executeUpdate();

            // when
            carDAO.delete(id);

            // then
            verify(preparedStatement, times(1)).executeUpdate();
            verify(connection, times(1)).prepareStatement(sql);
        }
    }

    private void setMockedCarInStatement(Car car) throws SQLException {
        doNothing()
                .when(preparedStatement)
                .setString(1, car.getName());
        doNothing()
                .when(preparedStatement)
                .setString(2, car.getDescription());
        doNothing()
                .when(preparedStatement)
                .setBigDecimal(3, car.getPrice());
    }

    private void getMockedCarFromResultSet(Car car) throws SQLException {
        doReturn(car.getId().toString())
                .when(resultSet)
                .getString("id");
        doReturn(car.getName())
                .when(resultSet)
                .getString("name");
        doReturn(car.getDescription())
                .when(resultSet)
                .getString("description");
        doReturn(car.getPrice())
                .when(resultSet)
                .getBigDecimal("price");
        doReturn(Timestamp.valueOf(car.getCreated()))
                .when(resultSet)
                .getTimestamp("created");
    }
}