package com.Shawn.dream_shops.dto;

import com.Shawn.dream_shops.model.CartItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class CartDto {

    private Long id;
    private Set<CartItemDto> items;
    private BigDecimal totalAmount;
}
