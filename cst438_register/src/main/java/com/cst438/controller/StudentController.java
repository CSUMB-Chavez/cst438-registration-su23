package com.cst438.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {
	
	
	@Autowired
	StudentRepository studentRepository;
	//Test to verify working student controller
	@GetMapping("/student")
	public StudentDTO getStudent(@RequestParam("student_id") int student_id) {
		System.out.println("/student");
		Student find = studentRepository.findById(student_id).orElse(null);
		if (find != null) {
			System.out.println("/schedule student "+ find.getName()+" "+find.getStudent_id());
			
			StudentDTO s = createStudentDTO(find);
			return s;
		} else {
			System.out.println("/schedule student not found. "+student_id);
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student not found. " );
		}
		
	}
	
	//Code to add a student to the database, check if email is already in sue
	@PostMapping("/student")
	@Transactional
	public StudentDTO addNewStudent(@RequestBody StudentDTO student) {
		
		Student check = studentRepository.findByEmail(student.student_email);
		if (check == null) {
			Student newStudent = new Student();
			newStudent.setName(student.student_name);
			newStudent.setEmail(student.student_email);
			studentRepository.save(newStudent);
			StudentDTO result = createStudentDTO(newStudent);
			return result;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Email is already registered: .  " + student.student_email);
		}
	}
	

	@PostMapping("/student/status")
	@Transactional
	public StudentDTO changeStatus(@RequestBody StudentDTO student) {
		
		Student studentStatus = studentRepository.findById(student.id).orElse(null);
		
		if ( studentStatus != null) {
			studentStatus.setStatus(student.status);
			studentStatus.setStatusCode(student.status_code);
			studentRepository.save(studentStatus);
			StudentDTO result = createStudentDTO(studentStatus);
			return result;
		} else {
			System.out.println("/schedule student not found. "+student.id);
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student not found. " );
		}
	}
		
	
	
	private StudentDTO createStudentDTO(Student s){
		StudentDTO sdto = new StudentDTO();
		sdto.id = s.getStudent_id();
		sdto.student_name = s.getName();
		sdto.student_email = s.getEmail();
		sdto.status_code = s.getStatusCode();
		return sdto;
	}
}
