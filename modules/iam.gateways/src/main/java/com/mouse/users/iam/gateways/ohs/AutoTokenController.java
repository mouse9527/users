package com.mouse.users.iam.gateways.ohs;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutoTokenController {

    @PostMapping("/auth/tokens")
    public Object allocate() {
        return null;
    }
}
