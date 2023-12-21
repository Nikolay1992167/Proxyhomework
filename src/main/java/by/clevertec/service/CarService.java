package by.clevertec.service;

import by.clevertec.dto.CarDto;
import by.clevertec.dto.InfoCarDto;

import java.util.List;
import java.util.UUID;

public interface CarService {

    InfoCarDto findById(UUID id);

    List<InfoCarDto> findAll(Integer pageNumber, Integer pageSize);

    UUID create(CarDto carDto);

    void update(UUID id, CarDto carDto);

    void delete(UUID id);
}
