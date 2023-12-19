package by.clevertec.service.impl;

import by.clevertec.dao.CarDAO;
import by.clevertec.dto.CarDto;
import by.clevertec.dto.InfoCarDto;
import by.clevertec.entity.Car;
import by.clevertec.exception.NotFoundException;
import by.clevertec.mapper.CarMapper;
import by.clevertec.service.CarService;
import by.clevertec.util.PageChecker;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarMapper carMapper;
    private final CarDAO carDAO;

    /**
     * Find a car by ID
     *
     * @param id car ID
     * @return found car
     * @throws NotFoundException if not find
     */
    @Override
    public InfoCarDto findById(UUID id) {
        Car car = carDAO.getById(id)
                .orElseThrow(() -> new NotFoundException("Car with " + id + " not found!"));
        return carMapper.toInfoCarDto(car);
    }

    /**
     * Returns all existing cars
     *
     * @return list of information by car
     */
    @Override
    public List<InfoCarDto> findAll(Integer pageNumber, Integer pageSize) {
        int offset = PageChecker.checkPage(pageNumber, pageSize);
        List<Car> cars = carDAO.findAll(offset, pageSize);
        return cars.stream()
                .map(carMapper::toInfoCarDto)
                .toList();
    }

    /**
     * Create new car from DTO
     *
     * @param carDto DTO with information about creat
     */
    @Override
    public UUID create(CarDto carDto) {
        Car carToSave = carMapper.toCar(carDto);
        Car car = carDAO.save(carToSave);
        return car.getId();
    }

    /**
     * Update also existing car from the information received in DTO
     *
     * @param id     ID of car for update
     * @param carDto DTO with information for update
     */
    @Override
    public void update(UUID id, CarDto carDto) {
        carDAO.getById(id).ifPresentOrElse(
                car -> {
                    Car updatedCar = carMapper.merge(car, carDto);
                    carDAO.update(updatedCar);
                },
                () -> {
                    throw new NotFoundException("Car with %s not found!");
                }
        );
    }

    /**
     * Delete existing car
     *
     * @param id ID car for delete
     */
    @Override
    public void delete(UUID id) {
        carDAO.delete(id);
    }
}
