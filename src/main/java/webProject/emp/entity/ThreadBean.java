package webProject.emp.entity;

import org.springframework.stereotype.Component;

@Component
public class ThreadBean {

	ThreadLocal<String> password = new ThreadLocal<String>();
	ThreadLocal<String> userName = new ThreadLocal<String>();

	public String getUserName() {
		return userName.get();
	}

	public String getPassword() {
		return password.get();
	}

	public void setUserName(String name) {
		userName.set(name);
	}

	public void setPassWord(String pwd) {
		password.set(pwd);
	}

	@Override
	public String toString() {
		return "ThreadBean [password=" + password.get() + ", userName=" + userName.get() + "]";
	}

}
