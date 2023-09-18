package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.LongStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/info")
public class InfoController {

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Value("${server.port}")
    private int port;

    @GetMapping("/get-port")
    public int grtPort() {
        return port;
    }

    @GetMapping("/calculate")
    public void calculate() {
        long startTime = System.currentTimeMillis();
        Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
        long timeConsumed = System.currentTimeMillis() - startTime;
        logger.info("первое время: " + timeConsumed);


        startTime = System.currentTimeMillis();
        LongStream.rangeClosed(1, 1_000_000)
                .sum();
        timeConsumed = System.currentTimeMillis() - startTime;
        logger.info("второе время: " + timeConsumed);
    }
}
