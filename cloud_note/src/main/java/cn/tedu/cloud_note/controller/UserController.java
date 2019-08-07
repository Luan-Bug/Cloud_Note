package cn.tedu.cloud_note.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.naming.directory.DirContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.tedu.cloud_note.entity.User;
import cn.tedu.cloud_note.service.PasswordException;
import cn.tedu.cloud_note.service.UserNameException;
import cn.tedu.cloud_note.service.UserNotFoundException;
import cn.tedu.cloud_note.service.UserService;
import cn.tedu.cloud_note.uitl.JSONResult;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController{
	
	@Resource
	UserService userservice;
	
	@RequestMapping("/login.do")
	@ResponseBody
	public Object login (
			String name,String password,HttpSession session) {
			User user = userservice.Login(name, password);
			//System.out.println(new JSONResult(user));
			//登录成功时候, 将user信息保存到session
		    //用于在过滤器中检查登录情况
		    session.setAttribute("loginUser", user); 
			return new JSONResult(user);
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Object addUser(
			String name, String nick, String password, String confirm) {
		User user = userservice.regist(name, nick, password, confirm);
		return new JSONResult(user);
	}
	
	@RequestMapping("/heartbeat.do")
	@ResponseBody
	public Object heartbeat(){
	    Object ok = "ok";
	    return new JSONResult(ok);
	}
	
	
	/*
	 *  responseBody会自动处理返回值
	 *  1、如果是javabean（集合，数值）会自动换成json
	 *  2、如果返回值是数组直接添加到body中
	 *   produces="image/png"用于设置Content-Type的值
	 */
	//显示生成的图片
	@RequestMapping(value="/image.do",produces="image/png")
	@ResponseBody
	public byte[] image() throws IOException {
		return createPng();
	}
	//下载图片
	@RequestMapping(value="/downloadimg.do",
			produces="application/octet-stream")
	@ResponseBody
	public byte[] download(HttpServletResponse res) throws IOException {
		res.setHeader("content-disposition","attachment; filename=\"demo.png\"");
		return createPng();
	}
	//生成图片
	private byte[] createPng() throws IOException {
		BufferedImage image = 
				new BufferedImage(200, 80, BufferedImage.TYPE_3BYTE_BGR);
		image.setRGB(100, 40, 0xffffff);
		//将图片编码为png
		ByteArrayOutputStream out = 
				new ByteArrayOutputStream();
		ImageIO.write(image, "png", out);
		out.close();
		byte[] data = out.toByteArray();
		return data;
	}
	
	//下载表格
	@RequestMapping(value="/downloadexcel.do",
			produces="application/octet-stream")
	@ResponseBody
	public byte[] excel(HttpServletResponse res) throws Exception {
		res.setHeader("content-disposition","attachment; filename=\"demo.xls\"");
		return createexcel();
	}
	
	//上传文件
	@RequestMapping("/uploadimg.do")
	@ResponseBody
	public JSONResult upload (
			MultipartFile userfile1,
			MultipartFile userfile2) throws Exception{
		//Spring MVC 中可以利用multipartFile
		//接受上载的文件，
		//文件中的一切数据都可以从multipartfile中找到
		
		String file1name = userfile1.getOriginalFilename();
		String file2name = userfile2.getOriginalFilename();
		System.out.println("上载的原始文件名"+file1name);
		System.out.println("上载的原始文件名"+file2name);
		//3种文件的保存方法
		//1、userfile1.transferTo(目标文件)
		File dir = new File("F:/demo");
		dir.mkdirs();
		File file1 = new File(dir,file1name);
		File file2 = new File(dir,file2name);
		userfile1.transferTo(file1);	
		userfile2.transferTo(file2);
		//2、userfile1.getBytes()获取文件的全部内容
		// 将文件读取到内存适合处理小文件
		//userfile1.getBytes();	
		//3、userfile1.getInputStream();
		//获取上载文件的流，适合处理大文件
		//不带缓冲区复制
		/*
		InputStream in1 = userfile1.getInputStream();	
		FileOutputStream fileOutputStream =
				new FileOutputStream(file1);
		int b;
		while(((b=in1.read())!=-1)) {
			fileOutputStream.write(b);
		}
		in1.close();
		fileOutputStream.close();
		//带缓冲区复制
		InputStream in2 = userfile2.getInputStream();
		FileOutputStream out2 = 
				new FileOutputStream(file2);
		byte[] buf = new byte[8*1024];
		int n ;
		while ((n=in2.read(buf))!=-1) {
			out2.write(buf,0,n);
		}
		in2.close();
		out2.close();*/
		return new JSONResult(true);
	}
	
	
	//生成表格	
	private byte[] createexcel() throws Exception {
		//创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		//创建工作表
		HSSFSheet sheet = 
					workbook.createSheet("Hello World");
		//创建工作行
		HSSFRow row = 
				sheet.createRow(0);
		//创建行中的列
		HSSFCell cell = 
				 row.createCell(0);
		cell.setCellValue("Hello World");
		//将excel文件保存到dyte数组
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		out.close();
		return out.toByteArray();
	}	
	
	
	
	/*
	 *处理注册时账号异常 
	 * 
	 */
	
	@ExceptionHandler(UserNameException.class)
	@ResponseBody
	public JSONResult handleUserNameException(Exception e) {
		return new JSONResult(4,e);
	}
	
	/*
	 *  处理账号异常
	 * 
	 */
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseBody
	public JSONResult handleUserNotFound(Exception e) {
		return new JSONResult(2,e);
		
	}
	/*
	  *  处理密码异常 
	 */
	@ExceptionHandler(PasswordException.class)
	@ResponseBody
	public JSONResult handlePassword(Exception e) {
		return new JSONResult(3, e);
	}
	
}
