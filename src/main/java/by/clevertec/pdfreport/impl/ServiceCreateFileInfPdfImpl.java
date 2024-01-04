package by.clevertec.pdfreport.impl;

import by.clevertec.exception.PDFException;
import by.clevertec.pdfreport.ServiceCreateFileInfPdf;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ServiceCreateFileInfPdfImpl implements ServiceCreateFileInfPdf {

    @Override
    public void creatFilePdfWithInfAboutCar(String response) {

        PathResult pathResult = findPaths();
        Path path = Paths.get(pathResult.pdf());

        try {
            Document document = new Document(PageSize.A4, 50, 40, 200, 70);

            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(path));
            document.open();

            PdfReader reader = new PdfReader(pathResult.template());
            PdfImportedPage page = writer.getImportedPage(reader, 1);

            PdfContentByte contentByte = writer.getDirectContent();
            contentByte.addTemplate(page, 0, 0);

            Paragraph paragraph = new Paragraph(30, processString(response), setsFont());
            Paragraph empty = new Paragraph("\n".repeat(8));

            document.add(empty);
            document.add(paragraph);

            document.close();
            writer.close();

        } catch (DocumentException | IOException e) {

            throw new PDFException();
        }
    }

    private static PathResult findPaths() {

        URL pdfURL = ServiceCreateFileInfPdfImpl.class.getResource("/pdf");
        URL templateURL = ServiceCreateFileInfPdfImpl.class.getResource("/pdf/Clevertec_Template.pdf");

        int randomNum = new Random().nextInt(900) + 100;

        String pdf = URLDecoder.decode(pdfURL.getPath(), StandardCharsets.UTF_8)
                .concat("ObjectCarFromResponse" + randomNum + ".pdf")
                .substring(1);
        String template = URLDecoder.decode(templateURL.getPath(), StandardCharsets.UTF_8);

        return new PathResult(pdf, template);
    }

    public record PathResult(String pdf, String template) {
    }

    private Font setsFont() {

        URL fontURL = ServiceCreateFileInfPdfImpl.class.getResource("/pdf/lato-light.ttf");
        String font = URLDecoder.decode(fontURL.getPath(), StandardCharsets.UTF_8);

        return FontFactory.getFont(font, "cp1251", BaseFont.EMBEDDED, 16);
    }

    private String processString(String input) {
        return Arrays.stream(input
                        .replace("{", "")
                        .replace("}", "")
                        .replace("\"", "")
                        .split(","))
                .map(String::trim)
                .collect(Collectors.joining("\n", "Информация об объекте:\n", ""));
    }
}