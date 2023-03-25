package com.afulvio.booklify.dto;

import com.afulvio.booklify.data.Role;
import lombok.Data;

@Data
public class UserDTO {

    private Integer id;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private Role role;
}
