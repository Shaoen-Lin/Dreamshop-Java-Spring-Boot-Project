package com.Shawn.dream_shops.reponse;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
}

// 更新 User account 時需要有的東西