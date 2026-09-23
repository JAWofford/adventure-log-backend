package com.jwofford.adventure_log_backend.repositories;

import com.jwofford.adventure_log_backend.models.CampgroundReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampgroundReviewRepository extends JpaRepository<CampgroundReview,Long> {

    List<CampgroundReview> findByUserIdOrderByCampgroundNameAsc(Long userId);

}
