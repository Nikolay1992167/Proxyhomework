package by.clevertec.pdfreport;

import by.clevertec.entity.Car;
import by.clevertec.exception.PDFCreatingException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.Stream;

public class ServicePdfImpl implements ServicePdf {

    @Override
    public Path createReportPdf(String cache, String action, Car car, String cacheAction) {

        final String titleReport = "Отчёт о выполненной операции";
        final String backgroundInformation = "Дата: %s Время: %s\nАлгоритм кэширования: %s\nВыполняемый метод: %s\nОписание поставщика объекта: %s\n%s"
                .formatted(
                        LocalDate.now(),
                        LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        cache,
                        action,
                        cacheAction,
                        "-".repeat(110));

        Path path = getPath();
        Document document = null;
        PdfWriter writer = null;
        try {
            document = new Document(PageSize.A4, 50, 40, 200, 70);
            writer = PdfWriter.getInstance(document, Files.newOutputStream(path));

            document.open();
            addBackgroundForFile(writer);

            Paragraph title = new Paragraph(30, titleReport, setsFont(12));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph information = new Paragraph(30, backgroundInformation, setsFont(12));
            document.add(information);

            PdfPTable table = new PdfPTable(6);
            table.setTotalWidth(520);
            table.setLockedWidth(true);

            float[] columnWidths = {2f, 10f, 8f, 25f, 7f, 9f};
            table.setWidths(columnWidths);

            Stream.of("№", "ID", "NAME", "DESCRIPTION", "PRICE", "DATE CREATED")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBorder(Rectangle.NO_BORDER);
                        header.setPhrase(new Phrase(columnTitle, setsFont(10)));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(header);
                    });

            table.addCell(new PdfPCell(new Phrase("1", setsFont(10))));
            table.addCell(new PdfPCell(new Phrase(car.getId() != null ? car.getId().toString() : "не установлен", setsFont(10))));
            table.addCell(new PdfPCell(new Phrase(car.getName(), setsFont(10))));
            table.addCell(new PdfPCell(new Phrase(car.getDescription(), setsFont(10))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(car.getPrice()), setsFont(10))));
            table.addCell(new PdfPCell(new Phrase(car.getCreated() != null ? car.getCreated().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")) : "не установлена", setsFont(10))));
            document.add(table);

        } catch (DocumentException | IOException e) {
            throw new PDFCreatingException();
        } finally {
            if (document != null) {
                document.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
        return path;
    }

    private Font setsFont(int size) {

        final String FONT = "src/main/resources/lato-light.ttf";
        final String ENCODING = "cp1251";
        return FontFactory.getFont(FONT, ENCODING, BaseFont.EMBEDDED, size);
    }

    private void addBackgroundForFile(PdfWriter writer) throws IOException {

        PdfReader backgroundReader = new PdfReader("src/main/resources/pdf/Clevertec_Template.pdf");
        PdfImportedPage background = writer.getImportedPage(backgroundReader, 1);
        PdfContentByte contentByte = writer.getDirectContentUnder();
        contentByte.addTemplate(background, 0, 0);
    }

    private Path getPath() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMHHmm");
        String dateTime = LocalDateTime.now().format(formatter);
        int randomNum = new Random().nextInt(900) + 100;
        return Paths.get("src/main/resources/pdf/reportPDF" + dateTime + randomNum + ".pdf");
    }
}
