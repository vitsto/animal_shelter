package pro.sky.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.entity.Pet;
import pro.sky.service.ReportService;
import pro.sky.service.StorageService;

public class ReportController {
    
    private final StorageService storageService;
    private final ReportService reportService;


    public ReportController(StorageService storageService, ReportService reportService) {
        this.storageService = storageService;
        this.reportService = reportService;
    }

    @PostMapping
    public void createReport(@RequestPart Pet pet, @RequestPart(name = "file") MultipartFile image, String description) {
        String pathToFile = storageService.store(image); //файл сохранен, вернулся путь
        reportService.createReport(pet, description, pathToFile);
    }
}
