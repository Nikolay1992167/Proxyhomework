package by.clevertec.proxy.factory;

import by.clevertec.cach.Cache;
import by.clevertec.dao.CarDAO;
import by.clevertec.entity.Car;
import by.clevertec.pdfreport.ServicePdf;
import by.clevertec.pdfreport.impl.ServicePdfImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class ActionFactory {

    private final Map<String, Action> actionMap = new HashMap<>();

    private final CarDAO target;

    @Autowired
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
            throw new IllegalArgumentException("Unsupported method: " + actionName);
        }
        return action;
    }

    public Optional<Car> getById(Object[] args) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        UUID id = (UUID) args[0];
        Car car = cache.get(id);
        if (car != null) {
            log.info(DescriptionAction.VALUE_GET_FROM_CACHE.getValue() + ": {}", car);
            servicePdf.createReportPdf(
                    getAlgorithmCache(),
                    methodName,
                    car,
                    DescriptionAction.VALUE_GET_FROM_CACHE.getValue());
            return Optional.of(car);
        } else {
            Optional<Car> result = target.getById(id);
            result.ifPresent(value -> {
                cache.put(id, value);
                servicePdf.createReportPdf(
                        getAlgorithmCache(),
                        methodName,
                        value,
                        DescriptionAction.VALUE_GET_FROM_BD.getValue() + " и " + DescriptionAction.VALUE_ADD_IN_CACHE.getValue());
                log.info(DescriptionAction.VALUE_ADD_IN_CACHE.getValue() + ": {}", value);
            });
            return result;
        }
    }

    public Car save(Object[] args) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        Car carToSave = (Car) args[0];
        Car savedCar = target.save(carToSave);
        servicePdf.createReportPdf(
                getAlgorithmCache(),
                methodName,
                savedCar,
                DescriptionAction.VALUE_ADD_IN_BD.getValue());
        log.info(DescriptionAction.VALUE_ADD_IN_CACHE.getValue() + ": {}", savedCar);
        return savedCar;
    }

    public Car update(Object[] args) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        Car carToUpdate = (Car) args[0];
        Car updatedCar = target.update(carToUpdate);
        cache.put(updatedCar.getId(), updatedCar);
        servicePdf.createReportPdf(
                getAlgorithmCache(),
                methodName,
                updatedCar,
                DescriptionAction.VALUE_UPDATE_IN_CACHE.getValue());
        log.info(DescriptionAction.VALUE_UPDATE_IN_CACHE.getValue() + ": {}", updatedCar);
        return updatedCar;
    }

    public Car delete(Object[] args) {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        UUID id = (UUID) args[0];
        Car deletedCar = cache.get(id);
        if (deletedCar != null) {
            cache.delete(id);
            log.info(DescriptionAction.VALUE_DELETE_IN_CACHE.getValue() + ": {}", id);
            servicePdf.createReportPdf(
                    getAlgorithmCache(),
                    methodName,
                    deletedCar,
                    DescriptionAction.VALUE_DELETE_IN_CACHE.getValue());
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
