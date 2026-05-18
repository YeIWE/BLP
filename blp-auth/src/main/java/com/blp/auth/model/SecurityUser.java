package com.blp.auth.model;

import lombok.Data;
import java.util.List;

@Data
public class SecurityUser {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Integer status;
    private List<String> roles;
    private List<String> permissions;
}
