package com.careerpilot.repository;
import com.careerpilot.entity.Analysis; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface AnalysisRepository extends JpaRepository<Analysis,Long>{List<Analysis> findAllByUserIdOrderByCreatedAtDesc(Long userId); Optional<Analysis> findByIdAndUserId(Long id,Long userId);}
