package by.clevertec.service.impl;

import by.clevertec.dao.CarDAO;
import by.clevertec.dto.CarDto;
import by.clevertec.dto.InfoCarDto;
import by.clevertec.entity.Car;
import by.clevertec.exception.NotFoundException;
import by.clevertec.mapper.CarMapper;
import by.clevertec.service.CarService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarMapper carMapper;
    private final CarDAO carDAO;

    /**
     * Ищет car по идентификатору
     *
     * @param id идентификатор car
     * @return найденный car
     * @throws NotFoundException если не найден
     */
    @Override
    public InfoCarDto findById(UUID id) {
        Car car = carDAO.getById(id)
                .orElseThrow(() -> new NotFoundException("Автомобиль с " + id + " не найден!"));
        return carMapper.toInfoCarDto(car);
    }

    /**
     * Возвращает все существующие cars
     *
     * @return лист с информацией о car
     */
    @Override
    public List<InfoCarDto> findAll() {
        List<Car> cars = carDAO.findAll();
        return cars.stream()
                .map(carMapper::toInfoCarDto)
                .toList();
    }

    /**
     * Создаёт новый car из DTO
     *
     * @param carDto DTO с информацией о создании
     */
    @Override
    public UUID create(CarDto carDto) {
        Car carToSave = carMapper.toCar(carDto);
        Car car = carDAO.save(carToSave);
        return car.getId();
    }

    /**
     * Обновляет уже существующий car из информации полученной в DTO
     *
     * @param id     идентификатор car для обновления
     * @param carDto DTO с информацией об обновлении
     */
    @Override
    public void update(UUID id, CarDto carDto) {
        carDAO.getById(id).ifPresentOrElse(
                car -> {
                    Car updatedCar = carMapper.merge(car, carDto);
                    carDAO.update(updatedCar);
                },
                () -> {
                    throw new NotFoundException("Автомобиль с %s не найден");
                }
        );
    }

    /**
     * Удаляет существующий car
     *
     * @param id идентификатор car для удаления
     */
    @Override
    public void delete(UUID id) {
        carDAO.delete(id);
    }
}
