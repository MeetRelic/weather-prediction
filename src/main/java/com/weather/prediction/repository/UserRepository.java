package com.weather.prediction.repository;

import com.weather.prediction.models.request.Users;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class UserRepository {

    private Map<String, Users> usersMap;

    @PostConstruct
    public void init() {
        this.usersMap = new HashMap<>();
        this.usersMap.put("Meet", new Users("Meet", new BCryptPasswordEncoder().encode("Password"), "ROLE_USER"));
    }

    public Users addUser(Users user) {
        return this.usersMap.putIfAbsent(user.getUserName(),
                new Users(user.getUserName(), new BCryptPasswordEncoder().encode(user.getPassword()), user.getRole()));

    }

}
