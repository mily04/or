package com.mily04.systemw.repository;

import com.mily04.systemw.model.ProjectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ProjectDataRepository 接口
 * 继承 JpaRepository 后，Spring Data JPA 会自动为你实现基础的增删改查（CRUD）功能
 */
@Repository
public interface ProjectDataRepository extends JpaRepository<ProjectData, String> {
    // 这里不需要写任何方法，JpaRepository 已经提供了 findById, save, delete 等功能
}