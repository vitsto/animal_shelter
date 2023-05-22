package pro.sky.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.entity.Pet;
import pro.sky.services.ReportService;
import pro.sky.services.StorageService;

@RestController
@RequestMapping("/report")
@Tag(name = "Контроллер формирования отчётов", description = "Обработка отчётов")
public class ReportController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private ReportService reportService;

    @Operation(summary = "Создание отчёта")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёт создан"
            )
    })
    @PostMapping
    public void createReport(@RequestPart Pet pet, @RequestPart(name = "file") MultipartFile image, String description) {
//        String pathToFile = storageService.store(image); //файл сохранен, вернулся путь
//        reportService.createReport(pet, description, pathToFile);
    }
}
