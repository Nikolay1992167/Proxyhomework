package by.clevertec.controller;

import by.clevertec.config.SpringConfig;
import by.clevertec.service.CarService;
import by.clevertec.service.impl.CarServiceImpl;
import com.google.gson.Gson;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import java.io.PrintWriter;

@Component
public abstract class AbstractCarServlet extends HttpServlet {

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

    protected final CarService carService = context.getBean(CarServiceImpl.class);

    protected final Gson gson = context.getBean(Gson.class);

    protected void printJson(Object object, PrintWriter printWriter) {
        String json = gson.toJson(object);

        printWriter.print(json);
        printWriter.flush();
    }
}
