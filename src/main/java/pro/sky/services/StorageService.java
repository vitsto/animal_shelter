package pro.sky.services;

import org.springframework.web.multipart.MultipartFile;
public interface StorageService {
    String store(MultipartFile img);
}
