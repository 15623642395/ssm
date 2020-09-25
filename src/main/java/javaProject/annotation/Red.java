package javaProject.annotation;

import org.springframework.stereotype.Component;

/**
 * 应用了注解类的类
 * 
 * @author 56525
 *
 */
@Component
public class Red {
	@MyValue(value = "红色")
	private String color;
	@MyValue(value = "月季")
	public String name;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Red [color=" + color + ",name=" + name + "]";
	}

}
