package fudan.database.project.controller;

import fudan.database.project.entity.ChiefNurse;
import fudan.database.project.entity.Doctor;
import fudan.database.project.entity.EmergencyNurse;
import fudan.database.project.entity.WardNurse;
import fudan.database.project.service.AccountService;
import net.sf.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AccountServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AccountService accountService = new AccountService();

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
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String type = request.getParameter("type");

        HttpSession session = request.getSession();
        switch (type) {
            case "doctor":
                Doctor doctor = accountService.checkDoctor(name, password);
                session.setAttribute("user", doctor);
                break;
            case "chief nurse":
                ChiefNurse chiefNurse = accountService.checkChiefNurse(name, password);
                session.setAttribute("user", chiefNurse);
                break;
            case "ward nurse":
                WardNurse wardNurse = accountService.checkWardNurse(name, password);
                session.setAttribute("user", wardNurse);
                break;
            case "emergency nurse":
                EmergencyNurse emergencyNurse = accountService.checkEmergencyNurse(name, password);
                session.setAttribute("user", emergencyNurse);
                break;
        }

        if(session.getAttribute("user") == null){
            request.setAttribute("loginStatus", "false");
            request.setAttribute("message", "There is something wrong with your <b>username or password</b>, please check and try again!");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }else{
            session.setAttribute("userType", type);
            request.setAttribute("loginStatus", "true");
            request.setAttribute("message", "Success!");
        }
        switch (type){
            case "doctor":
                request.getRequestDispatcher("/jsp/doctor.jsp").forward(request, response);
                break;
            case "chief nurse":
                request.getRequestDispatcher("/jsp/chiefNurse.jsp").forward(request, response);
                break;
            case "ward nurse":
                request.getRequestDispatcher("/jsp/wardNurse.jsp").forward(request, response);
                break;
            case "emergency nurse":
                request.getRequestDispatcher("/jsp/emergencyNurse.jsp").forward(request, response);
                break;
        }
    }


    private void changeInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        accountService.changeInfo(session, name, password);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("messages", "success");
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);

    }


}
