package com.springboot.backendprompren.data.dto.SignDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignUpDto {
    private String account;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String nickname;
    private String thumbnail;


}
