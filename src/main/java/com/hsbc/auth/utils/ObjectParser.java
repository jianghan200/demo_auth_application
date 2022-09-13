package com.hsbc.auth.utils;

import com.hsbc.auth.ParsingException;
import com.hsbc.auth.entities.Role;
import com.hsbc.auth.entities.User;
import com.sun.source.tree.TryTree;

import java.util.HashSet;
import java.util.Set;

public class ObjectParser {

    public static Role parseRole(String raw){
        if (raw == null || "".equals(raw)) return null;
        raw = raw.substring(1, raw.length()-1);
        String[] items  = raw.split(":");
        Role role = new Role();
        role.setId(Long.parseLong(items[0]));
        role.setName(items[1]);
        return role;
    }

    public static Set<Role> parseRoles(String raw){
        raw = raw.substring(1, raw.length()-1);
        String[] items  = raw.split(",");
        Set<Role> roles = new HashSet<>();
        for(String item: items){
            if(item.startsWith("{"))
                roles.add(parseRole(item));
        }
        return roles;
    }

    public static User parseUser(String raw) throws ParsingException {
        User user = new User();
        try{
            String[] items = raw.split("&");
            for(String item: items){
                if(item.trim().startsWith("id=")){
                    user.setId(Long.parseLong(item.replace("id=","")));
                }
                if(item.trim().startsWith("roles=")){
                    user.setRoles(parseRoles(item.replace("roles=","")));
                }
                if(item.trim().startsWith("username=")){
                    user.setUsername(item.replace("username=",""));
                }
                if(item.trim().startsWith("password=")){
                    String rawPassword = item.replace("password=","");
                    user.setPassword(EncryptUtil.md5(rawPassword));
                }
            }
        }catch(Exception e){
            throw new ParsingException("Error when parsing user object from string");
        }
        return user;

    }

    public static String getToken(String raw) throws Exception {
        return null;
    }






}
