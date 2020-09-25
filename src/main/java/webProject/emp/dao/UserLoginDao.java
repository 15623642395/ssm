package webProject.emp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import webProject.emp.entity.Emp;
import webProject.emp.entity.User;

@Mapper
public interface UserLoginDao {
	public List<User> checkUser(User user);

	public int updateUser(User user);

	public List<Emp> getEmp(Map<String, Object> map);

	public Map<String, Object> getPoint(Map<String, Object> map);

	public int insertPointStatus(Map<String, Object> map);

	public int updatePoint(Map<String, Object> map);

	public int restart(Map<String, Object> map);

	public Map<String, Object> queryByTable(Map<String, Object> map);

	public Map<String, Object> queryPackage(Map<String, Object> map);

	public Map<String, Object> queryPackageByArray(Map<String, Object> map);

}
