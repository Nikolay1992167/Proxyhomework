package by.clevertec;

import by.clevertec.dao.CarDAO;
import by.clevertec.dao.impl.CarDAOImpl;
import by.clevertec.db.DBRunner;
import by.clevertec.dto.CarDto;
import by.clevertec.dto.InfoCarDto;
import by.clevertec.mapper.CarMapper;
import by.clevertec.mapper.CarMapperImpl;
import by.clevertec.proxy.MyInvocationHandler;
import by.clevertec.service.CarService;
import by.clevertec.service.impl.CarServiceImpl;
import by.clevertec.util.Validation;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        extracted();

//        System.out.println("Проверка выполнения программы");
//        startApplication("", "", "");
    }

    private static void startApplication(String idForFindMethod, String idForUpdateMethod, String idForDeleteMethod) {
        CarDto createCar = new CarDto("Ладушка", "Почти хороший автомобиль!", BigDecimal.valueOf(105000));
        CarDto updateCar = new CarDto("Москвич", "Был хорошим авто наверное!", BigDecimal.valueOf(105000));

        CarDAO carDAO = new CarDAOImpl();
        CarMapper carMapper = new CarMapperImpl();

        MyInvocationHandler handler = new MyInvocationHandler(carDAO);
        CarDAO proxy = (CarDAO) Proxy.newProxyInstance(MyInvocationHandler.class.getClassLoader(), new Class<?>[] {CarDAO.class}, handler);

        Validation validation = new Validation();
        validation.validate(createCar);
        CarService service = new CarServiceImpl(carMapper,proxy);
        service.create(createCar);
        List<InfoCarDto> all = service.findAll();
        System.out.println(all);
        service.findById(UUID.fromString(idForFindMethod));
        service.update(UUID.fromString(idForUpdateMethod), updateCar);
        service.delete(UUID.fromString(idForDeleteMethod));
    }

    private static void extracted() {
        DBRunner.runSqlScripts();
    }
}