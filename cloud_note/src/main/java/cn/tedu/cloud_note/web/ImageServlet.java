package cn.tedu.cloud_note.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImageServlet
 */
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
         
    public ImageServlet() {
        super();
       
    }

	protected void doGet
			(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//发送图片
		byte[] png = createPng();
		response.setContentType("image/png");
		response.setContentLength(png.length);
		//在消息body中发送消息数据
		response.getOutputStream().write(png);
	}

	
	protected void doPost
			(HttpServletRequest request, HttpServletResponse response) 
					throws ServletException, IOException {
		
		doGet(request, response);
	}
	/**
	 * 创建一张图片并且编码为png格式
	 * 并且返回编码数据
	 * @throws IOException 
	 *  
	 */
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

}
