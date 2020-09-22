package webProject.emp.serviceImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import webProject.emp.dao.UserLoginDao;
import webProject.emp.entity.Emp;
import webProject.emp.entity.User;
import webProject.emp.service.UserLoginService;

//@Transactional
@Service
public class UserLoginServiceImp implements UserLoginService {
	@Autowired
	private UserLoginDao userDao;
	@Autowired
	private User user;

	Logger logger = LogManager.getLogger(UserLoginServiceImp.class.getName());

	/**
	 * 校验用户账号，密码
	 */
	public List<User> checkUser(String name, String pwd) {

		user.setName(name);
		user.setPassword(pwd);
		List<User> list = new ArrayList<User>();
		list = userDao.checkUser(user);
		logger.info("list:" + list);
		return list;
	}

	/**
	 * 分页查询
	 */
	@Override
	public List<Emp> getEmp(Map<String, Object> map) {
		List<Emp> list = new ArrayList<Emp>();
		list = userDao.getEmp(map);
		return list;
	}

	/**
	 * 修改用户名和密码
	 * @param name
	 * @param pwd
	 * @return
	 */
	@SuppressWarnings("unused")
	@Override
	@Transactional(noRollbackFor = { IllegalStateException.class, Exception.class })
	public int updateUser(String name, String pwd) {
		user.setName(name);
		user.setPassword(pwd);
		int i = 0;
		i = userDao.updateUser(user);
		int j = 2 / 0;
		return i;
	}

	@Override
	public Map<String, Object> getPoint(Map<String, Object> map) {
		Map<String, Object> rtMap = new HashMap<String, Object>();
		rtMap = userDao.getPoint(map);
		return rtMap;
	}

	@Override
	public int pointStop(Map<String, Object> map) {
		System.out.println("map:" + map);
		int count = userDao.insertPointStatus(map);
		return count;
	}

	@Override
	public int updatePoint(Map<String, Object> map) {
		int count = userDao.updatePoint(map);
		return count;
	}

	@Override
	public int restart(Map<String, Object> map) {
		int count = userDao.restart(map);
		return count;
	}

	/**
	 * 测试动态表名
	 */
	@Override
	public Map<String, Object> queryByTable(Map<String, Object> map) {
		return userDao.queryByTable(map);
	}

	/**
	 * 测试mybatis调用存储过程:有入参和出参
	 */
	public Map<String, Object> queryPackage(Map<String, Object> map) {
		userDao.queryPackage(map);
		return map;
	}

	/**
	 * 测试mybatis调用存储过程:数组作为入参
	 */
	@Override
	public Map<String, Object> queryPackageByArray(Map<String, Object> map) {
		userDao.queryPackageByArray(map);
		return map;
	}

}
