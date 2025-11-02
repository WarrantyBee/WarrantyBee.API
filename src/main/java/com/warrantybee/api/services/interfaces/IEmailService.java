package com.warrantybee.api.services.interfaces;

public interface IEmailService {
    void send(String to, String body);

    void sendOtp(String email, String otp);
}


