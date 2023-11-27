package by.clevertec.pdfreport;

import by.clevertec.entity.Car;

public interface ServicePdf {

    void createReportPdf(String cache, String action, Car car, String cacheAction);
}
