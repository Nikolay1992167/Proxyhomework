package by.clevertec.pdfreport;

import by.clevertec.entity.Car;
import by.clevertec.pdfreport.formattedtext.TextTemplate;
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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static by.clevertec.constants.Constants.FONT;

@Slf4j
public class ServicePdfImpl implements ServicePdf {

    @Override
    public void createReportPdf(String cache, String action, Car car, String cacheAction) {

        TextTemplate informationService = new TextTemplate();
        StringBuilder reportHeader = informationService.createCacheReportHeader(cache, action, cacheAction);
        StringBuilder reportBody = informationService.createCacheReportBody(car);
        Result result = new Result(reportHeader, reportBody);

        String report = result.reportHeader().append(result.reportBody()).toString();
        Path path = getPath();
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(path));
            document.open();

            PdfReader reader = new PdfReader("src/main/resources/pdf/Clevertec_Template.pdf");
            PdfImportedPage page = writer.getImportedPage(reader, 1);

            PdfContentByte contentByte = writer.getDirectContent();
            contentByte.addTemplate(page, 0, 0);
            Font font = FontFactory.getFont(FONT, "cp1251", BaseFont.EMBEDDED, 8);
            Paragraph paragraph = new Paragraph(30, report, font);
            paragraph.setSpacingAfter(0f);
            Paragraph empty = new Paragraph("\n".repeat(14));

            document.add(empty);
            document.add(paragraph);

            document.close();
            writer.close();

            log.info("createPdf {}", path);
        } catch (DocumentException | IOException e) {
            log.error(e.getMessage());
        }
    }

    private record Result(StringBuilder reportHeader, StringBuilder reportBody) {
    }

    private static Path getPath() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMHHmm");
        String dateTime = LocalDateTime.now().format(formatter);
        return Paths.get("src/main/resources/pdf/reportPDF" + dateTime + ".pdf");
    }
}
