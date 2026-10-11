package com.user.authentication.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDto {

    private String number;
    private String countryCode;
    private String cityCode;
}
