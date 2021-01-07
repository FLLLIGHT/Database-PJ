package fudan.database.project.controller;

import fudan.database.project.entity.ChiefNurse;
import fudan.database.project.entity.Doctor;
import fudan.database.project.entity.Patient;
import fudan.database.project.entity.WardNurse;
import fudan.database.project.service.NurseService;
import fudan.database.project.service.PatientService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final PatientService patientService = new PatientService();
    private final NurseService nurseService = new NurseService();
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

    private void queryPatientsWaitingToTransfer(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor)session.getAttribute("user");
        List<Patient> patients = patientService.getPatientsWaitingToTransfer(doctor.getAreaId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryAllPatientsInArea(HttpServletRequest request, HttpServletResponse response) throws IOException{
        System.out.println("=============================");
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor)session.getAttribute("user");
        List<Patient> patients = patientService.getPatientsByArea(doctor.getAreaId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryPatientsByLifeStatus(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor)session.getAttribute("user");
        int lifeStatus = Integer.parseInt(request.getParameter("lifeStatus"));
        List<Patient> patients = patientService.getPatientsByAreaAndLifeStatus(doctor.getAreaId(), lifeStatus);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryChiefNurse(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor)session.getAttribute("user");
        ChiefNurse chiefNurse = nurseService.getChiefNurseByArea(doctor.getAreaId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("chiefNurse", chiefNurse);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryWardNurse(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor)session.getAttribute("user");
        List<WardNurse> wardNurses = nurseService.getWardNursesByArea(doctor.getAreaId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("wardNurses", wardNurses);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryPatientsByNurse(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor)session.getAttribute("user");
        int nurseId = Integer.parseInt(request.getParameter("nurseId"));
        List<Patient> patients = patientService.getPatientsByNurseId(nurseId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void updateEvaluationOfPatient(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor)session.getAttribute("user");
        int evaluation = Integer.parseInt(request.getParameter("evaluation"));
        String patientId = request.getParameter("patientId");
        patientService.updateEvaluationOfPatient(patientId, evaluation);
        String message = patientService.moveArea(patientId, evaluation);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void updateLifeStatusOfPatient(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor)session.getAttribute("user");
        int lifeStatus = Integer.parseInt(request.getParameter("lifeStatus"));
        String patientId = request.getParameter("patientId");
        patientService.updateLifeStatusOfPatient(patientId, lifeStatus);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("messages", "success");
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);

    }

    private void addTestResult(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor)session.getAttribute("user");
        String patientId = request.getParameter("patientId");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(request.getParameter("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = request.getParameter("result");
        int evaluation = Integer.parseInt(request.getParameter("evaluation"));
        patientService.addTestResult(patientId, date, result, evaluation);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "success");
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryPatientsWaitingToDischarge(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor)session.getAttribute("user");
        List<Patient> patients = patientService.getPatientsWaitingToDischargeByArea(doctor.getAreaId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void dischargePatient(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor)session.getAttribute("user");
        String patientId = request.getParameter("patientId");
        patientService.dischargePatient(patientId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("messages", "success");
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);

    }

}
