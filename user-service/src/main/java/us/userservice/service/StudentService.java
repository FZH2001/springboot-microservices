package us.userservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import us.userservice.entity.Student;
import us.userservice.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }
    public String loadData(){
        try{
        Student s1 = new Student();
        s1.setName("Hamza Elgarai");
        s1.setCne("G133365030");
        Student s2 = new Student();
        s2.setName("Mohamed Hamdani");
        s2.setCne("R354612321");
        Student s3 = new Student();
        s3.setName("Ismail Kaou");
        s3.setCne("F36892135");
        s3.setCne("G133365030");
        studentRepository.saveAll(List.of(s1,s2,s3));
        studentRepository.flush();

        }catch(Exception e){
            return "Error : "+e.getMessage();
        }
        return "SUCCESS";
    }




}
