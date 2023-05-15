package pro.sky.services.impl;

import org.springframework.stereotype.Service;
import pro.sky.entity.Pet;
import pro.sky.entity.Report;
import pro.sky.repository.ReportRepository;
import pro.sky.services.ReportService;

import java.time.LocalDateTime;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public void createReport(Pet pet, String description, String pathToFile) {
        Report report = new Report(pet, LocalDateTime.now(), description, pathToFile);
        reportRepository.save(report);
    }
}
