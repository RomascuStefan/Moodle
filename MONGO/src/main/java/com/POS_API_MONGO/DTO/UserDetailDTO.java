package com.POS_API_MONGO.DTO;

import auth.AuthServiceOuterClass;
import lombok.Data;

@Data
public class UserDetailDTO {
    private String email;
    private AuthServiceOuterClass.Role role;
}
