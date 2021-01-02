package fudan.database.project.controller;

import fudan.database.project.dao.UserDAO;
import fudan.database.project.dao.UserDAOJdbcImpl;
import fudan.database.project.entity.User;
import fudan.database.project.service.AccountService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

public class AccountServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        String methodName = requestURI.substring(requestURI.lastIndexOf("/") + 1);

        try {
            Method method = getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userName = request.getParameter("userName");
        String pass = request.getParameter("pass");
        User user = accountService.checkUser(userName, pass);

        if(user == null){
            request.setAttribute("loginStatus", "false");
            request.setAttribute("message", "There is something wrong with your <b>username or password</b>, please check and try again!");
        }else{
            request.setAttribute("loginStatus", "true");
            request.setAttribute("message", "Success!");
        }
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }
}
