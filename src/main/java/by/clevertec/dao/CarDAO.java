package by.clevertec.dao;

import by.clevertec.entity.Car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarDAO {

    Optional<Car> getById(UUID id);

    List<Car> findAll();

    Car save(Car car);

    Car update(Car car);

    void delete(UUID id);
}
