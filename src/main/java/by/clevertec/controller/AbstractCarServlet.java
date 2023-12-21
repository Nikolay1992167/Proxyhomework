package by.clevertec.controller;

import by.clevertec.adapter.LocalDateTimeAdapter;
import by.clevertec.mapper.CarMapper;
import by.clevertec.mapper.CarMapperImpl;
import by.clevertec.service.CarService;
import by.clevertec.service.impl.CarServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServlet;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public abstract class AbstractCarServlet extends HttpServlet {

    protected final CarMapper carMapper = new CarMapperImpl();
    protected final CarService carService = new CarServiceImpl(carMapper);
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    protected void printJson(Object object, PrintWriter printWriter) {
        String json = gson.toJson(object);

        printWriter.print(json);
        printWriter.flush();
    }
}
