package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.ranges.RangeException;
import ru.hogwarts.school.exception.FacultyBadRequest;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for add faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty updateFaculty(Long facultyId, Faculty faculty) {
        logger.info("Was invoked method for update faculty");
        faculty.setId(facultyId);
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("Was invoked method for find faculty");
        return facultyRepository.findById(id).get();
    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty");
        boolean exists = facultyRepository.existsById(id);
        if (!exists) {
            throw new FacultyBadRequest();
        }
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculty() {
        logger.info("Was invoked method for find all faculty");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

    public Collection<Faculty> findByColorOrName(String color, String name) {
        logger.info("Was invoked method for find faculty by color or name");
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    public Optional<Faculty> getByStudentId(Long studentId) {
        logger.info("Was invoked method for find faculty by student id");
        return facultyRepository.findByStudent_Id(studentId);
    }

}
