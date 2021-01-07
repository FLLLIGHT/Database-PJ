package fudan.database.project.controller;

import fudan.database.project.entity.ChiefNurse;
import fudan.database.project.entity.Doctor;
import fudan.database.project.entity.Message;
import fudan.database.project.service.MessageService;
import fudan.database.project.service.PatientService;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final PatientService patientService = new PatientService();
    private final MessageService messageService = new MessageService();

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

    private void queryUnreadMessages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String type = (String) session.getAttribute("userType");
        List<Message> messages = null;
        if (type.equals("doctor")) {
            Doctor doctor = (Doctor) session.getAttribute("user");
            messages = messageService.getUnreadMessages(doctor.getDoctorId(), 1);
        } else if (type.equals("chief nurse")) {
            ChiefNurse chiefNurse = (ChiefNurse) session.getAttribute("user");
            messages = messageService.getUnreadMessages(chiefNurse.getChiefNurseId(), 2);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("messages", messages);
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);
    }

    private void markMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String type = (String) session.getAttribute("userType");
        int messageId = Integer.parseInt(request.getParameter("messageId"));
        messageService.markMessageAsRead(messageId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "success");
        JSONObject mapJson = JSONObject.fromObject(map);
        response.getWriter().print(mapJson);

    }
}
