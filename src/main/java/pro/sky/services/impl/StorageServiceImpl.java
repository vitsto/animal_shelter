package pro.sky.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.services.StorageService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {
    @Override
    public String store(MultipartFile img) {
        Path dir = Paths.get("/images");
        String filename = img.getName() + UUID.randomUUID();
        Path file;
        try {
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
            }
            file = Files.createFile(Paths.get(dir.toString(),  filename));
            BufferedInputStream fis = new BufferedInputStream(img.getInputStream());
            BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file.toFile()));
            fos.write(fis.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filename;
    }
}
