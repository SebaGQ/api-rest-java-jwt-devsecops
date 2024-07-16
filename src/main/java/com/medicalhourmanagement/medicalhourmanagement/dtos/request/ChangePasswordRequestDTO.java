package com.medicalhourmanagement.medicalhourmanagement.dtos.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordRequestDTO {

    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
