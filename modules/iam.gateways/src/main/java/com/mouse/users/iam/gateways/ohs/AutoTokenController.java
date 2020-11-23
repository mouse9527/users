package com.mouse.users.iam.gateways.ohs;

import com.mouse.users.iam.application.LoginWithUsernamePasswdUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AutoTokenController {
    private @Resource LoginWithUsernamePasswdUseCase loginWithUsernamePasswdUseCase;

    @PostMapping("/auth/tokens")
    public Object allocate(@RequestBody UsernamePasswdLoginCommand command) {
        return loginWithUsernamePasswdUseCase.login(command.getUsername(), command.getPassword());
    }
}
