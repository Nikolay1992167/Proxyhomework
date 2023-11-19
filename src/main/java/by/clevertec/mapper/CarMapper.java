package by.clevertec.mapper;

import by.clevertec.dto.CarDto;
import by.clevertec.dto.InfoCarDto;
import by.clevertec.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CarMapper {

    /**
     * Маппит DTO в car без UUID
     *
     * @param carDto - DTO для маппинга
     * @return новый car
     */
    Car toCar(CarDto carDto);

    /**
     * Маппит текущий продукт в DTO без даты
     *
     * @param car - существующий car
     * @return DTO с идентификатором
     */
    InfoCarDto toInfoCarDto(Car car);

    /**
     * Сливает существующий car с информацией из DTO
     * не меняет дату создания и идентификатор
     *
     * @param car    существующий car
     * @param carDto информация для обновления
     * @return обновлённый car
     */
    Car merge(@MappingTarget Car car, CarDto carDto);
}
