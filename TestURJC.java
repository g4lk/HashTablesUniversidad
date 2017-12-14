package tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import practica4.Degree;
import practica4.Student;
import practica4.URJC;

public class TestURJC {
	
	private Degree gic;
	private Degree gis;
	private Degree gii;
	private Student gic1;
	private Student gic2;
	private Student gii1;
	private Student gis1;
	private Student gis2;
	private Student gis3;
	private URJC urjc;

	@Before
	public void setUp() throws Exception {
		gic = new Degree("gic", "Grado en Ingenieria de Computadores");	
		gis = new Degree("gis", "Grado en Ingenieria del Software");
		gii = new Degree("gii", "Grado en Ingenieria en Informatica");
		gic1 = new Student("gic1", "NombreGIC1", "ApellidoGIC1", 9.8f);
		gic2 = new Student("gic2", "NombreGIC2", "ApellidoGIC2", 7.0f);
		gii1 = new Student("gii1", "NombreGII1", "ApellidoGIC1", 8.0f);
		gis1 = new Student("gis1", "NombreGIS1", "ApellidoGIS1", 5.0f);
		gis2 = new Student("gis2", "NombreGIS2", "ApellidoGIS2", 10.0f);
		gis3 = new Student("gis3", "NombreGIS3", "ApellidoGIS3", 6.0f);
		urjc = new URJC();
		urjc.insertDegree(gic);
		urjc.insertDegree(gii);
		urjc.insertDegree(gis);
		urjc.insertStudent(gic1, gic);
		urjc.insertStudent(gic2, gic);
		urjc.insertStudent(gii1, gii);
		urjc.insertStudent(gis1, gis);
		urjc.insertStudent(gis3, gis);
		urjc.insertStudent(gis2, gis);
	}

	@Test
	public void testStudents() {
		Set<Student> expected = new HashSet<>();
		expected.add(gis1);
		expected.add(gis2);
		expected.add(gis3);
		for(Student s: urjc.students(gis)) {
			assertTrue(expected.contains(s));
			expected.remove(s);
		}
		assertTrue(expected.isEmpty());
	}

	@Test
	public void testDegrees() {
		Set<Degree> expected = new HashSet<>();
		expected.add(gis);
		expected.add(gic);
		expected.add(gii);
		for (Degree s : urjc.degrees()) {
			assertTrue(expected.contains(s));
			expected.remove(s);
		}
		assertTrue(expected.isEmpty());
	}

	@Test
	public void testDegree() {
		assertEquals(gis, urjc.degree(gis1));
		assertEquals(gii, urjc.degree(gii1));
		assertEquals(gic, urjc.degree(gic2));
	}

	@Test
	public void testBestStudents() {
		Set<Student> expected = new HashSet<>();
		expected.add(gis2);
		expected.add(gii1);
		expected.add(gic1);
		for (Student s : urjc.bestStudents(8.0f)) {
			assertTrue(expected.contains(s));
			expected.remove(s);
		}
		assertTrue(expected.isEmpty());
	}

	@Test
	public void testDegreeInfo() {
		assertEquals(gis, urjc.degreeInfo("gis"));
	}

	@Test
	public void testStudentInfo() {
		assertEquals(gic1, urjc.studentInfo("gic1"));
	}

}
