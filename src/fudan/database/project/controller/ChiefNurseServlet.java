package fudan.database.project.controller;

import fudan.database.project.entity.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChiefNurseServlet  extends HttpServlet {

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

    private void queryAllPatientsInArea(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        ChiefNurse chiefNurse = (ChiefNurse)session.getAttribute("user");
        List<Patient> patients = patientService.getPatientsByArea(chiefNurse.getAreaId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryPatientsByLifeStatus(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        ChiefNurse chiefNurse = (ChiefNurse)session.getAttribute("user");
        int lifeStatus = Integer.parseInt(request.getParameter("lifeStatus"));
        List<Patient> patients = patientService.getPatientsByAreaAndLifeStatus(chiefNurse.getAreaId(), lifeStatus);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryPatientsWaitingToTransfer(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        ChiefNurse chiefNurse = (ChiefNurse)session.getAttribute("user");
        List<Patient> patients = patientService.getPatientsWaitingToTransfer(chiefNurse.getAreaId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryWardNurse(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        ChiefNurse chiefNurse = (ChiefNurse)session.getAttribute("user");
        List<WardNurse> wardNurses = nurseService.getWardNursesByArea(chiefNurse.getAreaId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("wardNurses", wardNurses);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryPatientsByNurse(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        ChiefNurse chiefNurse = (ChiefNurse)session.getAttribute("user");
        int nurseId = Integer.parseInt(request.getParameter("nurseId"));
        List<Patient> patients = patientService.getPatientsByNurseId(nurseId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryBeds(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        ChiefNurse chiefNurse = (ChiefNurse)session.getAttribute("user");
        List<Bed> beds = nurseService.getBedsByArea(chiefNurse.getAreaId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beds", beds);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void queryPatientByBed(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        ChiefNurse chiefNurse = (ChiefNurse)session.getAttribute("user");
        int bedId = Integer.parseInt(request.getParameter("bedId"));
        Patient patient = patientService.getPatientByBed(bedId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patient", patient);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }
}
