package com.Shawn.dream_shops.repository;

import com.Shawn.dream_shops.model.Category;
import com.Shawn.dream_shops.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);
}
