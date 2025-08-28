package com.Shawn.dream_shops.security.service;

import com.Shawn.dream_shops.model.User;
import com.Shawn.dream_shops.repository.UserRepository;
import com.Shawn.dream_shops.security.user.ShopUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepo.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return ShopUserDetails.buildUserDetails(user);
    }
}
