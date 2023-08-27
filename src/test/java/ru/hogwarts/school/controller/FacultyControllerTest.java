package ru.hogwarts.school.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    StudentRepository studentRepository;

    @AfterEach
    void clearBD() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void createFaculty() {
        ResponseEntity<Faculty> response = createFacultyForTest("one", "red");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("one");
        assertThat(response.getBody().getColor()).isEqualTo("red");
    }

    @Test
    void addFaculty() {
        ResponseEntity<Faculty> response = createFacultyForTest("one", "red");
        Long facultyId = response.getBody().getId();

        template.put("/faculty/put/" + facultyId, new Faculty(null, "two", "blue"));

        response = template.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("two");
        assertThat(response.getBody().getColor()).isEqualTo("blue");
    }

    @Test
    void removeFaculty() {
        ResponseEntity<Faculty> response = createFacultyForTest("one", "red");
        Long facultyId = response.getBody().getId();

        template.delete("/faculty/dell/" + facultyId);

        response = template.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getFacultyInfo() {
        ResponseEntity<Faculty> response = createFacultyForTest("one", "red");
        Long facultyId = response.getBody().getId();

        response = template.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("one");
        assertThat(response.getBody().getColor()).isEqualTo("red");
    }

    @Test
    void getAllFaculty() {
        createFacultyForTest("one", "red");
        createFacultyForTest("two", "blue");

        ResponseEntity<Collection> response = template
                .getForEntity("/faculty/findAll", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    void findByColorOrName() {
        createFacultyForTest("one", "red");
        createFacultyForTest("two", "blue");

        ResponseEntity<Collection> response = template
                .getForEntity("/faculty/by-color-or-name?color=red", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        Map<String, String> next = (HashMap) response.getBody().iterator().next();
        assertThat(next.get("color")).isEqualTo("red");
    }

    @Test
    void findFaculties() {
        createFacultyForTest("one", "red");
        createFacultyForTest("two", "blue");

        ResponseEntity<Collection> response = template
                .getForEntity("/faculty/find-color?color=blue", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        Map<String, String> next = (HashMap) response.getBody().iterator().next();
        assertThat(next.get("color")).isEqualTo("blue");
    }

    @Test
    void getByStudent() {
        ResponseEntity<Faculty> response = createFacultyForTest("one", "red");
        Student student = new Student(null, "Anton", 20);
        Faculty faculty = response.getBody();
        student.setFaculty(faculty);
        ResponseEntity<Student> studentResponse = template.postForEntity("/student", student, Student.class);
        assertThat(studentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long StudentId = studentResponse.getBody().getId();

        response = template.getForEntity("/faculty/by-student?studentId=" + StudentId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(faculty);
    }

    private ResponseEntity<Faculty> createFacultyForTest(String name, String color) {
        ResponseEntity<Faculty> response = template.postForEntity("/faculty",
                new Faculty(null, name, color),
                Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response;
    }
}