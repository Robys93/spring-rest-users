package com.giuseppe.rest_users.service.impl;

import com.giuseppe.rest_users.model.dto.User;
import com.giuseppe.rest_users.service.api.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private final List<User> users = new ArrayList<>();

    @Override
    public List<User> getAllUsers(String orderBy, Integer limit) {
        List<User> result = new ArrayList<>(this.users);

        if (orderBy != null) {
            boolean descending = orderBy.endsWith("_desc");
            String field = descending ? orderBy.substring(0, orderBy.length() - 5) :
                    (orderBy.endsWith("_asc") ? orderBy.substring(0, orderBy.length() - 4) : orderBy);

            Comparator<User> comparator = switch (field) {
                case "email" -> Comparator.comparing(User::email);
                case "name" -> Comparator.comparing(User::name);
                case "surname" -> Comparator.comparing(User::surname);
                default -> (a, b) -> 0;
            };

            if (descending) {
                comparator = comparator.reversed();
            }

            result.sort(comparator);
        }

        if (limit != null && limit > 0 && limit < result.size()) {
            result = result.subList(0, limit);
        }

        return result;
    }

    @Override
    public User getUserByEmail(String email) {
        return this.users.stream()
                .filter(user -> user.email().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User createUser(User user) {
        boolean emailExists = this.users.stream()
                .anyMatch(u -> u.email().equals(user.email()));
        if (emailExists) {
            return null;
        }
        this.users.add(user);
        return user;
    }

    @Override
    public User updateUser(String email, User updatedUser) {
        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).email().equals(email)) {
                this.users.set(i, updatedUser);
                return updatedUser;
            }
        }
        return null;
    }

    @Override
    public boolean deleteUser(String email) {
        return this.users.removeIf(user -> user.email().equals(email));
    }
}