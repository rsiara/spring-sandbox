package com.springstart.Controller;

import com.springstart.Model.Entity.Student;
import com.springstart.Service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;

@RestController
@RequestMapping(value = "/students")
public class StudentController {

    private final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private StudentService studentService;

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Collection<Student> getAllStudents() {
        logger.info("GET all students invoked");
        return studentService.getAllStudents();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public Student getStudentById(@PathVariable("id") int id) {
        logger.info("GET student id: " + id + " invoked");
        return studentService.getStudentById(id);
    }


    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> createStudent(@RequestBody Student student, UriComponentsBuilder ucBuilder) {
        logger.info("Creata student id: " + student.getId() + " name: " + student.getName() + " course:" + student.getCourse());

        if (studentService.exists(student)) {
            logger.info("a student with name " + student.getName() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        studentService.createStudent(student);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setLocation(ucBuilder.path("/students/{id}").buildAndExpand(student.getId()).toUri());

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Integer id, @RequestBody Student student) {
        logger.info("Update student with id: " + id);

        if (studentService.exists(student) == false) {
            logger.info("Student with id " + student.getName() + " is not exist");
            return new ResponseEntity<Student>(HttpStatus.NOT_FOUND);
        }
        studentService.updateStudent(student);

        return new ResponseEntity<Student>(student, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Integer id) {
        logger.info("Delete student with id: " + id);
        Student student = studentService.getStudentById(id);

        if (student == null) {
            logger.info("a student with id: " + id + " doesn't exist");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        studentService.deleteStudent(id);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public StudentService getStudentService() {
        return studentService;
    }


}
