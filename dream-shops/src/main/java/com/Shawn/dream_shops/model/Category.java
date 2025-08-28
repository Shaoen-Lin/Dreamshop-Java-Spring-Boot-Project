package com.Shawn.dream_shops.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    // 一個 Category 有許多 Product
    @JsonIgnore
    // 這一行會直接讓 Jackson 在序列化 Product 時忽略 category 字段。
    // 也就是會看不到 Category
    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }
}
