package com.sparta.company_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableFeignClients
public class CompanyApplication {

  private static ApplicationContext context;

  public static void main(String[] args) {
    context = SpringApplication.run(CompanyApplication.class, args);
  }

  @RestController
  public class RestartController {

    @GetMapping("/restart")
    public String restart() {
      Thread thread = new Thread(() -> {
        try {
          Thread.sleep(1000);
          SpringApplication.exit(context, () -> 0);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
      thread.setDaemon(false);
      thread.start();
      return "Restarting Application...";
    }
  }
}
