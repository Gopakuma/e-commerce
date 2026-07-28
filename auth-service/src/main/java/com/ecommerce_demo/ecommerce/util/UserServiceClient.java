package com.ecommerce_demo.ecommerce.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/user/uuid")
    Boolean updateUuid(@RequestBody UUID uuid);
}
