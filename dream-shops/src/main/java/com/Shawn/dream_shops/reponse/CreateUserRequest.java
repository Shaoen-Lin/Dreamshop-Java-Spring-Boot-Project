package com.Shawn.dream_shops.reponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
// 創立 User account 時需要有的東西
