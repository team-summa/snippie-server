package com.snippie.backend.auth.service;

import com.snippie.backend.auth.dto.CustomOauth2User;
import com.snippie.backend.auth.dto.GithubResponse;
import com.snippie.backend.auth.dto.OAuth2Response;
import com.snippie.backend.user.domain.User;
import com.snippie.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response;

        //추후 다른 소셜 로그인이 필요한 경우 여기에 추가
        if(registrationId.equals("github")){
            oAuth2Response = new GithubResponse(oAuth2User.getAttributes());
        }else{
            return null;
        }

        return handleUser(oAuth2Response);
    }

    private OAuth2User handleUser(OAuth2Response oAuth2Response) {
        return userRepository.findByGithubId(oAuth2Response.getProviderId())
                .map(user -> handleExistingUser(user, oAuth2Response))
                .orElseGet(() -> handleNewUser(oAuth2Response));
    }

    //새로운 사용자 등록
    private OAuth2User handleNewUser(OAuth2Response oAuth2Response) {
        User user = User.builder()
                .githubId(oAuth2Response.getProviderId())
                .nickname(oAuth2Response.getNickname())
                .avatarUrl(oAuth2Response.getAvatarUrl())
                .build();

        userRepository.save(user);
        return new CustomOauth2User(oAuth2Response);
    }

    //기존 사용자 정보 업데이트
    private OAuth2User handleExistingUser(User user, OAuth2Response oAuth2Response) {
        user.updateUserInfo(oAuth2Response.getNickname(), oAuth2Response.getAvatarUrl());
        userRepository.save(user);
        return new CustomOauth2User(oAuth2Response);
    }


}
