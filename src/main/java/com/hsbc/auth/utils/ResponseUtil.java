package com.hsbc.auth.utils;

import com.hsbc.auth.entities.Response;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseUtil {

    public static void response(HttpExchange httpExchange, int errCode, String msg) throws IOException {
        httpExchange.sendResponseHeaders(errCode, msg.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(msg.getBytes());
        os.close();
    }

    public static void response(HttpExchange httpExchange, Response response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.getMsg().length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getMsg().getBytes());
        os.close();
    }

}
