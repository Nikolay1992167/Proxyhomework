package by.clevertec.controller;

import by.clevertec.dto.CarDto;
import by.clevertec.dto.InfoCarDto;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@WebServlet("/cars")
public class CarServlet extends AbstractCarServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String id = req.getParameter("id");
        String pageNumber = req.getParameter("pageNumber");
        String pageSize = req.getParameter("pageSize");
        PrintWriter printWriter = resp.getWriter();

        if (id != null) {
            findById(id, printWriter);

        } else {
            findAll(pageNumber, pageSize, printWriter);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        BufferedReader reader = req.getReader();
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        CarDto carDto = gson.fromJson(result.toString(), CarDto.class);

        String carDtoToJson = gson.toJson(carDto);

        PrintWriter printWriter = resp.getWriter();
        resp.setStatus(201);
        printWriter.print(carDtoToJson);
        printWriter.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        BufferedReader reader = req.getReader();
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        String id = req.getParameter("id");

        CarDto carDto = gson.fromJson(result.toString(), CarDto.class);
        carService.update(UUID.fromString(id), carDto);

        PrintWriter printWriter = resp.getWriter();
        String carDtoToJson = gson.toJson(carDto);
        printWriter.print(carDtoToJson);
        printWriter.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {

        String id = req.getParameter("id");

        carService.delete(UUID.fromString(id));
        resp.setStatus(204);
    }

    private void findById(String id, PrintWriter printWriter) {

        InfoCarDto carDto = carService.findById(UUID.fromString(id));

        printJson(carDto, printWriter);
    }


    private void findAll(String pageNumber, String pageSize, PrintWriter printWriter) {

        if (pageNumber == null || pageNumber.isEmpty()) {
            pageNumber = "1";
        }

        if (pageSize == null || pageSize.isEmpty()) {
            pageSize = "20";
        }

        List<InfoCarDto> cars = carService.findAll(Integer.valueOf(pageNumber), Integer.valueOf(pageSize));

        printJson(cars, printWriter);
    }
}
