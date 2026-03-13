package org.daCoffee.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CookieDTO {
    private String memberId;
    private String token;
}
