package com.Shawn.dream_shops.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 依靠資料庫本身的「自動遞增」功能來產生主鍵
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory; // 存貨
    private String description;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    // 主體（parent） 的所有操作（新增、修改、刪除）會同步套用到子項（child）
    // 但如果只是將子實體從父實體的集合中移除，數據庫中的子實體不會被自動刪除，而只是將它與父實體的關聯解除
    // 也就是 沒有賣這個Product了 我們讓Category不指向他 但他依舊會存在資料庫中
    private Category category;

    /*
        因為 Product 會顯示 Category 又 Category 又會顯示 Product
        => 雙向關聯 (bi-directional relationship) => 無限遞迴 (infinite recursion)


        序列化 (Serialization)：
        把 物件 (Java 物件) → 轉換成某種格式 (通常是 JSON 或二進位資料) 傳給前端就叫做 序列化。


     */

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    // 資料庫設計關係-
    // 主體（parent） 的所有操作（新增、修改、刪除）會同步套用到子項（child） +
    // 只要將子實體從父實體的集合中移除（如從List、Set中remove），
    // 這個子實體會被自動從數據庫中刪除。這個子實體被認為是“孤兒”
    private List<Image> images;


    public Product(String name, String brand, BigDecimal price, int inventory, String description, Category category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.inventory = inventory;
        this.description = description;
        this.category = category;
    }
}
