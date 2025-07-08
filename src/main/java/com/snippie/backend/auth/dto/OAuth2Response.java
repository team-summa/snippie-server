package com.snippie.backend.auth.dto;

public interface OAuth2Response {
    //제공자 (ex) naver, google, github)
    String getProvider();

    //제공자가 발급해주는 아이디
    String getProviderId();

    String getNickname();

    String getAvatarUrl();

    //이메일
//    String getEmail();

    //사용자 실명(설정한 이름)
//    String getName();
}
