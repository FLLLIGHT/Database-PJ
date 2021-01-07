package fudan.database.project.service;

import fudan.database.project.dao.*;
import fudan.database.project.dao.impl.*;
import fudan.database.project.entity.ChiefNurse;
import fudan.database.project.entity.Doctor;
import fudan.database.project.entity.EmergencyNurse;
import fudan.database.project.entity.WardNurse;

import javax.servlet.http.HttpSession;

public class AccountService {

    private final ChiefNurseDAO chiefNurseDAO = new ChiefNurseDAOJdbcImpl();
    private final DoctorDAO doctorDAO = new DoctorDAOJdbcImpl();
    private final EmergencyNurseDAO emergencyNurseDAO = new EmergencyNurseDAOJdbcImpl();
    private final WardNurseDAO wardNurseDAO = new WardNurseDAOJdbcImpl();

    public void changeInfo(HttpSession session, String name, String password){
        String type = (String) session.getAttribute("userType");
        switch (type){
            case "doctor":
                Doctor doctor = (Doctor)session.getAttribute("user");
                if(!name.equals("")) doctor.setName(name);
                if(!password.equals("")) doctor.setPassword(password);
                doctorDAO.update(doctor);
                session.setAttribute("user", doctor);
                break;
            case "chief nurse":
                ChiefNurse chiefNurse = (ChiefNurse)session.getAttribute("user");
                if(!name.equals("")) chiefNurse.setName(name);
                if(!password.equals("")) chiefNurse.setPassword(password);
                chiefNurseDAO.update(chiefNurse);
                session.setAttribute("user", chiefNurse);
                break;
            case "ward nurse":
                WardNurse wardNurse = (WardNurse)session.getAttribute("user");
                if(!name.equals("")) wardNurse.setName(name);
                if(!password.equals("")) wardNurse.setPassword(password);
                wardNurseDAO.update(wardNurse);
                session.setAttribute("user", wardNurse);
                break;
            case "emergency nurse":
                EmergencyNurse emergencyNurse = (EmergencyNurse)session.getAttribute("user");
                if(!name.equals("")) emergencyNurse.setName(name);
                if(!password.equals("")) emergencyNurse.setPassword(password);
                emergencyNurseDAO.update(emergencyNurse);
                session.setAttribute("user", emergencyNurse);
                break;
        }
    }

    public Doctor checkDoctor(String name, String password){
        Doctor doctor = doctorDAO.get(name);
        if(doctor==null) {
            System.out.println("doctor not found");
            return null;
        }
        if(doctor.getPassword().equals(password)) {
            System.out.println("yes");
            return doctor;
        }
        System.out.println("password false");
        return null;
    }

    public ChiefNurse checkChiefNurse(String name, String password){
        ChiefNurse chiefNurse = chiefNurseDAO.get(name);
        if(chiefNurse==null) {
            System.out.println("chief nurse not found");
            return null;
        }
        if(chiefNurse.getPassword().equals(password)) {
            System.out.println("yes");
            return chiefNurse;
        }
        System.out.println("password false");
        return null;
    }

    public EmergencyNurse checkEmergencyNurse(String name, String password){
        EmergencyNurse emergencyNurse = emergencyNurseDAO.get(name);
        if(emergencyNurse==null) {
            System.out.println("emergency nurse not found");
            return null;
        }
        if(emergencyNurse.getPassword().equals(password)) {
            System.out.println("yes");
            return emergencyNurse;
        }
        System.out.println("password false");
        return null;
    }

    public WardNurse checkWardNurse(String name, String password){
        WardNurse wardNurse = wardNurseDAO.get(name);
        if(wardNurse==null) {
            System.out.println("ward nurse not found");
            return null;
        }
        if(wardNurse.getPassword().equals(password)) {
            System.out.println("yes");
            return wardNurse;
        }
        System.out.println("password false");
        return null;
    }

}
