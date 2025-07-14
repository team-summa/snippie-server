package com.snippie.backend.auth.handler;

import com.snippie.backend.auth.dto.CustomOauth2User;
import com.snippie.backend.auth.dto.UserPrincipal;
import com.snippie.backend.user.domain.User;
import com.snippie.backend.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;

    @Value("${app.oauth2.success-redirect-uri}")
    private String successRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOauth2User oauthUser = (CustomOauth2User) authentication.getPrincipal();

        // DB에서 User 엔티티 조회
        User user = userRepository.findByGithubId(oauthUser.getSocialId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // UserPrincipal 생성
        UserPrincipal principal = new UserPrincipal(user.getId(), user.getNickname());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        (UserDetails) principal, null, principal.getAuthorities()
                );

        // SecurityContext에 새 Authentication 등록
        SecurityContextHolder.getContext().setAuthentication(auth);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        response.sendRedirect(successRedirectUri);
    }
}
