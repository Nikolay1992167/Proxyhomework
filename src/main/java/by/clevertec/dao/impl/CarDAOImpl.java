package by.clevertec.dao.impl;

import by.clevertec.dao.CarDAO;
import by.clevertec.entity.Car;
import by.clevertec.exception.CarSQLException;
import by.clevertec.proxy.annotations.ReflectionCheck;
import by.clevertec.util.ConnectionManager;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CarDAOImpl implements CarDAO {

    private final Connection connection;

    /**
     * Find in memory car by id
     *
     * @param id car id
     * @return Optional<Product>  if found, otherwise  Optional.empty()
     */
    @Override
    @ReflectionCheck
    public Optional<Car> getById(UUID id) {

        String sql = "SELECT * FROM public_car.cars WHERE id = ?";
        Optional<Car> car = Optional.empty();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, (id));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    car = Optional.of(getCarFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new CarSQLException(e.getMessage());
        }

        return car;
    }

    /**
     * Find all cars in memory
     *
     * @return list of found cars
     */
    @Override
    public List<Car> findAll(Integer pageNumber, Integer pageSize) {

        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM public_car.cars ORDER BY id LIMIT ? OFFSET ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, pageNumber);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Car car = getCarFromResultSet(resultSet);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new CarSQLException(e.getMessage());
        }

        return cars;
    }

    /**
     * Save car in memory
     *
     * @param car saved car
     * @return saved car in memory
     */
    @Override
    @ReflectionCheck
    public Car save(Car car) {

        String sql = "INSERT INTO public_car.cars (name, description, price) VALUES (?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setCarValuesInStatement(statement, car);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString(1));
                car.setId(id);
            }
        } catch (SQLException e) {

            throw new CarSQLException(e.getMessage());
        }

        return car;
    }

    /**
     * Update car in memory
     *
     * @param car update car
     * @return update car in memory
     */
    @Override
    @ReflectionCheck
    public Car update(Car car) {

        String sql = "UPDATE public_car.cars SET name = ?, description = ?, price = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setCarValuesInStatement(statement, car);
            statement.setObject(4, car.getId());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString(1));
                car.setId(id);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new CarSQLException(e.getMessage());
        }

        return car;
    }

    /**
     * Delete car for memory by id
     *
     * @param id car id
     */
    @Override
    @ReflectionCheck
    public void delete(UUID id) {

        String sql = "DELETE FROM public_car.cars WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());

            throw new CarSQLException(e.getMessage());
        }
    }

    private Car getCarFromResultSet(ResultSet resultSet) throws SQLException {

        return Car.builder()
                .id(UUID.fromString(resultSet.getString("id")))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .price(resultSet.getBigDecimal("price"))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .build();
    }

    private void setCarValuesInStatement(PreparedStatement statement, Car car) throws SQLException {
        statement.setString(1, car.getName());
        statement.setString(2, car.getDescription());
        statement.setBigDecimal(3, car.getPrice());
    }
}
