package by.clevertec.controller;

import by.clevertec.dto.InfoCarDto;
import by.clevertec.exception.NotFoundException;
import by.clevertec.pdfreport.ServiceCreateFileInfPdf;
import by.clevertec.pdfreport.impl.ServiceCreateFileInfPdfImpl;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@WebServlet("/generation")
@Component
public class GenerationFilePdfServlet extends AbstractCarServlet {
    private final ServiceCreateFileInfPdf informationService = context.getBean(ServiceCreateFileInfPdfImpl.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String id = req.getParameter("id");

        if (id == null) {
            throw new NotFoundException("ID not provided!");
        }

        InfoCarDto carDto = carService.findById(UUID.fromString(id));

        if (carDto != null) {
            String carDtoToJson = gson.toJson(carDto);
            informationService.creatFilePdfWithInfAboutCar(carDtoToJson);
        } else {

            throw new NotFoundException("The requested object was not found!");
        }
    }
}

