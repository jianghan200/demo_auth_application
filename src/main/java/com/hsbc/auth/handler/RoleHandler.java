package com.hsbc.auth.handler;

import com.hsbc.auth.ParsingException;
import com.hsbc.auth.entities.Response;
import com.hsbc.auth.entities.User;
import com.hsbc.auth.utils.ObjectParser;
import com.hsbc.auth.utils.ResponseUtil;
import com.hsbc.auth.entities.Role;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static com.hsbc.auth.MainService.*;

public class RoleHandler extends BaseHandler implements HttpHandler {

    String uri_base = "/role/";

    @Override
    public void handle(HttpExchange t) throws IOException {
        try {
            // Create role
            if ("POST".equals(t.getRequestMethod())) {
                String content = getContent(t);
                Response response = createRole(content);
                ResponseUtil.response(t, response);
            }

            // Delete role
            if ("DELETE".equals(t.getRequestMethod())) {
                String content = getContent(t);
                Response response = deleteRole(content);
                ResponseUtil.response(t, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.response(t, 500, e.getMessage());
        }
    }

    public Response createRole(String content) throws ParsingException {
        Role role = ObjectParser.parseRole(content);
        if (allRoles.containsKey(role.getName())) {
            return new Response(200, "the role already exist");
        }
        allRoles.put(role.getName(), role);
        return new Response(200, "the role " + role.getName() + " create success");
    }

    public Response deleteRole(String content) throws ParsingException {
        Role role = ObjectParser.parseRole(content);
        if (allRoles.containsKey(role.getName())) {
            allRoles.remove(role.getName());
            return new Response(200, "role delete success");
        } else {
            return new Response(200, "role does not exist");
        }
    }
}