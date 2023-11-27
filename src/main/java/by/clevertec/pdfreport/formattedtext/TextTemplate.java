package by.clevertec.pdfreport.formattedtext;

import by.clevertec.entity.Car;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TextTemplate {

    public StringBuilder createCacheReportHeader(String cache, String action, String cacheAction) {
        return new StringBuilder("""
                Отчёт о выполненных операциях
                Дата: %s Время: %s
                Алгоритм кэширования: %s
                Выполняемый метод: %s
                Логика работы с кэш: %s
                %s
                %-70s %-10s %-40s %-12s %-9s
                """.formatted(
                LocalDate.now(),
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                cache,
                action,
                cacheAction,
                "-".repeat(150),
                "ID",
                "NAME",
                "DESCRIPTION",
                "PRICE",
                "DATE CREATED"
        ));
    }

    public StringBuilder createCacheReportBody(Car car) {
        return new StringBuilder("""
                %-34s  | %-10s | %-30s | %-10s | %-5s
                """.formatted(
                car.getId(),
                car.getName(),
                car.getDescription(),
                car.getPrice(),
                car.getCreated() != null ? car.getCreated().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")) : "null"
        ));
    }
}
