package pro.sky.services;

import pro.sky.entity.Pet;

public interface ReportService {
    void createReport(Pet pet, String description, String pathToFile);

}
