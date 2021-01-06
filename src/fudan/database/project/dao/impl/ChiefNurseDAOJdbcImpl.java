package fudan.database.project.dao.impl;

import fudan.database.project.dao.ChiefNurseDAO;
import fudan.database.project.dao.DAO;
import fudan.database.project.entity.ChiefNurse;
import fudan.database.project.entity.Doctor;

import java.util.List;

public class ChiefNurseDAOJdbcImpl extends DAO<ChiefNurse> implements ChiefNurseDAO {
    @Override
    public List<ChiefNurse> getAll() {
        String sql = "SELECT chiefNurseId, name, password, areaId FROM chief_nurse";
        return getForList(sql);
    }

    @Override
    public ChiefNurse get(String name) {
        String sql = "SELECT chiefNurseId, name, password, areaId FROM chief_nurse WHERE name = ?";
        return get(sql, name);
    }

    @Override
    public ChiefNurse getChiefNurseByArea(int areaId) {
        String sql = "SELECT chiefNurseId, name, password, areaId FROM chief_nurse WHERE areaId = ?";
        return get(sql, areaId);
    }
}
