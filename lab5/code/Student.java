import java.util.*;

public class Student {

	University University;
	private String sID;
	private String sName;
	private float avg;
	private Course[] courses;
	Collection<Log> log;

	public float getAvg() {
		return this.avg;
	}

	/**
	 * 
	 * @param avg
	 */
	public void setAvg(float avg) {
		this.avg = avg;
	}

	public String getSID() {
		return this.sID;
	}

	/**
	 * 
	 * @param sid
	 */
	public void setSID(String sid) {
		this.sID = sid;
	}

	public String getSName() {
		return this.sName;
	}

	/**
	 * 
	 * @param name
	 */
	public void setSName(String name) {
		this.sName = name;
	}

	public Course[] getStudents() {
		// TODO - implement Student.getStudents
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param students
	 */
	public void setStudents(Course[] students) {
		// TODO - implement Student.setStudents
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param sid
	 * @param name
	 * @param avg
	 * @param students
	 */
	public Student(String sid, String name, float avg, Course[] students) {
		// TODO - implement Student.Student
		throw new UnsupportedOperationException();
	}

}