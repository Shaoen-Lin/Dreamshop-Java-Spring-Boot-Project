package com.Shawn.dream_shops.dto;

import com.Shawn.dream_shops.model.Order;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private List<OrderDto> order;
    private CartDto cart;
}
