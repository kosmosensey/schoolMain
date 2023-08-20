package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<Long> save(@RequestParam Long studentId,
                                      @RequestParam MultipartFile multipartFile) {
        try {
            Long avatarId = avatarService.save(studentId, multipartFile);
            return ResponseEntity.ok(avatarId);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/from-disk/{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void getFromDisk(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            Avatar avatar = avatarService.getById(id);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            FileInputStream fis = new FileInputStream(avatar.getFilePath());
            fis.transferTo(response.getOutputStream());
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @GetMapping("/from-db/{id}")
    public ResponseEntity<byte[]> getFromDataBase(@PathVariable("id") Long id) {
        Avatar avatar = avatarService.getById(id);
        byte[] data = avatar.getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getFileSize());
        return ResponseEntity.status(200).headers(headers).body(data);
    }
}
