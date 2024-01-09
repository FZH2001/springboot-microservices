package us.userservice.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.userservice.model.entity.Client;

import java.util.Map;
@FeignClient(name ="authentication-service")
public interface UserRestAPI {
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/auth/signup")
    void createUser(@RequestBody User user);
}