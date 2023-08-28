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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.*;


@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyController facultyController;

    @SpyBean
    private FacultyService facultyService;


    @Test
    void getFacultyInfo() throws Exception {
        Faculty faculty = new Faculty(1L, "Hogwarts", "Red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculty/1")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hogwarts"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void createFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Hogwarts", "Red");
        when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Hogwarts"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void addFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Hogwarts", "Red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty/put/1")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Hogwarts"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void removeFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Hogwarts", "Red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(delete("/faculty/dell/1")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllFaculty() throws Exception {
        List<Faculty> faculties = Arrays.asList(
                new Faculty(1L, "Hogwarts", "Red"),
                new Faculty(2L, "Slytherin", "Blue")
        );
        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(get("/faculty/findAll")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Hogwarts"))
                .andExpect(jsonPath("$[0].color").value("Red"))
                .andExpect(jsonPath("$[1].name").value("Slytherin"))
                .andExpect(jsonPath("$[1].color").value("Blue"));
    }

    @Test
    void findFaculties() throws Exception {
        when(facultyService.findByColor("Red"))
                .thenReturn(Arrays.asList(
                        new Faculty(1L, "Hogwarts", "Red")
                ));

        mockMvc.perform(get("/faculty/find-color")
                        .param("color", "Red")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Hogwarts"))
                .andExpect(jsonPath("$[0].color").value("Red"))
                .andExpect(jsonPath("$[0].student").doesNotExist());
    }

    @Test
    void findByColorOrName() throws Exception {
        when(facultyService.findByColorOrName("Red", "Red"))
                .thenReturn(Arrays.asList(
                        new Faculty(1L, "Hogwarts", "Red")
                ));

        mockMvc.perform(get("/faculty/by-color-or-name")
                        .param("color", "Red")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Hogwarts"))
                .andExpect(jsonPath("$[0].color").value("Red"))
                .andExpect(jsonPath("$[0].student").doesNotExist());
    }

    @Test
    void getByStudent() throws Exception {
        Student student = new Student(1L, "Anton", 29);
        Faculty faculty = new Faculty(1L, "Hogwarts", "Red");
        faculty.setStudent(Collections.singletonList(student));

        when(facultyService.getByStudentId(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculty/by-student?studentId=1")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hogwarts"))
                .andExpect(jsonPath("$.color").value("Red"))
                .andExpect(jsonPath("$[0].student").doesNotExist());
    }
}

