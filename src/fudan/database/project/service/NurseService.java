package fudan.database.project.service;

import fudan.database.project.dao.BedDAO;
import fudan.database.project.dao.ChiefNurseDAO;
import fudan.database.project.dao.PatientDAO;
import fudan.database.project.dao.WardNurseDAO;
import fudan.database.project.dao.impl.BedDAOJdbcImpl;
import fudan.database.project.dao.impl.ChiefNurseDAOJdbcImpl;
import fudan.database.project.dao.impl.PatientDAOJdbcImpl;
import fudan.database.project.dao.impl.WardNurseDAOJdbcImpl;
import fudan.database.project.entity.Bed;
import fudan.database.project.entity.ChiefNurse;
import fudan.database.project.entity.WardNurse;

import java.util.List;

public class NurseService {
    ChiefNurseDAO chiefNurseDAO = new ChiefNurseDAOJdbcImpl();
    WardNurseDAO wardNurseDAO = new WardNurseDAOJdbcImpl();
    PatientDAO patientDAO = new PatientDAOJdbcImpl();
    PatientService patientService = new PatientService();
    BedDAO bedDAO = new BedDAOJdbcImpl();
    public ChiefNurse getChiefNurseByArea(int areaId){
        return chiefNurseDAO.getChiefNurseByArea(areaId);
    }

    public List<WardNurse> getWardNursesByArea(int areaId){
        return wardNurseDAO.getWardNursesByArea(areaId);
    }

    public List<Bed> getBedsByArea(int areaId){
        return bedDAO.getBedsByArea(areaId);
    }

    public void addWardNurse(String name, String password, int areaId){
        WardNurse wardNurse = new WardNurse(name, password, areaId);
        wardNurseDAO.save(wardNurse);
        //检查是不是有可以转移的
        patientService.autoTransfer();
    }

    public String deleteWardNurse(int nurseId){
        int number = patientDAO.getPatientsByNurseId(nurseId).size();
        if(number>0) return "The nurse is still taking care of one or more patients, delete fail";
        //todo: 更多check，比如病房护士是否属于护士长分管的病区，这个id是不是一个病房护士的id
        wardNurseDAO.delete(nurseId);
        return "delete success";
    }
}
