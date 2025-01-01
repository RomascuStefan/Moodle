package com.POS_API.DTO;

import auth.AuthServiceOuterClass;
import lombok.Data;

@Data
public class UserDetailDTO {
    private String email;
    private AuthServiceOuterClass.Role role;
}
