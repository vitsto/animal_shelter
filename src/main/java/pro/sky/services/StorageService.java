package pro.sky.services;

import com.pengrad.telegrambot.model.File;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(File img);
}
