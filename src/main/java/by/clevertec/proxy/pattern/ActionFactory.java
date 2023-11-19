package by.clevertec.proxy.pattern;

import by.clevertec.cach.Cache;
import by.clevertec.dao.CarDAO;
import by.clevertec.entity.Car;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static by.clevertec.reportXML.ReportCreatorXML.writeCarToXml;

@Slf4j
public class ActionFactory{

    private final Map<String, Action> actionMap = new HashMap<>();
    private final CarDAO target;
    private final Cache<UUID, Car> cache;

    public ActionFactory(CarDAO target, Cache<UUID, Car> cache) {
        this.target = target;
        this.cache = cache;
        actionMap.put("getById", this::getById);
        actionMap.put("save", this::save);
        actionMap.put("update", this::update);
        actionMap.put("delete", this::delete);
    }

    public Action getAction(String actionName) {
        Action action = actionMap.get(actionName);
        if (action == null) {
            throw new IllegalArgumentException("Неподдерживаемый метод: " + actionName);
        }
        return action;
    }

    public Optional<Car> getById(Object[] args) {

        UUID id = (UUID) args[0];
        Car car = cache.get(id);
        if (car != null) {
            log.info("Взято из кэша: {}", car);
            return Optional.of(car);
        } else {
            Optional<Car> result = target.getById(id);
            result.ifPresent(value -> {
                cache.put(id, value);
                writeCarToXml(value);
                log.info("Добавлено в кэш: {}", value);
            });
            return result;
        }
    }

    public Car save(Object[] args) {

        Car carToSave = (Car) args[0];
        Car savedCar = target.save(carToSave);
        cache.put(savedCar.getId(), savedCar);
        writeCarToXml(savedCar);
        log.info("Добавлено в кэш: {}", savedCar);
        return savedCar;
    }

    public Car update(Object[] args) {

        Car carToUpdate = (Car) args[0];
        Car updatedCar = target.update(carToUpdate);
        cache.put(updatedCar.getId(), updatedCar);
        log.info("Обновлено в кэше: {}", updatedCar);
        return updatedCar;
    }

    public Car delete(Object[] args) {

        cache.delete((UUID) args[0]);
        log.info("Удалено из кэша: {}", args[0]);
        target.delete((UUID) args[0]);
        return null;
    }
}
