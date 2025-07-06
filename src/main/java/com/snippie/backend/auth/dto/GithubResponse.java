package com.snippie.backend.auth.dto;

import java.util.Map;

public class GithubResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public GithubResponse(Map<String, Object> attribute){
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "github";
    }

    @Override
    public String getProviderId() {
        Object idObj = attribute.get("id");
        if (idObj == null) {
            return null;
        }
        return String.valueOf(idObj);
    }

//    @Override
//    public String getEmail() {
//        return (String)attribute.get("email");
//    }

//    @Override
//    public String getName() {
//        return (String) attribute.get("name");
//    }

    public String getAvatarUrl(){
        return (String)attribute.get("avatar_url");
    }

    public String getNickname(){
        return (String)attribute.get("login");
    }
}
