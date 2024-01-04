package by.clevertec.mapper;

import by.clevertec.dto.CarDto;
import by.clevertec.dto.InfoCarDto;
import by.clevertec.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CarMapper {

    /**
     * Mappit DTO in a car without ID
     *
     * @param carDto - DTO for mapper
     * @return a new car
     */
    Car toCar(CarDto carDto);

    /**
     * Mappit current car in DTO without date
     *
     * @param car - existing car
     * @return DTO with ID
     */
    InfoCarDto toInfoCarDto(Car car);

    /**
     * Merges an existing car with information from the DTO
     * does not change the creation date and ID
     *
     * @param car    existing car
     * @param carDto information for update
     * @return updated car
     */
    Car merge(@MappingTarget Car car, CarDto carDto);
}
