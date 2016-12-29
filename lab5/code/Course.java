import java.util.*;

public class Course {
	private University University;
	private String cID;
	private String cName;
	private int credit;
	private Student[] students;
	Collection<Log> log;

	public String getCID() {
		return this.cID;
	}

	/**
	 * 
	 * @param cid
	 */
	public void setCID(String cid) {
		this.cID = cid;
	}

	public String getCName() {
		return this.cName;
	}

	/**
	 * 
	 * @param name
	 */
	public void setCName(String name) {
		this.cName = name;
	}

	public Student[] getCourses() {
		// TODO - implement Course.getCourses
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param courses
	 */
	public void setCourses(Student[] courses) {
		// TODO - implement Course.setCourses
		throw new UnsupportedOperationException();
	}

	public int getCredit() {
		return this.credit;
	}

	/**
	 * 
	 * @param credit
	 */
	public void setCredit(int credit) {
		this.credit = credit;
	}

	/**
	 * 
	 * @param cid
	 * @param name
	 * @param credit
	 * @param courses
	 */
	public Course(String cid, String name, int credit, Student[] courses) {
		// TODO - implement Course.Course
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param student
	 */
	public void addStudent(Student student) {
		// TODO - implement Course.addStudent
		throw new UnsupportedOperationException();
	}

}