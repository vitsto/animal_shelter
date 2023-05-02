package pro.sky;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import pro.sky.services.impl.StorageServiceImpl;

public class FileUploadTest {

    private StorageServiceImpl storageService = new StorageServiceImpl();

    @Test
    public void shouldReturnPathToFile() {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        String pathToFile = storageService.store(multipartFile);

    }
}
