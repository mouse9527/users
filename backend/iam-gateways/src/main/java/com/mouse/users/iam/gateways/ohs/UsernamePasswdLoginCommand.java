package com.mouse.users.iam.gateways.ohs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsernamePasswdLoginCommand {
    private final String username;
    private final String password;
}
