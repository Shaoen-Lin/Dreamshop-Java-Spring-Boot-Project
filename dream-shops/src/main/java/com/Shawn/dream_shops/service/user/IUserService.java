package com.Shawn.dream_shops.service.user;

import com.Shawn.dream_shops.dto.UserDto;
import com.Shawn.dream_shops.model.User;
import com.Shawn.dream_shops.reponse.CreateUserRequest;
import com.Shawn.dream_shops.reponse.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
