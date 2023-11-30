package by.clevertec.dao.impl;

import by.clevertec.dao.CarDAO;
import by.clevertec.entity.Car;
import by.clevertec.exception.JDBCConnectionException;

import by.clevertec.exception.ResourceSqlException;
import by.clevertec.proxy.annotations.MyAnnotation;
import by.clevertec.util.ConnectionManager;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class CarDAOImpl implements CarDAO {

    private final Connection connection;

    public CarDAOImpl() {
        connection = ConnectionManager.getJDBCConnection();
    }

    /**
     * Find in memory car by id
     *
     * @param id car id
     * @return Optional<Product>  if found, otherwise  Optional.empty()
     */
    @Override
    @MyAnnotation
    public Optional<Car> getById(UUID id) {
        String sql = "SELECT * FROM cars WHERE id = ?";
        Optional<Car> car = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, (id));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    car = Optional.of(getCarFromResultSet(resultSet));
                }
            } catch (SQLException e) {
                throw new ResourceSqlException();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JDBCConnectionException(e.getMessage());
        }
        return car;
    }

    /**
     * Find all cars in memory
     *
     * @return list of found cars
     */
    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Car car = getCarFromResultSet(resultSet);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JDBCConnectionException(e.getMessage());
        }
        return cars;
    }

    /**
     * Save car in memory
     *
     * @param car saved car
     * @return saved car in memory
     * @throws IllegalArgumentException if supplied car null
     */
    @Override
    @MyAnnotation
    public Car save(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        String sql = "INSERT INTO cars (name, description, price) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setCarValuesInStatement(statement, car);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString(1));
                car.setId(id);
            }
        } catch (SQLException e) {
            throw new JDBCConnectionException(e.getMessage());
        }
        return car;
    }

    /**
     * Update car in memory
     *
     * @param car update car
     * @return update car in memory
     * @throws IllegalArgumentException if otherwise car null
     */
    @Override
    @MyAnnotation
    public Car update(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        String sql = "UPDATE cars SET name = ?, description = ?, price = ? WHERE id = ?";
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
            throw new JDBCConnectionException(e.getMessage());
        }
        return car;
    }

    /**
     * Delete car for memory by id
     *
     * @param id car id
     */
    @Override
    @MyAnnotation
    public void delete(UUID id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JDBCConnectionException(e.getMessage());
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
