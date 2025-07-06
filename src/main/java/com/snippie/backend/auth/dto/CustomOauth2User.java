package com.snippie.backend.auth.dto;

import com.snippie.backend.user.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOauth2User implements OAuth2User {

    private final String socialId; // GitHub ID
    private final String nickname; // GitHub login 값
    private final String avatarUrl; // 프로필 이미지 URL


    public CustomOauth2User(OAuth2Response oAuth2Response) {
        this.socialId = oAuth2Response.getProviderId();
        this.nickname = oAuth2Response.getNickname();
        this.avatarUrl = oAuth2Response.getAvatarUrl();
    }


    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return this.socialId;
    }
}
