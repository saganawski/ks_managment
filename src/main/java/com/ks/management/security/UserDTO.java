package com.ks.management.security;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {
    @NonNull
    private Integer id;
    private String username;
    private String password;
    private String confirmPassword;
    private Boolean isActive;
    private String roles;
    private Date updatedDate;
    private Date createdDate;
}
