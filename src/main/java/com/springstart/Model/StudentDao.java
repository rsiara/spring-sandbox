package com.springstart.Model;

import com.springstart.Model.Entity.Student;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class StudentDao {

    private static Map<Integer, Student> students;

    static{
        students = new HashMap<Integer,Student>(){
            {
                put(1, new Student(1, "Said", "Computer Science"));
                put(2, new Student(2, "Alex U", "Finance"));
                put(3, new Student(3, "Anna", "Maths"));
            }
        };
    }

    public Collection<Student> getAllStudents(){
        return this.students.values();
    }

    public Student geStudentById(int id) {
        return this.students.get(id);
    }

    public void create(Student student) {
        students.put(student.getId(),student);
    }

    public void removeAllStudents() {
        students.clear();
    }

    public boolean exists(Student student) {
        return students.containsKey(student.getId());
    }

    public void delete(Integer id){
        students.remove(id);
    }

    public void update(Student student) {
        students.put(student.getId(), student);
    }
}