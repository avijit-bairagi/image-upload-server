package com.ovi.EnkaizenTest.dao.repository;

import com.ovi.EnkaizenTest.dao.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    List<ImageEntity> findByParentId(Long parentId);
}
