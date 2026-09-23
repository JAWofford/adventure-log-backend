package com.jwofford.adventure_log_backend.repositories;

import com.jwofford.adventure_log_backend.models.TripLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripLogRepository extends JpaRepository<TripLog, Long> {
    List<TripLog> findByUserIdOrderByStartDateDesc(Long userId);
}
