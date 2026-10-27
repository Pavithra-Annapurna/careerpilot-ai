package com.careerpilot.repository;
import com.careerpilot.entity.Resume; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ResumeRepository extends JpaRepository<Resume,Long>{List<Resume> findAllByUserIdOrderByCreatedAtDesc(Long userId); Optional<Resume> findByIdAndUserId(Long id,Long userId);}
