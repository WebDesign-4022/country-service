package com.webdesign.countryservice.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webdesign.countryservice.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Value("${adminToken}")
    private String adminToken;

    public String activate(String username, boolean active, String token) {
        if (!token.equals(adminToken)) return null;

        User user = User.getUserByName(username);
        if (user == null) return null;

        user.setActive(active);
        return "{\"success\": true}";
    }

    public String listUsers(String token) {
        if (!token.equals(adminToken)) return null;

        List<User> users = User.getUsers();
        JsonArray jsonArray = new JsonArray();
        for (User user : users) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("username", user.getName());
            jsonObject.addProperty("active", user.getActive());
            jsonArray.add(jsonObject);
        }

        Gson gson = new Gson();
        return gson.toJson(jsonArray);
    }
}
