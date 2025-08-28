package com.Shawn.dream_shops.request;

import com.Shawn.dream_shops.model.Category;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest  {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory; // 存貨
    private String description;
    private Category category;

    // 這裡沒有 Image
}
