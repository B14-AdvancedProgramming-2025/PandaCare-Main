package id.ac.ui.cs.advprog.b14.pandacare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PandaCareApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandaCareApplication.class, args);
    }

}
