package by.clevertec.controller;

import by.clevertec.dto.InfoCarDto;
import by.clevertec.exception.NotFoundException;
import by.clevertec.pdfreport.ServiceCreateFileInfPdf;
import by.clevertec.pdfreport.impl.ServiceCreateFileInfPdfImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@WebServlet("/generate")
public class GenerationFilePdfServlet extends AbstractCarServlet {
    private final ServiceCreateFileInfPdf information = new ServiceCreateFileInfPdfImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String id = req.getParameter("id");

        if (id == null) {
            throw new NotFoundException("ID not provided!");
        }

        InfoCarDto carDto = carService.findById(UUID.fromString(id));

        if (carDto != null) {
            String carDtoToJson = gson.toJson(carDto);
            information.creatFilePdfWithInfAboutCar(carDtoToJson);
        } else {

            throw new NotFoundException("The requested object was not found!");
        }
    }
}

