package ru.hogwarts.school.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @Value("${server.port}")
    private Long port;

    @GetMapping("/port")
    public Long getPort() {
        return port;
    }
}
