package com.webdesign.countryservice.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private int id;

    private String name;
    private String password;
    private boolean active;

    private String loginToken;
    private LocalDateTime loginTokenExpires;

    private List<Token> apiTokens = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>();

    public User(String name, String password) {
        this.id = users.size();
        this.name = name;
        this.password = password;
        this.active = false;
        users.add(this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public List<Token> getApiTokens() {
        return apiTokens;
    }

    public static User getUserById(int id) {
        for (User user : users) {
            if (user.id == id) {
                return user;
            }
        }
        return null;
    }

    public static User getUserByLoginToken(String loginToken) {
        for (User user : users) {
            if (user.loginToken != null && user.loginToken.equals(loginToken) && user.loginTokenExpires.isAfter(LocalDateTime.now())) {
                return user;
            }
        }
        return null;
    }

    public static User login(String name, String password) {
        for (User user : users) {
            if (user.getName().equals(name) && user.getPassword().equals(password) && user.active) {
                user.loginToken = UUID.randomUUID().toString();
                user.loginTokenExpires = LocalDateTime.now().plusHours(1);
                return user;
            }
        }
        return null;
    }


    public static boolean authenticate(String loginToken) {
        for (User user : users) {
            if (user.loginToken != null && user.loginToken.equals(loginToken) && user.active && user.loginTokenExpires.isAfter(LocalDateTime.now())) {
                return true;
            }
        }
        return false;
    }
}
