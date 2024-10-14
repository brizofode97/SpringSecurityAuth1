package com.Opencassrooms.SpringSecurityAuth1.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class LoginController {

    private final OAuth2AuthorizedClientService oauth2AuthorizedClientService;

    @GetMapping(path = "/user")
    public String getUser(){
        return "Welcome, User";
    }

    public LoginController(OAuth2AuthorizedClientService oauth2AuthorizedClientService) {
        this.oauth2AuthorizedClientService = oauth2AuthorizedClientService;
    }

    @GetMapping(path = "/admin")
    public String getAdmin(){
        return "Welcome, Admin";
    }

    @GetMapping(path = "/")
    public String getUserInfo(Principal user, @AuthenticationPrincipal OidcUser oidcUser){
        StringBuffer userInfo = new StringBuffer();
        if(user instanceof UsernamePasswordAuthenticationToken){
            userInfo.append(getUsernamePasswordLoginInfo(user));
        }else{
            userInfo.append(getOAuth2LoginInfor(user, oidcUser));
        }
        return userInfo.toString();
    }

    //IMPORTANT: On affiche des informations à partir de l'objet Principal si l'utilisateur se connecte avec un "username" et "password"
    private StringBuffer getUsernamePasswordLoginInfo(Principal user){
        StringBuffer usernameInfo = new StringBuffer();
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) user;
        if(token.isAuthenticated()){
            User userToken = (User) token.getPrincipal();
            usernameInfo.append("Welcome, " + userToken.getUsername());
        }else{
            usernameInfo.append("NAN for UsernamePasswordAuthenticationToken");
        }
        return usernameInfo;
    }

    //IMPORTANT: On affiche des informations à partir de l'objet Principal si l'utilisateur se connecte avec un "OAuth 2.0"
    //NOTE: On a fait d'abord le test avec uniquement OAuth, maintenant on ajoute OIDC pour avoir plus d'information
    private StringBuffer getOAuth2LoginInfor(Principal user, OidcUser oidcUser){
        StringBuffer protectedInfo = new StringBuffer();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) user;
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oauth2AuthorizedClientService.loadAuthorizedClient(
                token.getAuthorizedClientRegistrationId(), token.getName());
        if(token.isAuthenticated()){
            Map<String, Object> userAttributes = ( (DefaultOAuth2User) token.getPrincipal()).getAttributes();
            String userToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
            protectedInfo.append("Welcome, "+ userAttributes.get("name") + "<br><br/>");
            protectedInfo.append("e-mail: " + userAttributes.get("email") + "<br><br/>");
            protectedInfo.append("Access Token: " + userToken);

            //NOTE: Implémentation de OIDC
            OidcIdToken idToken = oidcUser.getIdToken();
            if(idToken != null){
                protectedInfo.append("IDToken: " + idToken.getTokenValue() + "<br><br/><br><br/>");
                protectedInfo.append("Mapped Values: <br></br>");
                Map<String, Object> claims = idToken.getClaims();
                for(String key : claims.keySet()){
                    protectedInfo.append("---" + key + ": " + claims.get(key) + "<br><br/>");
                }
            }

        }
        return protectedInfo;
    }
}
