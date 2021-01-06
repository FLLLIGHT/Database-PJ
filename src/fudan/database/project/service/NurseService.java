package fudan.database.project.service;

import fudan.database.project.dao.ChiefNurseDAO;
import fudan.database.project.dao.WardNurseDAO;
import fudan.database.project.dao.impl.ChiefNurseDAOJdbcImpl;
import fudan.database.project.dao.impl.WardNurseDAOJdbcImpl;
import fudan.database.project.entity.ChiefNurse;
import fudan.database.project.entity.WardNurse;

import java.util.List;

public class NurseService {
    ChiefNurseDAO chiefNurseDAO = new ChiefNurseDAOJdbcImpl();
    WardNurseDAO wardNurseDAO = new WardNurseDAOJdbcImpl();
    public ChiefNurse getChiefNurseByArea(int areaId){
        return chiefNurseDAO.getChiefNurseByArea(areaId);
    }

    public List<WardNurse> getWardNursesByArea(int areaId){
        return wardNurseDAO.getWardNursesByArea(areaId);
    }
}
