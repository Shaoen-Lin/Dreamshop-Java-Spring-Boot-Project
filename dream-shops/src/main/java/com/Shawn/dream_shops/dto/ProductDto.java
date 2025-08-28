package com.Shawn.dream_shops.dto;

import com.Shawn.dream_shops.model.Category;
import com.Shawn.dream_shops.model.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory; // 存貨
    private String description;
    private Category category;
    private List<ImageDto> images;
    // 也就是 Return Image 的 information 而不是 image本人 不然會噴錯
}
