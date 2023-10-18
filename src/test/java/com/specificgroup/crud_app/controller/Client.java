package com.specificgroup.crud_app.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.specificgroup.crud_app.util.Constants.HttpMethod.GET;


public final class Client {

    private final String url;
    private final String getRequestUrl;
    private final Optional<String> requestMethod;
    private Optional<String> content;
    private String response;
    private int responseCode;

    private Client(Builder builder) {
        this.url = builder.url;
        this.getRequestUrl = builder.getRequestUrl;
        this.requestMethod = Optional.ofNullable(builder.requestMethod);
        this.content = Optional.ofNullable(builder.content);
    }

    public void sendRequest() {
        try {
            HttpURLConnection connection;
            if (requestMethod.isPresent() && GET.equalsIgnoreCase(requestMethod.get())) {
                connection = (HttpURLConnection) new URL(url + getRequestUrl).openConnection();
            } else {
                connection = (HttpURLConnection) new URL(url).openConnection();
            }
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept","*/*");
            connection.setRequestMethod(requestMethod.get());
            if (content.isPresent()) {
                connection.setDoOutput(true);
                writeContent(connection, content.get());
            }

            responseCode = connection.getResponseCode();

            if (responseCode < 299) {
                response = readResponse(connection.getInputStream());
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getResponse() {
        return response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    private String readResponse(InputStream inputStream) throws IOException {
        StringBuilder concat = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String data;
            while ((data = bufferedReader.readLine()) != null) {
                concat.append(data);
            }
        }
        return concat.toString();
    }

    private void writeContent(HttpURLConnection httpsURLConnection, String content) throws IOException {
        try (OutputStream outputStream = httpsURLConnection.getOutputStream()) {
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes);
        }
    }

    public static class Builder {
        private String url;
        private String getRequestUrl;
        private String requestMethod;
        private String content;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder getRequestUrl(String getRequestUrl) {
            this.getRequestUrl = getRequestUrl;
            return this;
        }

        public Builder requestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Client build() {
            return new Client(this);
        }
    }

}
