package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    @Value("${path.to.avatars.folder}")
    private Path avatarPath;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public Avatar getById(Long id) {
        return avatarRepository.findById(id)
                .orElseThrow();
    }

    public Long save(Long studentId, MultipartFile multipartFile) throws IOException {
        String absolutePath = saveInDisk(studentId, multipartFile);
        Avatar avatar = saveInDataBase(studentId, multipartFile, absolutePath);
        return avatar.getId();
    }

    private String saveInDisk(Long studentId, MultipartFile multipartFile) throws IOException {
        Files.createDirectories(avatarPath);
        String originalFilename = multipartFile.getOriginalFilename();
        int Index = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(Index);
        String fileName = studentId + extension;
        String absolutePath = avatarPath.toAbsolutePath() + "/" + fileName;
        FileOutputStream fos = new FileOutputStream(absolutePath);
        multipartFile.getInputStream().transferTo(fos);
        fos.close();
        return absolutePath;
    }

    private Avatar saveInDataBase(Long studentId, MultipartFile multipartFile, String absolutePath) throws IOException {
        Student studentReference = studentRepository.getReferenceById(studentId);
        Avatar avatar = avatarRepository.findByStudent(studentReference).orElse(new Avatar());
        avatar.setStudent(studentReference);
        avatar.setFilePath(absolutePath);
        avatar.setMediaType(multipartFile.getContentType());
        avatar.setFileSize(multipartFile.getSize());
        avatar.getData(multipartFile.getBytes());
        avatarRepository.save(avatar);
        return avatar;
    }
}
