package pro.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
