package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

  @RequestMapping("/auth/login")
  public String login(@RequestParam String userId) {
    System.out.println("############################################ login");
    if(userId.equals("admin")) {

      Authentication authentication = new UserAuthentication(userId, null, null);
      String token = JwtTokenProvider.generateToken(authentication);

      return token;
    }
    return "????????";
  }

  @RequestMapping("/main")
  public String main() {
return "main";
  }
}
