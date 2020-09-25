package webProject.emp.entity;

import org.springframework.stereotype.Component;

@Component
public class Login1 {
	private String name;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Login1 [name=" + name + "]";
	}

}
