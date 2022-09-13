package com.hsbc.auth.handler;

import com.hsbc.auth.ParsingException;
import com.hsbc.auth.utils.ObjectParser;
import com.hsbc.auth.utils.ResponseUtil;
import com.hsbc.auth.entities.Response;
import com.hsbc.auth.entities.Role;
import com.hsbc.auth.entities.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static com.hsbc.auth.MainService.allUsers;
import static com.hsbc.auth.MainService.tokenMap;


public class UserHandler extends BaseHandler implements HttpHandler {

    String uri_base = "/user/";

    @Override
    public void handle(HttpExchange t) throws IOException {
        try {
            // get all roles for a user
            if ("GET".equals(t.getRequestMethod())) {
                if("roles".equals(t.getRequestURI().getPath().replace(uri_base,""))){
                    String token = getToken(t);
                    Response response = getRoleForUser(token);
                    ResponseUtil.response(t, response);
                }
            }

            if ("POST".equals(t.getRequestMethod())) {
                // Check user role
                if("check_user_with_role".equals(t.getRequestURI().getPath().replace(uri_base,""))){
                    String content = getContent(t);
                    String token = getToken(t);
                    Response response = checkUserRole(token, content);
                    ResponseUtil.response(t, response);

                // Crate user
                } else if ("/user".equals(t.getRequestURI().getPath())){
                    String content = getContent(t);
                    Response response = createUser(content);
                    ResponseUtil.response(t, response);
                }
            }

            // add role for a user
            if ("PATCH".equals(t.getRequestMethod())) {
                if ("add_role".equals(t.getRequestURI().getPath().replace(uri_base, ""))) {
                    String content = getContent(t);
                    String token = getToken(t);
                    Response response = addRoleForUser(token, content);
                    ResponseUtil.response(t, response);
                }
            }

            // delete user
            if ("DELETE".equals(t.getRequestMethod())) {
                String content = getContent(t);
                Response response = deleteUser(content);
                ResponseUtil.response(t, response);
            }

            ResponseUtil.response(t,404, "Endpoint not supported" );
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.response(t, 500, e.getMessage());
        }

    }


    public Response createUser(String content) throws ParsingException {
        User user = ObjectParser.parseUser(content);
        if (allUsers.containsKey(user.getUsername())) {
            return new Response(200, "User already exist");
        }
        allUsers.put(user.getUsername(), user);
        return new Response(200, "The user " + user.getUsername() + " create success");
    }

    public Response deleteUser(String content) throws ParsingException {
        User user = ObjectParser.parseUser(content);
        if (allUsers.containsKey(user.getUsername())) {
            allUsers.remove(user.getUsername());
            return new Response(200, "User delete success");
        } else {
            return new Response(200, "User does not exist");
        }
    }

    public Response getRoleForUser(String token) throws ParsingException {
        if(!isTokenValid(token)){
            return new Response(200, "token is not valid/expired!");
        }else{
            String username = tokenMap.get(token);
            User authUser = allUsers.get(username);
            return new Response(200, authUser.getRoles().toString() );
        }
    }

    public Response checkUserRole(String token, String content) throws ParsingException {
        Role role = ObjectParser.parseRole(content);
        if(!isTokenValid(token)){
            return new Response(200, "token is not valid/expired!");
        }else{
            String username = tokenMap.get(token);
            User authUser = allUsers.get(username);
            if(authUser.getRoles().contains(role)){
                return new Response(200, "true");
            }else{
                return new Response(200, "false");
            }
        }
    }

    public Response addRoleForUser(String token, String content) throws ParsingException {
        Role role = ObjectParser.parseRole(content);
        if (!isTokenValid(token)) {
            return new Response(200, "token is not valid/expired!");
        } else {
            String username = tokenMap.get(token);
            User authUser = allUsers.get(username);
            if (authUser.getRoles().contains(role)) {
                return new Response(200, "User already has the role " + role.getName());
            } else {
                authUser.getRoles().add(role);
                return new Response(200, "Add role to user success");
            }
        }
    }

}