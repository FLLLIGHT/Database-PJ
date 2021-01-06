package fudan.database.project.controller;

import fudan.database.project.service.PatientService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class EmergencyNurseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final PatientService patientService = new PatientService();

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

    private void registerPatient(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");
        String telephone = request.getParameter("telephone");
        int evaluation = Integer.parseInt(request.getParameter("evaluation"));

        String message = patientService.registerPatient(name, address, gender, telephone, evaluation);
        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }
}
