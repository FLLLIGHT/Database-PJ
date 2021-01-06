package fudan.database.project.controller;

import fudan.database.project.entity.ChiefNurse;
import fudan.database.project.entity.EmergencyNurse;
import fudan.database.project.entity.Patient;
import fudan.database.project.service.PatientService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        HttpSession session = request.getSession();
        EmergencyNurse emergencyNurse = (EmergencyNurse)session.getAttribute("user");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");
        String telephone = request.getParameter("telephone");
        int evaluation = Integer.parseInt(request.getParameter("evaluation"));

        String message = patientService.registerPatient(name, address, gender, telephone, evaluation);
        request.setAttribute("message", message);
        request.getRequestDispatcher("/jsp/emergencyNurse.jsp").forward(request, response);
    }

    private void queryPatientsByArea(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        EmergencyNurse emergencyNurse = (EmergencyNurse)session.getAttribute("user");
        int areaId = Integer.parseInt(request.getParameter("areaId"));
        List<Patient> patients = patientService.getPatientsByArea(areaId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryPatientsByLifeStatus(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        EmergencyNurse emergencyNurse = (EmergencyNurse)session.getAttribute("user");
        int lifeStatus = Integer.parseInt(request.getParameter("lifeStatus"));
        List<Patient> patients = patientService.getPatientsByLifeStatus(lifeStatus);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryPatientsByEvaluation(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        EmergencyNurse emergencyNurse = (EmergencyNurse)session.getAttribute("user");
        int evaluation = Integer.parseInt(request.getParameter("evaluation"));
        List<Patient> patients = patientService.getPatientsByEvaluation(evaluation);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }
}
