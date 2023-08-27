package ru.hogwarts.school.controller;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @Autowired
    private TestRestTemplate template;


    @Test
    void createStudent() {
        ResponseEntity<Student> response = createStudentForTest("Anton", 29);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Anton");
        assertThat(response.getBody().getAge()).isEqualTo(29);
    }

    @Test
    void addStudent() {
        ResponseEntity<Student> response = createStudentForTest("Anton", 29);
        Long studentId = response.getBody().getId();

        template.put("/student/put/" + studentId, new Student(null, "Nikita", 30));

        response = template.getForEntity("/student/" + studentId, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Nikita");
        assertThat(response.getBody().getAge()).isEqualTo(30);
    }

    @Test
    void removeStudent() {
        ResponseEntity<Student> response = createStudentForTest("Anton", 29);
        Long studentId = response.getBody().getId();

        template.delete("/student/dell/" + studentId);

        response = template.getForEntity("/student/" + studentId, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void getStudentInfo() {
        ResponseEntity<Student> response = createStudentForTest("Anton", 29);
        Long studentId = response.getBody().getId();

        response = template.getForEntity("/student/" + studentId, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Anton");
        assertThat(response.getBody().getAge()).isEqualTo(29);
    }

    @Test
    void findStudents() {
        createStudentForTest("Anton", 29);
        createStudentForTest("Nikita", 30);

        ResponseEntity<Collection> response = template
                .getForEntity("/student/find?age=29", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        Map<String, Integer> next = (HashMap) response.getBody().iterator().next();
        assertThat(next.get("age")).isEqualTo(29);
    }

    @Test
    void getAllStudents() {
        createStudentForTest("Anton", 29);
        createStudentForTest("Nikita", 30);

        ResponseEntity<Collection> response = template
                .getForEntity("/student/findAll", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    void findByBetween() {
        ResponseEntity<Student> student1 = createStudentForTest("Anton", 25);
        ResponseEntity<Student> student2 = createStudentForTest("Nikita", 15);
        ResponseEntity<Collection> response = template
                .getForEntity("/student/find-min-and-max?min=20&max=30", Collection.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).size().isEqualTo(1);
    }

    private ResponseEntity<Student> createStudentForTest(String name, int age) {
        ResponseEntity<Student> response = template.postForEntity("/student",
                new Student(null, name, age),
                Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AssertionsForClassTypes.assertThat(response.getBody()).isNotNull();
        return response;
    }
}
