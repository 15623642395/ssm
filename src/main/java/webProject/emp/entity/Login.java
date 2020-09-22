package webProject.emp.entity;

public class Login {
	private String userName;
	private String password;
	private int count;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Login [userName=" + userName + ", password=" + password + ", count=" + count + "]";
	}

}
