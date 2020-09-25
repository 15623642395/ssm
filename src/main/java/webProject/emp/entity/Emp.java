package webProject.emp.entity;

public class Emp {
	private String id;
	private String createdtime;
	private String deptid;
	private String email;
	private String gender;
	private String login_account;
	private String password;
	private String phone;
	private String status;
	private String updatetime;
	private String user_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(String createdtime) {
		this.createdtime = createdtime;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLogin_account() {
		return login_account;
	}

	public void setLogin_account(String login_account) {
		this.login_account = login_account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	@Override
	public String toString() {
		return "Emp [id=" + id + ", createdtime=" + createdtime + ", deptid=" + deptid + ", email=" + email
				+ ", gender=" + gender + ", login_account=" + login_account + ", password=" + password + ", phone="
				+ phone + ", status=" + status + ", updatetime=" + updatetime + ", user_name=" + user_name + "]";
	}

}
