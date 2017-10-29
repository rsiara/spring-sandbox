package com.springstart.tests;

import com.springstart.Configuration.RootConfig;
import com.springstart.Controller.StudentController;
import com.springstart.Model.Entity.Student;
import com.springstart.Model.StudentDao;
import com.springstart.Service.StudentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class StudentControllerTest {

    private MockMvc mockMvc;
    private MediaType contentType;
    @Autowired
    private StudentDao studentDao;
    @Mock
    private StudentService studentService;
    @InjectMocks
    private StudentController studentController;

    {
        contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),
                Charset.forName("utf8"));
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        studentDao.removeAllStudents();
        System.out.println("Test init method is executed before every test");
    }

    // =========================================== Get All Student =========================================
    @Test
    public void getAllStudentsTest() throws Exception {
        Collection<Student> students = Arrays.asList(
                new Student(1, "Adam", "English"),
                new Student(2, "Kamil", "Biology")
        );
        //Record
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Adam")))
                .andExpect(jsonPath("$[0].course", is("English")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Kamil")))
                .andExpect(jsonPath("$[1].course", is("Biology")));


        verify(studentService, times(1)).getAllStudents();
        verifyNoMoreInteractions(studentService);
    }

    // =========================================== Update Student =========================================
    @Test
    public void updateStudentSuccessTest() throws Exception {
        Student student = new Student(1, "Adam", "English");

        when(studentService.exists(student)).thenReturn(true);
        doNothing().when(studentService).updateStudent(student);

        mockMvc.perform(
                put("/students/{id}", student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(student)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(student.getId())))
                .andExpect(jsonPath("$.name", is(student.getName())))
                .andExpect(jsonPath("$.course", is(student.getCourse()))
                );
        verify(studentService, times(1)).exists(student);
        verify(studentService, times(1)).updateStudent(student);

        verifyNoMoreInteractions(studentService);

    }

    @Test
    public void updateStudentFailureTest() throws Exception {
        Student student = new Student(1, "Adam", "English");

        when(studentService.exists(student)).thenReturn(false);

        mockMvc.perform(
                put("/students/{id}", student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(student)))
                .andExpect(status().isNotFound()
                );
        verify(studentService, times(1)).exists(student);
        verifyNoMoreInteractions(studentService);

    }

    // =========================================== Get User By ID =========================================
    @Test
    public void getStudentByIdSuccessTest() throws Exception {


        Student student = new Student(1, "Adam", "English");

        when(studentService.getStudentById(student.getId())).thenReturn(student);

        mockMvc.perform(get("/students/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Adam")))
                .andExpect(jsonPath("$.course", is("English")));


        verify(studentService, times(1)).getStudentById(student.getId());
        verifyNoMoreInteractions(studentService);
    }


    public void getStudentByIdFailureTest() throws Exception {

        Student student = new Student(1, "Adam", "English");

        when(studentService.getStudentById(student.getId())).thenReturn(null);

        mockMvc.perform(get("/students/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        verify(studentService, times(1)).getStudentById(student.getId());
        verifyNoMoreInteractions(studentService);
    }

    // =========================================== Delete User =========================================
    @Test
    public void deleteStudentSuccessTest() throws Exception {

        Student student = new Student(1, "Adam", "English");

        when(studentService.getStudentById(student.getId())).thenReturn(student);
        doNothing().when(studentService).deleteStudent(student.getId());

        mockMvc.perform(
                delete("/students/{id}", student.getId()))
                .andExpect(status().isOk());

        verify(studentService, times(1)).getStudentById(student.getId());
        verify(studentService, times(1)).deleteStudent(student.getId());

        verifyZeroInteractions(studentService);

    }

    @Test
    public void deleteStudentFailureTest() throws Exception {

        Student student = new Student(1, "Adam", "English");

        when(studentService.getStudentById(student.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/students/{id}", student.getId()))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).getStudentById(student.getId());

        verifyZeroInteractions(studentService);
    }

    // =========================================== Create User  =========================================
    @Test
    public void createStudentSuccessTest() throws Exception {

        Student student = new Student(1, "Adam", "English");

        when(studentService.exists(student)).thenReturn(false);
        doNothing().when(studentService).createStudent(student);

        mockMvc.perform(
                post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(student)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("http://localhost/students/" + student.getId()))
                );
        verify(studentService, times(1)).exists(student);
        verify(studentService, times(1)).createStudent(student);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void createStudentFailureTest() throws Exception {

        Student student = new Student(1, "Adam", "English");

        when(studentService.exists(student)).thenReturn(true);

        mockMvc.perform(
                post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(student)))
                .andExpect(status().isConflict());

        verify(studentService, times(1)).exists(student);
        verifyNoMoreInteractions(studentService);
    }



    // =========================================== Mockito Spy Example  =========================================
    @Test
    public void mockitoSpyExampleTest() throws Exception {

        Student student = spy(new Student(1, "Adam", "English"));
        when(student.getName()).thenReturn("MOCKITO TEST");

        assertEquals("student should return MOCKITO TEST", "MOCKITO TEST", "MOCKITO TEST" );

        when(studentService.exists(student)).thenReturn(false);
        doNothing().when(studentService).createStudent(student);


    }

/*    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

    protected String asJsonString(Object o) throws IOException {

        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter
                = new MappingJackson2HttpMessageConverter();
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(
                o, contentType, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
