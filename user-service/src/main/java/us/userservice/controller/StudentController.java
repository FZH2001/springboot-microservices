package us.userservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.userservice.entity.Student;
import us.userservice.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping("/get")
    public List<Student> getStudents(){
        return studentService.getStudents();
    }

    @GetMapping("/load-data")
    public String loadData(){
        return studentService.loadData();
    }


}
