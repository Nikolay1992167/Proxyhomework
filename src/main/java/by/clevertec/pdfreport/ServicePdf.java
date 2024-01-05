package by.clevertec.pdfreport;

import by.clevertec.entity.Car;

import java.nio.file.Path;

public interface ServicePdf {

    Path createReportPdf(String cache, String action, Car car, String cacheAction);
}
