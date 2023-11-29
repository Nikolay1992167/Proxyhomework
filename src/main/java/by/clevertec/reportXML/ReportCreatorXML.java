package by.clevertec.reportXML;

import by.clevertec.entity.Car;
import by.clevertec.exception.XMLParserException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ReportCreatorXML {

    public static void writeCarToXml(Car car) {

        final String PATH_TO_FILE_XML = "src/main/java/by/clevertec/reportXML/report";

        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.registerModule(new JavaTimeModule());
            String uniqueId = UUID.randomUUID().toString();
            File file = new File(PATH_TO_FILE_XML + uniqueId + ".xml");
            xmlMapper.writeValue(file, car);
        } catch (IOException e) {
            throw new XMLParserException();
        }
    }
}
