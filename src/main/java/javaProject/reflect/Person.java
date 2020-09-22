package javaProject.reflect;

public class Person {
	public String name;
	private Integer age;
	public String addr = "abcbcd";
	private String info = "nbbdeu";
	public int x;
	public int y;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Person other = (Person) obj;
		if (x != other.x) {
			return false;
		}

		if (y != other.y) {
			return false;
		}

		return true;
	}

	public Person() {

	}

	public Person(int age) {
		this.age = age;
	}

	public Person(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Person(int age, String name) {
		this.age = age;
		this.name = name;
	}

	public Person(String name, int age, String addr, String info) {
		this.name = name;
		this.age = age;
		this.addr = addr;
		this.info = info;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", addr=" + addr + ", info=" + info + "]";
	}
}
