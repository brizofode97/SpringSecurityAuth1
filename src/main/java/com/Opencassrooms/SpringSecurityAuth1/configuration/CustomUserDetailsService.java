package com.Opencassrooms.SpringSecurityAuth1.configuration;

import com.Opencassrooms.SpringSecurityAuth1.model.DBUser;
import com.Opencassrooms.SpringSecurityAuth1.repository.DBUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final DBUserRepository dbUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DBUser dbUser = dbUserRepository.findByUsername(username);
        if (dbUser == null) {
            return null;
        }

        return new User(dbUser.getUsername(), dbUser.getPassword(), getGrantedAuthority(dbUser.getRole()));
    }

    private List<GrantedAuthority> getGrantedAuthority(String role){
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + role));
        return grantedAuthorityList;
    }

}
