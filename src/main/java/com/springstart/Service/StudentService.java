package com.springstart.Service;

import com.springstart.Model.Entity.Student;
import com.springstart.Model.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StudentService {
    private StudentDao studentDao;

    public Collection<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }

    public Student getStudentById(int id) {
        return studentDao.geStudentById(id);
    }

    public StudentDao getStudentDao() {
        return studentDao;
    }

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }


    public void createStudent(Student student) {
        studentDao.create(student);
    }

    public boolean exists(Student student) {
       return studentDao.exists(student);
    }

    public void deleteStudent(Integer id) {
        studentDao.delete(id);
    }

    public void updateStudent(Student student) {
        studentDao.update(student);
    }
}