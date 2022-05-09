package com.svu.e_we_job.util.controller.model;

public class Administrator {
    public String Login,Password;

    public static final String ADMIN="Admin";

    public Administrator(String login, String password) {
        Login = login;
        Password = password;
    }
}
