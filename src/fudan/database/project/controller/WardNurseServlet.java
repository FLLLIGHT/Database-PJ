package fudan.database.project.controller;

import fudan.database.project.entity.Doctor;
import fudan.database.project.entity.Patient;
import fudan.database.project.entity.WardNurse;
import fudan.database.project.service.PatientService;
import net.sf.json.JSONObject;

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

public class WardNurseServlet extends HttpServlet {

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

    private void queryRelatedPatients(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        WardNurse wardNurse = (WardNurse) session.getAttribute("user");
        List<Patient> patients = patientService.getPatientsByNurseId(wardNurse.getWardNurseId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patients", patients);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void addDailyRecord(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        WardNurse wardNurse = (WardNurse) session.getAttribute("user");
        String patientId = request.getParameter("patientId");
        float temperature = Float.parseFloat(request.getParameter("temperature"));
        String symptom = request.getParameter("symptom");
        int lifeStatus = Integer.parseInt(request.getParameter("lifeStatus"));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = df.parse(request.getParameter("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        patientService.addDailyRecord(patientId, temperature, symptom, lifeStatus, date);
    }

}
