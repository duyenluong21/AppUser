package com.example.app_user.model;

public class PublicKeyRequest {
    private String maNV;
    private String public_key;

    // Constructor
    public PublicKeyRequest(String public_key) {
        this.public_key = public_key;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }
}
