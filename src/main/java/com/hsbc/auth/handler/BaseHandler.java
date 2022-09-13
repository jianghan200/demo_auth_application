package com.hsbc.auth.handler;

import com.hsbc.auth.entities.Response;
import com.hsbc.auth.entities.User;
import com.hsbc.auth.utils.ResponseUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static com.hsbc.auth.MainService.allUsers;
import static com.hsbc.auth.MainService.tokenMap;

public class BaseHandler {

//    public void invoke(HttpExchange exchange, HttpCallable callable) throws IOException {
//        String content = getContent(exchange);
//        try {
//            Response response = callable.call(content);
//            ResponseUtil.response(exchange, response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }




    protected String getContent(HttpExchange exchange) throws IOException {
        BufferedReader httpInput = new BufferedReader(new InputStreamReader(
                exchange.getRequestBody(), "UTF-8"));
        StringBuilder in = new StringBuilder();
        String input;
        while ((input = httpInput.readLine()) != null) {
            in.append(input).append(" ");
        }
        httpInput.close();
        return in.toString().trim();
    }

    public String getToken(HttpExchange exchange){
        List<String> tokens = exchange.getRequestHeaders().get("Token");
        if( tokens!= null && tokens.size() >=1){
            return tokens.get(0);
        }
        return null;
    }

    public boolean isTokenValid(String token){
        if(!tokenMap.containsKey(token)){
           return false;
        }else{
            String username = tokenMap.get(token);
            User authUser = allUsers.get(username);
            if(authUser.getTokenExpiredTime() < System.currentTimeMillis()){
                tokenMap.remove(token);
                authUser.setToken(null);
                return false;
            }else{
                return true;
            }
        }
    }
}
