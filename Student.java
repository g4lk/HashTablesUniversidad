package practica4;

public class Student {
	

	private final String studentRecord;
	private String name;
	private String lastName; 
	private float avgGrade;
	
	public Student(String studentRecord, String name, String lastName, float avgGrade) {
		this.studentRecord=studentRecord;
		this.name=name;
		this.lastName=lastName;
		this.avgGrade=avgGrade;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public float getAvgGrade() {
		return avgGrade;
	}

	public void setAvgGrade(float avgGrade) {
		this.avgGrade = avgGrade;
	}

	public String getStudentRecord() {
		return studentRecord;
	}
	

}
