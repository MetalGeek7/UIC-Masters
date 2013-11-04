package testConnection;

import javax.persistence.*;

@Entity
@Table (name = "student")
public class Student implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "UIN")
	private long uin;
	@Column(name = "name")
	private String name;
	@Column(name = "age")
	private Integer age;
	@Column(name = "school")
	private String school;

	public Student() {
	}

	public Student(long uin) {
		this.uin = uin;
	}

	public Student(long uin, String name, Integer age, String school) {
		this.uin = uin;
		this.name = name;
		this.age = age;
		this.school = school;
	}

	public long getUin() {
		return this.uin;
	}

	public void setUin(long uin) {
		this.uin = uin;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSchool() {
		return this.school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

}
