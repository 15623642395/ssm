package webProject.emp.service;

import java.util.List;
import java.util.Map;

import webProject.emp.entity.Emp;
import webProject.emp.entity.User;

public interface UserLoginService {
	public List<User> checkUser(String name, String pwd);

	public int updateUser(String name, String pwd);

	public List<Emp> getEmp(Map<String, Object> map);

	public Map<String, Object> getPoint(Map<String, Object> map);

	public int pointStop(Map<String, Object> map);

	public int updatePoint(Map<String, Object> map);

	public int restart(Map<String, Object> map);

	public Map<String, Object> queryByTable(Map<String, Object> map);

	public Map<String, Object> queryPackage(Map<String, Object> map);

	public Map<String, Object> queryPackageByArray(Map<String, Object> map);

}
