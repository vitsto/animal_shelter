package pro.sky.services.impl;

import com.pengrad.telegrambot.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.services.StorageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {
    @Override
    public String store(File img) {
        Path dir = Paths.get("/images");
        String filename = img.fileId() + UUID.randomUUID();
        Path file;
        try {
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
            }
            file = Files.createFile(Paths.get(dir.toString(),  filename));

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file.toFile()));
            oos.writeObject(img);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filename;
    }

}
