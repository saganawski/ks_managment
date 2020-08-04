package com.ks.management.security;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {
    @NonNull
    private Integer id;
    private String username;
    private String password;
    private Boolean isActive;
    private String roles;
}
