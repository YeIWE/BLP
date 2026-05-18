package com.blp.auth.service;

import com.blp.auth.mapper.UserMapper;
import com.blp.auth.model.SecurityUser;
import lombok.RequiredArgsConstructor;
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
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser su = userMapper.selectByUsername(username);
        if (su == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        List<String> roles = userMapper.selectRolesByUserId(su.getId());
        List<String> perms = userMapper.selectPermissionsByUserId(su.getId());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (roles != null) {
            roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
        }
        if (perms != null) {
            perms.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));
        }
        return User.withUsername(su.getUsername())
                .password(su.getPassword())
                .authorities(authorities)
                .accountLocked(su.getStatus() != null && su.getStatus() == 0)
                .build();
    }
}
