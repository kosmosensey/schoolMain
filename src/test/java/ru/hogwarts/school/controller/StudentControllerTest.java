package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void getStudentInfo() throws Exception {
        Student student = new Student(1L, "Anton", 29);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/get/1")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anton"))
                .andExpect(jsonPath("$.age").value(29));
    }

    @Test
    void createStudent() throws Exception {
        Student student = new Student(1L, "Anton", 29);
        when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anton"))
                .andExpect(jsonPath("$.age").value(29));
    }

    @Test
    void addStudent() throws Exception {
        Student student = new Student(1L, "Anton", 29);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.put("/student/put/1")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anton"))
                .andExpect(jsonPath("$.age").value(29));
    }

    @Test
    void removeStudent() throws Exception {
        Student student = new Student(1L, "Anton", 29);
        when(studentRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/dell/1")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findStudents() throws Exception {
        int age = 29;
        Student anton = new Student(1L, "Anton", age);
        when(studentRepository.findAllByAge(age)).thenReturn(Collections.singletonList(anton));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/find?age=" + age)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Anton"))
                .andExpect(jsonPath("$[0].age").value(age));
    }



    @Test
    void findByBetween() throws Exception {
        when(studentRepository.findAllByAgeBetween(20, 30))
                .thenReturn(Arrays.asList(
                        new Student(1L, "Anton", 29),
                        new Student(2L, "Nikita", 22)
                ));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/find-min-and-max?min=20&max=30")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Anton"))
                .andExpect(jsonPath("$[1].name").value("Nikita"));
    }

    @Test
    void getAllStudents() throws Exception {
        List<Student> students = Arrays.asList(
                new Student(1L, "Anton", 29),
                new Student(2L, "Nikita", 22)
        );
        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/findAll")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Anton"))
                .andExpect(jsonPath("$[0].age").value(29))
                .andExpect(jsonPath("$[1].name").value("Nikita"))
                .andExpect(jsonPath("$[1].age").value(22));
    }
}
