package com.springstart.Utils;

import com.springstart.Model.Entity.Student;
import com.springstart.Model.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class StreamSandbox  {

    private String[] stringArray;
    private Object[] studentArray;

    private StudentDao studentDao;

    {
        stringArray = new String[]{"a1", "a2", "b1", "c2", "c1"};
    }

    public void doSomething(){
        List<String> stringList = Arrays.asList(stringArray);
        studentArray =  studentDao.getAllStudents().toArray();

        stringList
                .stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);
    }

    public StudentDao getStudentDao() {
        return studentDao;
    }

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }
}
