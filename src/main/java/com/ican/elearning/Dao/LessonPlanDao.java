package com.ican.elearning.Dao;

import com.ican.elearning.dataobject.LessonPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by JackyGuo
 * 2017/9/12 18:38
 */
public interface LessonPlanDao extends JpaRepository<LessonPlan,Integer>{
    Page<LessonPlan> findByPlanGrade(String planGrade,Pageable pageable);
}
