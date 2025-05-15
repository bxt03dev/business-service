package com.buixuantruong.shopapp.service.impl;

import com.buixuantruong.shopapp.model.*;
import com.buixuantruong.shopapp.model.SocialLoginResponse.UserProfile;
import com.buixuantruong.shopapp.repository.RoleRepository;
import com.buixuantruong.shopapp.repository.SocialAccountRepository;
import com.buixuantruong.shopapp.repository.UserRepository;
import com.buixuantruong.shopapp.security.jwt.JWTTokenUtil;
import com.buixuantruong.shopapp.service.SocialLoginService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginServiceImpl implements SocialLoginService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final JWTTokenUtil jwtTokenUtil;
    private final OAuth2Config oAuth2Config;

    @Override
    @Transactional
    public SocialLoginResponse loginWithGoogle(SocialLoginRequest request) throws Exception {
        OAuth2UserInfo userInfo = getGoogleUserInfo(request.getAccessToken());
        return processUserLogin(userInfo, SocialAccount.PROVIDER_GOOGLE, request.getAccessToken());
    }

    @Override
    @Transactional
    public SocialLoginResponse loginWithFacebook(SocialLoginRequest request) throws Exception {
        OAuth2UserInfo userInfo = getFacebookUserInfo(request.getAccessToken());
        return processUserLogin(userInfo, SocialAccount.PROVIDER_FACEBOOK, request.getAccessToken());
    }

    @Override
    public OAuth2UserInfo getGoogleUserInfo(String accessToken) throws Exception {
        String userInfoEndpoint = oAuth2Config.getProviders().get("google").getUserInfoUri();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
                userInfoEndpoint,
                HttpMethod.GET,
                entity,
                String.class);
        
        Map<String, Object> attributes = 
                objectMapper.readValue(response.getBody(), Map.class);
        
        return OAuth2UserInfo.extractGoogleUserInfo(attributes);
    }

    @Override
    public OAuth2UserInfo getFacebookUserInfo(String accessToken) throws Exception {
        String userInfoEndpoint = oAuth2Config.getProviders().get("facebook").getUserInfoUri();
        
        String graphApiUrl = userInfoEndpoint + 
                "?fields=id,name,email,picture&access_token=" + accessToken;
        
        ResponseEntity<String> response = restTemplate.getForEntity(graphApiUrl, String.class);
        
        Map<String, Object> attributes = 
                objectMapper.readValue(response.getBody(), Map.class);
        
        return OAuth2UserInfo.extractFacebookUserInfo(attributes);
    }
    
    @Transactional
    private SocialLoginResponse processUserLogin(OAuth2UserInfo userInfo, String provider, String accessToken) {
        boolean isNewUser = false;
        
        // Tìm tài khoản theo provider và providerId
        Optional<SocialAccount> existingSocialAccount = socialAccountRepository
                .findByProviderAndProviderId(provider, userInfo.getId());
        
        User user;
        
        if (existingSocialAccount.isPresent()) {
            // Người dùng đã tồn tại
            SocialAccount socialAccount = existingSocialAccount.get();
            user = socialAccount.getUser();
            
            // Cập nhật thông tin mới nhất
            socialAccount.setEmail(userInfo.getEmail());
            socialAccount.setName(userInfo.getName());
            socialAccount.setPictureUrl(userInfo.getImageUrl());
            socialAccount.setAccessToken(accessToken);
            
            socialAccountRepository.save(socialAccount);
        } else {
            // Người dùng mới, kiểm tra xem có tài khoản nào với email này chưa
            Optional<User> existingUserByEmail = Optional.empty();
            if (userInfo.getEmail() != null) {
                existingUserByEmail = userRepository.findByEmail(userInfo.getEmail());
            }
            
            if (existingUserByEmail.isPresent()) {
                // Có người dùng có email này, liên kết tài khoản xã hội với họ
                user = existingUserByEmail.get();
                
                SocialAccount newSocialAccount = SocialAccount.builder()
                        .provider(provider)
                        .providerId(userInfo.getId())
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .pictureUrl(userInfo.getImageUrl())
                        .accessToken(accessToken)
                        .user(user)
                        .build();
                
                if (user.getSocialAccounts() == null) {
                    user.setSocialAccounts(new ArrayList<>());
                }
                user.getSocialAccounts().add(newSocialAccount);
                
                user = userRepository.save(user);
            } else {
                // Người dùng hoàn toàn mới
                isNewUser = true;
                
                // Tạo mã số điện thoại ngẫu nhiên nếu không có
                String phoneNumber = generateRandomPhoneNumber();
                while (userRepository.existsByPhoneNumber(phoneNumber)) {
                    phoneNumber = generateRandomPhoneNumber();
                }
                
                // Tìm vai trò USER
                Role userRole = roleRepository.findByName(Role.USER)
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName(Role.USER);
                            return roleRepository.save(newRole);
                        });
                
                // Tạo người dùng mới
                user = User.builder()
                        .fullName(userInfo.getName())
                        .email(userInfo.getEmail())
                        .avatarUrl(userInfo.getImageUrl())
                        .phoneNumber(phoneNumber)
                        .active(true)
                        .socialAccounts(new ArrayList<>())
                        .role(userRole)
                        .build();
                
                SocialAccount newSocialAccount = SocialAccount.builder()
                        .provider(provider)
                        .providerId(userInfo.getId())
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .pictureUrl(userInfo.getImageUrl())
                        .accessToken(accessToken)
                        .user(user)
                        .build();
                
                user.getSocialAccounts().add(newSocialAccount);
                
                user = userRepository.save(user);
            }
        }
        
        // Tạo JWT token
        String jwtToken = jwtTokenUtil.generateToken(user);
        
        // Tạo response
        return SocialLoginResponse.builder()
                .accessToken(jwtToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenUtil.getExpirationTime())
                .message("Login successful")
                .success(true)
                .userProfile(UserProfile.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .avatarUrl(user.getAvatarUrl())
                        .role(user.getRole().getName())
                        .isNewUser(isNewUser)
                        .build())
                .build();
    }
    
    private String generateRandomPhoneNumber() {
        // Tạo một số điện thoại ngẫu nhiên 10 chữ số bắt đầu bằng "09"
        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder("09");
        for (int i = 0; i < 8; i++) {
            phoneNumber.append(random.nextInt(10));
        }
        return phoneNumber.toString();
    }
} 