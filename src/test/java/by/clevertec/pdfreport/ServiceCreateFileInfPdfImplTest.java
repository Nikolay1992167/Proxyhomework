package by.clevertec.pdfreport;

import by.clevertec.exception.PDFException;
import by.clevertec.pdfreport.impl.ServiceCreateFileInfPdfImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ServiceCreateFileInfPdfImplTest {

    @InjectMocks
    private ServiceCreateFileInfPdfImpl service;

    @Test
    void shouldCreatePdfFileWithInformationAboutCar() {
        // given
        String lineResponse = "Checking file creation";

        // when, then
        assertDoesNotThrow(() -> service.creatFilePdfWithInfAboutCar(lineResponse));
    }
}