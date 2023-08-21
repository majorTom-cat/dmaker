package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {

    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<String> getAllDevelopers() {
        // GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");
        System.out.println("called");

        return Arrays.asList("Snow", "Elsa", "Olaf");
    }

    //@PostMapping("/create-developer")
    @RequestMapping(value="/", method = {RequestMethod.GET, RequestMethod.POST})
    public List<String> createDeveloper() {
        // POST /developers HTTP/1.1
        System.out.println("post called");
        log.info("POST /create-developer HTTP/1.1");

        dMakerService.createDeveloper();


        return Arrays.asList("Snow", "Elsa", "Olaf");
    }
}
