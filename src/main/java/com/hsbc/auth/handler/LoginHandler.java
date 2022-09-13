package com.hsbc.auth.handler;

import com.hsbc.auth.ParsingException;
import com.hsbc.auth.entities.Response;
import com.hsbc.auth.entities.Role;
import com.hsbc.auth.utils.ObjectParser;
import com.hsbc.auth.utils.ResponseUtil;
import com.hsbc.auth.entities.User;
import com.hsbc.auth.utils.TokenUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static com.hsbc.auth.Constant.TOKEN_LIFE_TIME;
import static com.hsbc.auth.MainService.allUsers;
import static com.hsbc.auth.MainService.tokenMap;


public class LoginHandler extends BaseHandler implements HttpHandler {

        String uri_base = "/login/";
        @Override
        public void handle(HttpExchange t) throws IOException {
            try{
                // Authentication user
                if("POST".equals(t.getRequestMethod())){
                    String content = getContent(t);
                    Response response = authentication(content);
                    ResponseUtil.response(t, response);
                }

                // Invalidate token
                if("DELETE".equals(t.getRequestMethod())){
                    String token = getContent(t);
                    Response response = invalidate(token);
                    ResponseUtil.response(t, response);
                }

                ResponseUtil.response(t,404, "Endpoint not supported" );
            }catch (Exception e){
                ResponseUtil.response(t,500, e.getMessage() );
            }
        }


    public Response authentication(String content ) throws ParsingException {
        User user = ObjectParser.parseUser(content);
        if(allUsers.containsKey(user.getUsername())){
            User exist = allUsers.get(user.getUsername());
            if(exist.getPassword().equals(user.getPassword())){
                String token = TokenUtil.tokenSupplier.get();
                tokenMap.put(token, exist.getUsername());
                exist.setToken(token);
                exist.setTokenExpiredTime(System.currentTimeMillis() + TOKEN_LIFE_TIME);
            }
            return new Response(200, exist.getToken());
        }
        allUsers.put(user.getUsername(), user);
        return new Response(200, "the user "+ user.getUsername() +" create success");
    }

    public Response invalidate(String token ) throws ParsingException {
        if(!tokenMap.containsKey(token)){
            return new Response(200, "token is not exist!");
        }else{
            String username = tokenMap.get(token);
            User authUser = allUsers.get(username);
            authUser.setToken(null);
            authUser.setTokenExpiredTime(System.currentTimeMillis());
            return new Response(200, "" );
        }
    }

}