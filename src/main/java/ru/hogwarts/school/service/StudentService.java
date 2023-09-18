package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentBadRequest;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student");
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Long studentId, Student student) {
        logger.info("Was invoked method for edit student");
        student.setId(studentId);
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        boolean exists = studentRepository.existsById(id);
        if (!exists) {
            throw new StudentBadRequest();
        }
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for find all students");
        return studentRepository.findAll();
    }

    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked method for find a students by age");
        return studentRepository.findAllByAge(age);
    }

    public Collection<Student> findByBetween(int min, int max) {
        logger.info("Was invoked method for find students by min and max value");
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public void printAsync() {
        List<Student> all = studentRepository.findAll();
        System.out.println(all.get(0));
        System.out.println(all.get(1));

        Thread t1 = new Thread(() -> {
            System.out.println(all.get(2));
            System.out.println(all.get(3));
        });

        Thread t2 = new Thread(() -> {
            System.out.println(all.get(4));
            System.out.println(all.get(5));
        });

        t1.start();
        t2.start();
    }

    public void printSync() {
        List<Student> all = studentRepository.findAll();
        printSync(all.get(0));
        printSync(all.get(1));

        Thread t1 = new Thread(() -> {
            printSync(all.get(2));
            printSync(all.get(3));
        });

        Thread t2 = new Thread(() -> {
            printSync(all.get(4));
            printSync(all.get(5));
        });

        t1.start();
        t2.start();
    }

    private synchronized void printSync(Student student) {
        System.out.println(student);
    }
}
