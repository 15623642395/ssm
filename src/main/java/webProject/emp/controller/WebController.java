package webProject.emp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import javaProject.utils.HttpUtil;
import webProject.emp.entity.Login;
import webProject.emp.entity.Login1;
import webProject.emp.entity.OnlineData;
import webProject.emp.entity.ThreadBean;
import webProject.emp.entity.UpLoad;
import webProject.emp.entity.User;
import webProject.emp.util.UrlUploadUtil;
import webProject.exception.CodeException;
import webProject.responseData.ResponseData;

/**
 * 该类专门用jmeter模拟前台页面测试
 * 
 * 	jmeter的访问地址是
 * 		
 * 		http://localhost:8080/ssm/web/web1.do
 * @ResponseBody
 * 		该注解告诉返回的是json
 * 		不加该注解默认返回的是页面
 * @author 56525
 *
 */
@Controller
@RequestMapping("/web")
public class WebController {

	Object object = new Object();
	Logger logger = LogManager.getLogger(WebController.class.getName());
	@Autowired
	private ObjectFactory<User> objectFactory;

	@Autowired
	private ThreadBean threadBean;

	@Autowired
	private Login1 login1;

	@Autowired
	private UrlUploadUtil urlUploadUtil;

	/**
	 * 使用jmeter测试
	 * 
	 * 测试使用javaBean，验证线程安全问题
	 * 	结论：javaBean作为形参传入时，即使该对象拥有全局变量也不会存在安全问题
	 *
	 * 
	 * @param requestData
	 * @return
	 * @throws InterruptedException 
	 */
	@RequestMapping(value = "/web1.do", method = RequestMethod.POST)
	@ResponseBody
	public Login login1(@RequestBody Login login) throws InterruptedException {
		int count = (int) (1 + Math.random() * 100);
		Thread.sleep(1000);
		logger.info(Thread.currentThread().getName() + ",count：" + count);
		Thread.sleep(1000);
		login.setCount(count);
		Thread.sleep(1000);
		logger.info(Thread.currentThread().getName() + ",出参：" + login.getCount());
		return login;
	}

	/**
	 * 使用jmeter、和postMan测试
	 * 
	 * 	现象： 1、postman出参入参一致
	 * 		  2、jmeter出参入参不一致
	 * 	结论：postman是串行，不能测试并发状况
	 * 		 jmeter是并行，可以测试并发状况
	 * @param requestData
	 * @return
	 * @throws InterruptedException 
	 */
	@RequestMapping(value = "/web2.do", method = RequestMethod.POST)
	@ResponseBody
	public Login login2(@RequestBody Login login) throws InterruptedException {
		logger.info(Thread.currentThread().getName() + "入参:" + login.getUserName());
		Thread.sleep(1000);
		login1.setName("姓名:" + login.getUserName());
		Thread.sleep(1000);
		logger.info(Thread.currentThread().getName() + "出参:" + login1.getName());
		return login;
	}

	/**
	 * 使用jmeter测试
	 * 
	 * 解决多线程下，JavaBean全局变量问题
	 * 
	 * 方法一：
	 * 
	 * 测试使用javaBean，验证线程安全问题
	 * 	1、将User变为多例模式
	 * 	2、使用objectFactory包装注入
	 * 结论：单独将User变为多例直接注入User会存在安全问题，要配合objectFactory使用
	 * 
	 * @param login
	 * @return
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "/web3.do", method = RequestMethod.POST)
	@ResponseBody
	public Login login3(@RequestBody Login login) throws InterruptedException {
		User user = objectFactory.getObject();
		logger.info(Thread.currentThread().getName() + ":入参" + login.getUserName());
		Thread.sleep(1000);
		user.setName(login.getUserName());
		Thread.sleep(1000);
		logger.info(Thread.currentThread().getName() + ":user出参" + user.getName());
		return login;
	}

	/**
	 * 使用jmeter测试
	 *
	 * 解决多线程下，JavaBean全局变量问题
	 * 
	 * 方法二：
	 * 
	 * 测试使用javaBean，验证线程安全问题
	 * 	使用ThreadLocal解决安全问题
	 */
	int i = 0;

	@RequestMapping(value = "/web4.do", method = RequestMethod.POST)
	@ResponseBody
	public Login login4(@RequestBody Login login) throws InterruptedException {
		logger.info(":入参" + login.getUserName());
		// Thread.sleep(1000);
		threadBean.setUserName(login.getUserName());
		threadBean.setPassWord(login.getPassword());
		// Thread.sleep(1000);
		logger.info(":出参" + threadBean.getUserName());
		// Thread.sleep(1000);
		logger.info(":" + threadBean);
		return login;
	}

	/**
	 * 测试多线程下载文件
	 * 	1、文件重名之后会重命名文件下载
	 * 	2、只要子线程下载失败则删除该文件
	 * 测试从linux中下载附件
	 * 	1、下载地址：http://192.168.10.12:8080/ssm/image/test.jpg
	 * 	2、下载路径：D:\file\fileLoad(本地) 虚拟机:/home/zhuhao
	 * 	3、文件名称：1.jpg
	 * 	4、线程数：4
	 * 
	 * 
	 * @param upLoad
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws CodeException 
	 */
	@RequestMapping(value = "/web5.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData threadUploadFile(@RequestBody UpLoad upLoad)
			throws InterruptedException, IOException, CodeException {
		Long start = System.currentTimeMillis();
		boolean flag = urlUploadUtil.uploadFile(upLoad.getDownloadUrl(), upLoad.getFilePath(), upLoad.getFileName(),
				upLoad.getFileName());
		ResponseData responseData = null;
		if (!flag) {
			throw new CodeException("WC002", "文件下载失败");
		} else {
			responseData = new ResponseData();
		}
		Long end = System.currentTimeMillis();
		logger.info("下载完成" + Thread.currentThread().getName() + ",总共耗时:" + (end - start) / 1000);
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
				throw new CodeException("WC001", "文件没有上传");
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
	 * 测试接受json数组
	 * json数组使用list<map>接受
	 * 测试数据
	 *  {
	 *   "businessCode":"ZHUHAO",
	 *   "systemId":"ZHU",
	 *   "params":[
	 *			{
	 *				"phone":"15623642395",
	 *				"name":"zhangsan"
	 *			},
	 *			{
	 *				"phone":"17317439309",
	 *				"name":"lisi"
	 *			}
	 *		
	 *		  ]
	 *   }
	 * 
	 * @param onlineData
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@RequestMapping(value = "/web7.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData json(@RequestBody OnlineData onlineData) throws InterruptedException, IOException {
		logger.info(onlineData.getBusinessCode());
		logger.info(onlineData.getSystemId());
		logger.info(onlineData.getParams());
		for (int i = 0; i < onlineData.getParams().size(); i++) {
			logger.info(onlineData.getParams().get(i).get("phone"));
			logger.info(onlineData.getParams().get(i).get("name"));
		}
		ResponseData responseData = null;
		responseData = new ResponseData();
		return responseData;
	}

	/**
	 * @ResponseBody注解会将返回的对象以JSON格式输出给前台
	 * 返回json字符串
	 * 	@ResponseBody注解会将返回的对象转换为json对象
	 * @param onlineData
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@RequestMapping(value = "/web8.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> toJson(@RequestBody OnlineData onlineData) throws InterruptedException, IOException {
		Map<String, Object> map1 = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		map1.put("phone", "15623642395");
		map1.put("name", "zhangsan");
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("phone", "17317439309");
		map2.put("name", "lisi");
		list.add(map1);
		list.add(map2);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("businessCode", "ZHUHAO");
		map.put("systemId", "ZHU");
		map.put("params", list);
		return map;
	}

	/**
	 * 测试Get请求
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@RequestMapping(value = "/web9.do", method = RequestMethod.GET)
	@ResponseBody
	public ResponseData test1() throws InterruptedException, IOException {
		// object=new Object();
		logger.info("进入web7测试");
		synchronized (object) {
			Thread.sleep(10000);
		}
		logger.info("web7测试完成");
		ResponseData responseData = null;
		responseData = new ResponseData();
		return responseData;
	}

	/**
	 * 测试工具类
	 */
	@RequestMapping(value = "/web100.do", method = RequestMethod.POST)
	@ResponseBody
	public String test(@RequestBody OnlineData onlineData) throws InterruptedException, IOException {
		Map<String, Object> map1 = new HashMap<String, Object>();
		logger.info("**************************日志开始*************************");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		map1.put("phone", "15623642395");
		map1.put("name", "zhangsan");
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("phone", "17317439309");
		map2.put("name", "lisi");
		list.add(map1);
		list.add(map2);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("businessCode", "ZHUHAO");
		map.put("systemId", "ZHU");
		map.put("params", list);
		String json = JSON.toJSON(map).toString();
		logger.info(json);
		logger.info("**************************日志结束*************************");
		return json;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					HttpUtil.post("http://localhost:8080/ssm/web/web100.do",
							"{ \"receive\":\"565253584@qq.com\",\"subject\":\"activeMq邮件发送\",\"message\":\"activeMq邮件发送\"  }");
				}
			}).start();

		}

	}
}
