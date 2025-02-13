package ru.hogwarts.school.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.Service.InfoService;

@RestController
public class InfoController {

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    public final InfoService infoService;

    @Value("${server.port}")
    private Long port;

    @GetMapping("/port")
    public Long getPort() {
        return port;
    }

    @GetMapping("/formula")
    private long getFastestAnswer() {
        return infoService.getFastestAnswer();
    }
}
