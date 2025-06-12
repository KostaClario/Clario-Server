package com.oopsw.clario.dto.member;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinDTO {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phonenum;

    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @Size(min = 8, message = "비밀번호 확인은 8자 이상이어야 합니다.")
    private String confirmPassword;
}
