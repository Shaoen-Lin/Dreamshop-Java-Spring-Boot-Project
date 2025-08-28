package com.Shawn.dream_shops.data;

import com.Shawn.dream_shops.model.Role;
import com.Shawn.dream_shops.model.User;
import com.Shawn.dream_shops.repository.RoleRepository;
import com.Shawn.dream_shops.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Transactional
@Component //  宣告為 Spring 組件 (Component)，在應用程式啟動時自動掃描、偵測並管理這個類別。
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private final UserRepository userRepo;
    @Autowired
    private final RoleRepository roleRepo;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN","ROLE_USER");
        createDefaultUserIfNotExits();
        createDefaultRoleIfNotExits(defaultRoles);
        createDefaultAdminIfNotExits();
    }

    private void createDefaultUserIfNotExits() {

        Role userRole = roleRepo.findByName("ROLE_USER").get();
        for (int i = 1; i <= 5; ++i) {
            String defaultEmail = "user" + i + "@email.com";

            if (userRepo.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepo.save(user);
            System.out.println("Default vet user " + i + " created successfully.");
        }
    }

    private void createDefaultAdminIfNotExits() {

        Role adminRole = roleRepo.findByName("ROLE_ADMIN").get();
        for (int i = 1; i <= 2; ++i) {
            String defaultEmail = "admin" + i + "@email.com";

            if (userRepo.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepo.save(user);
            System.out.println("Default vet user " + i + " created successfully.");
        }
    }

    private void createDefaultRoleIfNotExits(Set<String> roles){
            roles.stream()
                    .filter(role -> roleRepo.findByName(role). isEmpty())
                    .map(Role::new).forEach(roleRepo::save);
    }
}