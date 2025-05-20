package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class OlaController {

    private static final String template = "Ola, %s";
    private static final AtomicLong counter = new AtomicLong();

    @RequestMapping("/ola")
    public Ola ola(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Ola(counter.incrementAndGet(), String.format(template, name));
    }

}
