package com.springstart.tests;

import com.springstart.Configuration.RootConfig;
import com.springstart.Model.Entity.Student;
import com.springstart.Model.StudentDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class StudentDaoTest {

    @Autowired
    private StudentDao studentDao;

    @Before
    public void init() {
        System.out.println("Test init method is executed before every test");
        studentDao.removeAllStudents();
    }

    @Test
    public void dummyTest() {
        assertEquals("Dummy test", 1, 1);
    }

    @Test
    public void testCreateStudent() {
        Student newStudent = new Student(1, "Adam", "Nowicki");
        studentDao.create(newStudent);

        Collection<Student> students = studentDao.getAllStudents();
        assertEquals("Number of students shoud be one", 1, students.size());


    }
}