package org.daCoffee.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtUserDetails {
    private final String memberId;
    private final int memberTier;
}