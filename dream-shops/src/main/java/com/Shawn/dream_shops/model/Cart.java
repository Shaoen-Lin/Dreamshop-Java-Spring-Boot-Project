package com.Shawn.dream_shops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy="cart", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();
    // orphanRemoval = true => When there is no cart item then the cart will be deleted

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addItem(CartItem item)
    {
        this.items.add(item);
        item.setCart(this);
        updateTotalAmount();
    } // 只要加入一個 item  就要重算購物車的總金額

    public void removeItem(CartItem item)
    {
        this.items.remove(item);
        item.setCart(null);
        updateTotalAmount();
    }

    public BigDecimal updateTotalAmount()
    {
        this.totalAmount = items.stream()
                .map(item ->
                    {
                        BigDecimal unitPrice = item.getUnitPrice();

                        if(unitPrice == null)
                            return BigDecimal.ZERO;

                        return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                    }).reduce(BigDecimal.ZERO, BigDecimal::add);
                    // BigDecimal.ZERO 是初始值（確保購物車空的時候會回傳 0）
        //          // BigDecimal::add 表示用 add 來累加。
        return this.totalAmount;
    }
}
