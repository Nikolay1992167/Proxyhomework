package by.clevertec.pdfreport;

import by.clevertec.entity.Car;
import by.clevertec.pdfreport.impl.ServicePdfImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import util.CarTestData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ServicePdfImplTest {

    @InjectMocks
    private ServicePdfImpl servicePdf;

    @Test
    void shouldCreateReportPdf() throws IOException {
        // given
        String cache = "LRU";
        String action = "getById";
        Car car = CarTestData.builder()
                .build()
                .buildCar();
        String cacheAction = "Объект получен из кэш";

        // when
        Path path = servicePdf.createReportPdf(cache, action, car, cacheAction);

        // then
        assertThat(path).isNotNull();
        File file = path.toFile();
        assertThat(file).exists().isFile().hasExtension("pdf");
        assertThat(file.length()).isGreaterThan(0);
        Files.delete(path);
    }
}