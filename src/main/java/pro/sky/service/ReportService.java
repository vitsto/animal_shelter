package pro.sky.service;

import liquibase.pro.packaged.R;
import org.springframework.stereotype.Service;
import pro.sky.entity.Pet;
import pro.sky.entity.Report;
import pro.sky.repository.ReportRepository;

import java.time.LocalDateTime;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void createReport(Pet pet, String description, String pathToFile) {
        Report report = new Report(pet, LocalDateTime.now(), description, pathToFile);
        reportRepository.save(report);
    }
}
