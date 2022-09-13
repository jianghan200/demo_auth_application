package com.hsbc.auth;

import com.hsbc.auth.entities.Role;
import com.hsbc.auth.entities.User;
import com.hsbc.auth.handler.LoginHandler;
import com.hsbc.auth.handler.RoleHandler;
import com.hsbc.auth.handler.UserHandler;
import com.hsbc.auth.utils.EncryptUtil;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static com.hsbc.auth.Constant.TOKEN_LIFE_TIME;

public class MainService {

    public static ConcurrentHashMap<String, User> allUsers = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Role> allRoles = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> tokenMap = new ConcurrentHashMap<>();

    static {
        //md5("pass123") -> "32250170a0dca92d53ec9624f336ca24"
        allUsers.put("john", new User(11L, "john", "32250170a0dca92d53ec9624f336ca24" ));
        allRoles.put("FTE", new Role(100L,"FTE") );
        tokenMap.put("1663056279809-3a9f3ac5-280f-42bb-9c2e-c1ef2980e6b1", "john");
        allUsers.get("john").setTokenExpiredTime(System.currentTimeMillis() + TOKEN_LIFE_TIME );
    }



    public static void main(String[] args) {
        try {

            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/user", new UserHandler());
            server.createContext("/role", new RoleHandler());
            server.createContext("/login", new LoginHandler());

            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}