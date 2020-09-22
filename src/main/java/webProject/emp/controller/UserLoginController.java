package webProject.emp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.sql.Array;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javaProject.utils.HttpUtil;
import javaProject.utils.JedisSentinelPoolUtil;
import javaProject.utils.OracleArray.OracleArrayUtil;
import javaProject.utils.exportExcel.ExcelUtil2;
import javaProject.utils.exportExcel.ExcelUtil3;
import redis.clients.jedis.Jedis;
import webProject.emp.entity.Emp;
import webProject.emp.entity.User;
import webProject.emp.service.UserLoginService;
import webProject.exception.CodeException;
import webProject.responseData.RequestData;
import webProject.responseData.ResponseData;

@Controller
@RequestMapping("/user")
public class UserLoginController {
	// 使用@Qualifier演示接口的多个实现如何注入，默认是实现类的类名首字母小写
	@Autowired
	@Qualifier("userLoginServiceImp")
	private UserLoginService userService;
	Logger logger = LogManager.getLogger(UserLoginController.class.getName());

	@SuppressWarnings({ "rawtypes", "unused" })
	@Autowired
	private RedisTemplate redisTemplate;

	@SuppressWarnings("unused")
	@Autowired
	private WebController webController;

	@Autowired
	HttpServletRequest request;

	/**
	 * 跳转用户登陆页面 发送请求,同时演示spring-data-redis
	 * http://localhost:8080/ssm/user/welcome.do
	 * 虚拟机http://192.168.10.12:8080/ssm/user/welcome.do
	 * @return
	 * @throws CodeException 
	 * @throws InterruptedException 
	 */
	@RequestMapping("/welcome.do")
	public String welcome() throws CodeException, InterruptedException {
		logger.info("进入登陆页面");
		// redisTemplate.boundHashOps("redisTest").put("15623642395", "1");
		// logger.info(redisTemplate.boundHashOps("redisTest").get("15623642395"));
		// redisTemplate.boundHashOps("redisTest").delete("15623642395");
		// Login login = new Login();
		// login = webController.login1(login);
		// logger.info(login);
		return "welcome";// （直接return:"abc"）的是转发到页面
	}

	/**
	 * 登陆跳转页面,对应登陆一
	 * 	接收方式一：使用String接收，每个string对应页面中的一个值
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping("/login1.do")
	public String login(HttpServletRequest request, @RequestParam("userName") String userName,
			@RequestParam("password") String password) {
		logger.info("调用入参:" + "userName:" + userName + "," + "password:" + password);

		// 先看数据库中是否有该数据
		List<User> list = userService.checkUser(userName, password);
		logger.info("sql查询返回参数：" + list);
		// 如果没有，再看redis中是否有该数据
		String pwd = "";
		if (list.size() == 0) {
			Jedis jedis = null;
			try {
				jedis = JedisSentinelPoolUtil.getJedisSentinelPool().getResource();
				logger.info("jedis返回值：" + jedis.get(userName));
				Set<String> keys = jedis.keys("*");
				for (String key : keys) {
					if (key.equals(userName)) {
						pwd = jedis.get(key);
						if (pwd.equals(password)) {
							break;
						} else {
							pwd = "";
						}
					}
				}
			} catch (Exception e) {
				logger.info("服务器内部错误:" + e.getMessage());
				request.setAttribute("msg", "服务器内部错误，请重新登陆！");
				return "login";
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
		// 登陆成功进入到富文本编译器
		if (list.size() > 0 || !"".equals(pwd)) {
			return "index";
		} else {// 登录失败，跳转到登陆页面
			request.setAttribute("msg", "账户或是密码错误，请重新登陆！");
			return "login";
		}
	}

	/**
	 * 登陆跳转页面，对应登陆二：测试mybatis的动态表名
	 * 	接收方式二：String接收json串，返回json
	 * 返回值也要跟dataType一致
	 *
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/login2.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData login2(@RequestBody String param) {
		HttpSession session = request.getSession();
		logger.info("登陆二sessionId:" + session.getId());
		ResponseData responseData = new ResponseData();
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("name", "祝浩");
		// map.put("age", 18);
		// map.put("sessionId", session.getId());
		// responseData.setDataMap(map);
		// 测试mybatis动态表名
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("name", "zhuhao");// 账户
		queryMap.put("password", "123456");// 密码
		queryMap.put("tableName", "emp");// 动态表名
		System.out.println(userService.queryByTable(queryMap));
		return responseData;
	}

	/**
	 * 登陆跳转页面，对应登陆三
	 * 	后台使用包装类接收并返回json包装类
	 * 返回json格式需要添加jackson相关的三个包
	 * 	1、jackson-annotations
	 * 	2、jackson-core
	 * 	3、jackson-databind
	 *默认是不能转换为json的
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/login3.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData login3(@RequestBody RequestData requestData) {
		ResponseData responseData = new ResponseData();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "祝浩");
		map.put("age", 18);
		responseData.setDataMap(map);
		return responseData;
	}

	/**
	 * 修改用户密码，验证ssm的事务控制
	 * 详见：https://www.cnblogs.com/aegisada/p/5485520.html
	 * @param param
	 * @return
	 * @throws CodeException 
	 */
	@RequestMapping(value = "/login4.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData login4(@RequestBody RequestData requestData) throws CodeException {
		Map<String, Object> map = requestData.getReqBody();
		String password = map.get("password").toString();
		String userName = map.get("userName").toString();
		try {
			userService.updateUser(userName, password);
		} catch (Exception e) {
			logger.info("修改失败" + e.getMessage());
			throw new CodeException("ULC001", "被除数不能为0");

		}
		ResponseData responseData = new ResponseData();
		return responseData;
	}

	/**
	 * 文件上传:form表单提交
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/fileupload.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData fileupload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ResponseData responseData = null;
		// 得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
		String savePath = "D:/file/fileLoad";
		File file = new File(savePath);
		InputStream in = null;
		FileOutputStream out = null;
		// 判断上传文件的保存目录是否存在
		if (!file.exists() && !file.isDirectory()) {
			// 创建目录
			file.mkdir();
		}
		logger.info("文件开始上传");
		// 消息提示
		try {
			// 使用Apache文件上传组件处理文件上传步骤：
			// 1、创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 2、创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 解决上传文件名的中文乱码
			upload.setHeaderEncoding("UTF-8");
			// 3、判断提交上来的数据是否是上传表单的数据
			if (!ServletFileUpload.isMultipartContent(request)) {
				// 按照传统方式获取数据
				logger.info("没有文件上传");
				throw new CodeException("ULC001", "文件没有上传");
			}
			// 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				// 如果fileitem中封装的是普通输入项的数据(除文件流之外的参数获取)
				if (item.isFormField()) {
					String name = item.getFieldName();
					// 解决普通输入项的数据的中文乱码问题
					String value = item.getString("UTF-8");
					logger.info(name + "=" + value);
				} else {// 如果fileitem中封装的是上传文件
						// 得到上传的文件名称，
					String filename = item.getName();
					logger.info("fileName:" + filename);
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					filename = filename.substring(filename.lastIndexOf("\\") + 1);
					// 获取item中的上传文件的输入流
					in = item.getInputStream();
					// 创建一个文件输出流
					out = new FileOutputStream(savePath + "\\" + filename);
					// 创建一个缓冲区
					byte buffer[] = new byte[1024];
					// 判断输入流中的数据是否已经读完的标识
					int len = 0;
					while ((len = in.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					logger.info("附件上传完成!");
					// 删除处理文件上传时生成的临时文件
					item.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
		responseData = new ResponseData();
		return responseData;
	}

	/**
	 * 导出excel:单个excel
	 * 
	 *    不能有返回值
	 *    
	 *     java servlet一个请求，只能有一个返回输出流，所以一次只能下载一个文件。
	 *    
	 *     如果确实需要下载多个Excel文件，可以先在本地生成多个Excel文件，然后使用zip压缩，
	 *     再通过一个返回输出流将zip压缩包返回给客户端。我也遇到同样的问题，这个是在别人那搜到的。
	 *     
	 * @param requestData
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/exportExcel.do")
	public void exportExcle(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		logger.info("进入导出后台");
		try {
			List<Emp> list = null;
			// 该list为了一次导入多个excel文件
			Map<String, Object> map = new HashMap<String, Object>();
			String fileName = getFileName() + ".xls";
			// 根据页面分页下载，一个excel
			int currentPage = Integer.valueOf(request.getParameter("currentPage"));// 当前页
			int pageNum = Integer.valueOf(request.getParameter("pageNum"));// 每页条数
			int start = (currentPage - 1) * pageNum;// 计算查询起始值
			int end = currentPage * pageNum;// 计算查询截止至
			map.put("start", start);
			map.put("end", end);
			list = userService.getEmp(map);
			logger.info("开始导出");
			ExcelUtil2.exportData(request, response, list, fileName);
			logger.info("导出完成");
		} catch (Exception e) {
			logger.info("下载失败:" + e.getMessage());
		}
	}

	/**
	 * 导出excel:导出多个excel
	 * 
	 * 	1、生成的excel存放到本地
	 * 	2、生成的excel打成zip包
	 * 	3、将压缩包通过浏览器下载
	 * 	4、删除上述已生成的文件
	 *    
	 * @param requestData
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/exportExcel2.do")
	public void exportExcels(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		logger.info("进入导出后台");
		List<String> fileList = new ArrayList<String>();
		try {
			List<Emp> list = null;
			// 该list为了一次导出多个excel文件
			Map<String, Object> map = new HashMap<String, Object>();
			// 分成多个excel下载
			int currentPage = 0;// 当前页
			int pageNum = 2000;// 每页条数
			// 本地文件临时存放路径
			String path = "D:" + File.separator + "file" + File.separator + "excel";
			// linux文件临时存放路径
			// String path = "/home/zhuhao/excel";
			while (true) {
				currentPage++;
				int start = (currentPage - 1) * pageNum;// 计算查询起始值
				int end = currentPage * pageNum;// 计算查询截止至
				map.put("start", start);
				map.put("end", end);
				list = userService.getEmp(map);
				if (list.size() > 0) {
					logger.info("开始导出");
					// 1、生成本地excel
					String flleName = getFileName() + ".xls";
					ExcelUtil3.exportData(request, response, list, path, flleName);
					logger.info("导出完成");
					fileList.add(path + File.separator + flleName);
					// 清空list防止内存溢出
					list.clear();
				} else {
					break;
				}
			}
			String zipFileName = getFileName();
			// 2、将本地生成的文件打成zip压缩包
			ExcelUtil3.fileToZip(path, path, zipFileName);
			fileList.add(path + File.separator + zipFileName + ".zip");
			// 3、将zip文件写到客户端
			ExcelUtil3.writeToClient(request, response, path, zipFileName + ".zip", fileList);
			// 4、删除文件
			ExcelUtil3.deleteFile(fileList);
		} catch (Exception e) {
			logger.info("下载失败:" + e.getMessage());
		}
	}

	/**
	 * 获取文件名
	 * @return
	 */
	public String getFileName() {
		// 获取文件名，使之有唯一性
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		int i = (int) (Math.random() * 900 + 100);
		String myStr = Integer.toString(i);
		String fileName = "员工报表" + sdf.format(date) + myStr;
		return fileName;
	}

	public static void main(String[] args) {
		HttpUtil.post("http://localhost:8080/ssm/user/exportExcel.do",
				"{ \"userName\":\"祝浩\",\"password\":\"123456\",\"count\":\"1\" }");
	}

	/**
	 * 断点续传--开始
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@RequestMapping(value = "/pointStart.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData pointStart(@RequestBody RequestData requestData) throws InterruptedException, IOException {
		logger.info("断点续传开始");
		ResponseData responseData = new ResponseData();
		Map<String, Object> map = requestData.getReqBody();
		Map<String, Object> rtMap = new HashMap<>();
		// 源文件与目标文件
		File sourceFile = new File("D:/", "test.txt");
		File targetFile = new File("E:/", "test.txt");
		RandomAccessFile readFile = null;
		RandomAccessFile writeFile = null;
		try {
			readFile = new RandomAccessFile(sourceFile, "rw");
			logger.info("文件大小：" + readFile.length());
			writeFile = new RandomAccessFile(targetFile, "rw");
			// 数据缓冲区
			int bt = 1024;
			if (readFile.length() <= 1024) {
				bt = (int) readFile.length();
			}
			byte[] buf = new byte[bt];
			int len = 0;
			// 数据读写
			while ((len = readFile.read(buf)) >= 0) {
				Thread.sleep(1000);
				// 读取数据库状态，看是否正常下载状态
				rtMap = userService.getPoint(map);
				logger.info("断点数据信息" + rtMap);
				// 如果此时有暂停,直接返回
				if (rtMap != null && "0".equals(rtMap.get("status").toString())) {
					// 获取此时指针位置
					long readPoint = readFile.getFilePointer();
					long writePoint = writeFile.getFilePointer();
					map.put("readPoint", readPoint);
					map.put("writePoint", writePoint);
					logger.info("修改断点位置参数：" + map);
					// 更新数据库中的指针位置
					userService.updatePoint(map);
					return responseData;
				} else {
					writeFile.write(buf, 0, len);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			readFile.close();
			writeFile.close();
		}
		return responseData;
	}

	/**
	 * 断点续传--暂停
	 * @param requestData
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@RequestMapping(value = "/pointStop.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData pointStop(@RequestBody RequestData requestData) throws InterruptedException, IOException {
		logger.info("断点续传暂停");
		ResponseData responseData = new ResponseData();
		Map<String, Object> map = requestData.getReqBody();
		logger.info("map:" + map);
		// 检查数据库中有没有记录该uuid的数据
		Map<String, Object> rtMap = userService.getPoint(map);
		// 没有数据则插入数据
		if (rtMap == null) {
			userService.pointStop(map);
			// 有数据则更新为暂停状态
		} else {
			userService.updatePoint(map);
		}
		return responseData;
	}

	/**
	 * 断点续传--重启
	 * @param requestData
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@RequestMapping(value = "/pointRestart.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData pointRestart(@RequestBody RequestData requestData) throws InterruptedException, IOException {
		logger.info("断点续传重启开始");
		ResponseData responseData = new ResponseData();
		Map<String, Object> map = requestData.getReqBody();
		Map<String, Object> rtMap = new HashMap<>();
		// 源文件与目标文件
		File sourceFile = new File("D:/", "test.txt");
		File targetFile = new File("E:/", "test.txt");
		// 将暂停状态修改为启动状态
		userService.restart(map);
		// 获取指针位置
		rtMap = userService.getPoint(map);
		RandomAccessFile readFile = null;
		RandomAccessFile writeFile = null;
		try {
			readFile = new RandomAccessFile(sourceFile, "rw");
			writeFile = new RandomAccessFile(targetFile, "rw");
			// 数据缓冲区
			int bt = 1024;
			if (readFile.length() <= 1024) {
				bt = (int) readFile.length();
			}
			byte[] buf = new byte[bt];
			readFile.seek(Integer.valueOf(rtMap.get("read_point").toString()) - bt);
			writeFile.seek(Integer.valueOf(rtMap.get("write_point").toString()));
			// 数据读写
			int len = 0;
			// 数据读写
			while ((len = readFile.read(buf)) >= 0) {
				Thread.sleep(1000);
				// 如果此时有暂停,直接返回
				if (rtMap != null && "0".equals(rtMap.get("status").toString())) {
					// 获取此时指针位置
					long readPoint = readFile.getFilePointer();
					long writePoint = writeFile.getFilePointer();
					map.put("readPoint", readPoint);
					map.put("writePoint", writePoint);
					logger.info("修改断点位置参数：" + map);
					// 更新数据库中的指针位置
					userService.updatePoint(map);
					return responseData;
				} else {
					writeFile.write(buf, 0, len);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			readFile.close();
			writeFile.close();
		}
		return responseData;
	}

	/**
	 * 使用postman等测试mybatis调用存储过程:入参为一般类型
	 * @param requestData
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryPackage.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData queryPackage(@RequestBody RequestData requestData) throws InterruptedException, IOException {
		logger.info("mybatis调用存储过程");
		ResponseData responseData = new ResponseData();
		Map<String, Object> map = requestData.getReqBody();
		logger.info("调用入参：" + map);
		Map<String, Object> rtnMap = new HashMap<>();
		// 获取所有结果集
		rtnMap = userService.queryPackage(map);
		List<Map<String, Object>> list = (List<Map<String, Object>>) rtnMap.get("out_cur");
		System.out.println("查询结果集：" + list);
		responseData.setDataMap(rtnMap);
		return responseData;
	}

	/**
	 * 使用postman等测试mybatis调用存储过程：入参为特殊类型--数组
	 * 注意：需要将Java数组转换为Oracle数组
	 * @param requestData
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/queryPackageByArray.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData queryPackageByArray(@RequestBody RequestData requestData)
			throws InterruptedException, IOException, SQLException {
		ResponseData responseData = new ResponseData();
		Map<String, Object> map = requestData.getReqBody();
		logger.info("调用入参：" + map);
		// 构造数组入参
		String[] in_dept_arr = ((String) map.get("in_dept_arr")).split(",");
		// 将Java数组转换为Oracle数组
		Array array = OracleArrayUtil.castToOracleArray(in_dept_arr);
		System.out.println("打印参数：" + array.getArray());
		Map<String, Object> rtnMap = new HashMap<>();
		// 获取所有结果集
		map.replace("in_dept_arr", array);
		rtnMap = userService.queryPackageByArray(map);
		rtnMap.remove("in_dept_arr");
		responseData.setDataMap(rtnMap);
		logger.info("返回参数：" + responseData);
		return responseData;
	}

}
