package cn.tedu.cloud_note.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.tedu.cloud_note.entity.User;

public class AccessFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}
	private String login = "/log_in.html";
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = 
                (HttpServletRequest)request;
        HttpServletResponse res =
                (HttpServletResponse)response;
        HttpSession session = req.getSession();
        
        //�Ź� log_in.html
        String path = req.getRequestURI();
        System.out.println("access:"+path);
        if(path.endsWith(login)){
            chain.doFilter(request, response);
            return;
        }
        
        //�Ź�  alert_error.html
        if(path.endsWith("alert_error.html")){
            chain.doFilter(request, response);
            return;
        }
        
        //����û��Ƿ��¼
        User user = (User)session
                .getAttribute("loginUser");
        //���û�е�¼���ض��� ��¼ҳ
        if(user==null){//û�е�¼
            //�ض��򵽵�¼ҳ
            res.sendRedirect(
                req.getContextPath()+login);
            return;
        }
        //�����¼�ͷŹ�
        chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}