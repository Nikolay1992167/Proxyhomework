package by.clevertec.proxy.factory;

import by.clevertec.cach.Cache;
import by.clevertec.dao.CarDAO;
import by.clevertec.entity.Car;
import by.clevertec.pdfreport.ServicePdf;
import by.clevertec.pdfreport.ServicePdfImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static by.clevertec.constants.Constants.VALUE_ADD_IN_CACHE;
import static by.clevertec.constants.Constants.VALUE_DELETE_IN_CACHE;
import static by.clevertec.constants.Constants.VALUE_GET_FROM_BD;
import static by.clevertec.constants.Constants.VALUE_GET_FROM_CACHE;
import static by.clevertec.constants.Constants.VALUE_UPDATE_IN_CACHE;
import static by.clevertec.reportXML.ReportCreatorXML.writeCarToXml;

@Slf4j
public class ActionFactory {

    private final Map<String, Action> actionMap = new HashMap<>();
    private final CarDAO target;
    private final Cache<UUID, Car> cache;

    private final ServicePdf servicePdf = new ServicePdfImpl();

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

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        UUID id = (UUID) args[0];
        Car car = cache.get(id);
        if (car != null) {
            log.info(VALUE_GET_FROM_CACHE + ": {}", car);
            servicePdf.createReportPdf(getAlgorithmCache(), methodName, car, VALUE_GET_FROM_CACHE);
            return Optional.of(car);
        } else {
            Optional<Car> result = target.getById(id);
            result.ifPresent(value -> {
                cache.put(id, value);
                servicePdf.createReportPdf(getAlgorithmCache(), methodName, value, VALUE_GET_FROM_BD);
                writeCarToXml(value);
                log.info(VALUE_ADD_IN_CACHE + ": {}", value);
            });
            return result;
        }
    }

    public Car save(Object[] args) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        Car carToSave = (Car) args[0];
        Car savedCar = target.save(carToSave);
        cache.put(savedCar.getId(), savedCar);
        servicePdf.createReportPdf(getAlgorithmCache(), methodName, savedCar, VALUE_ADD_IN_CACHE);
        writeCarToXml(savedCar);
        log.info(VALUE_ADD_IN_CACHE + ": {}", savedCar);
        return savedCar;
    }

    public Car update(Object[] args) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        Car carToUpdate = (Car) args[0];
        Car updatedCar = target.update(carToUpdate);
        cache.put(updatedCar.getId(), updatedCar);
        servicePdf.createReportPdf(getAlgorithmCache(), methodName, updatedCar, VALUE_UPDATE_IN_CACHE);
        log.info(VALUE_UPDATE_IN_CACHE + ": {}", updatedCar);
        return updatedCar;
    }

    public Car delete(Object[] args) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        UUID id = (UUID) args[0];
        Car deletedCar = cache.get(id);
        if (deletedCar != null) {
            cache.delete(id);
            log.info(VALUE_DELETE_IN_CACHE + ": {}", id);
            servicePdf.createReportPdf(getAlgorithmCache(), methodName, deletedCar, VALUE_DELETE_IN_CACHE);
        }
        target.delete(id);
        return null;
    }

    private String getAlgorithmCache() {
        String className = cache.getClass().getName();
        String shortName = className.substring(className.lastIndexOf('.') + 1);
        return shortName.substring(0, shortName.indexOf("Cache"));
    }
}
