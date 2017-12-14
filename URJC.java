package practica4;

import java.util.ArrayList;

import material.maps.Entry;
import material.maps.HashTableMapSC;

public class URJC {
	private HashTableMapSC<String,Degree> tablaGrados;
	private HashTableMapSC<String,Student> tablaAlumnos;
	private HashTableMapSC<Student,Degree> tablaAlumnosEnGrados;
	
	public URJC() {
		tablaGrados = new HashTableMapSC<String,Degree>();
		tablaAlumnos = new HashTableMapSC<String,Student>();
		tablaAlumnosEnGrados = new HashTableMapSC<Student,Degree>();
	}
	
	public void insertDegree(Degree degree) {
		tablaGrados.put(degree.getKey(), degree);
	}
	
	public void insertStudent(Student student, Degree degree) {
		tablaAlumnos.put(student.getStudentRecord(), student);
		tablaAlumnosEnGrados.put(student, degree);
	}
	
	/**
	 * Recovers all the students enrolled in a given degree
	 * @param degree the degree to search
	 * @return an iterable collections with the students
	 */
	public Iterable<Student> students(Degree degree) {
		Iterable<Entry<Student,Degree>> entradas=tablaAlumnosEnGrados.entries();
		ArrayList<Student> estudiantes = new ArrayList<>();
		for(Entry<Student,Degree> entrada: entradas) {
			if (entrada.getValue().getKey() == degree.getKey() && entrada.getValue().getValue()== degree.getValue()) {
				estudiantes.add(entrada.getKey());
			}
		}
		return estudiantes;
	}
	
	/**
	 * Recovers all the degrees currently in the university
	 * @return an iterable collection with all the degrees
	 */
	public Iterable<Degree> degrees() {
		return tablaGrados.values();
	}

	/**
	 * Recover the degree in which a student is enrolled
	 * @param student the student to check the degree
	 * @return the degree of the student
	 */
	public Degree degree(Student student) {
		return tablaAlumnosEnGrados.get(student);
	}
	
	/**
	 * Recovers the students with a gred larger or equal than
	 * the given one
	 * @param minGrade the minimum grade to be considered a good student
	 * @return an iterable collection with the best students
	 */
	public Iterable<Student> bestStudents(float minGrade) {
		Iterable<Entry<String,Student>> entradas = tablaAlumnos.entries();
		ArrayList<Student> bests = new ArrayList<>();
		for(Entry<String,Student> entrada: entradas) {
			if(entrada.getValue().getAvgGrade()>=minGrade) {
				bests.add(entrada.getValue());
			}
		}
		return bests;
	}
	
	/**
	 * Recovers a degree based on its id
	 * @param idDegree the id of the degree
	 */
	public Degree degreeInfo(String idDegree) {		
		return tablaGrados.get(idDegree);
	}
	
	/**
	 * Recovers a student based on its record number
	 * @param studentRecord the record of the student
	 */
	public Student studentInfo(String studentRecord) {
		return tablaAlumnos.get(studentRecord);
	}
}
