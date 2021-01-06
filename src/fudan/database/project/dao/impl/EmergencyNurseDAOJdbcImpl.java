package fudan.database.project.dao.impl;

import fudan.database.project.dao.DAO;
import fudan.database.project.dao.EmergencyNurseDAO;
import fudan.database.project.entity.Doctor;
import fudan.database.project.entity.EmergencyNurse;

import java.util.List;

public class EmergencyNurseDAOJdbcImpl extends DAO<EmergencyNurse> implements EmergencyNurseDAO {
    @Override
    public List<EmergencyNurse> getAll() {
        String sql = "SELECT emergency_nurse_id, name, password FROM emergency_nurse";
        return getForList(sql);
    }

    @Override
    public EmergencyNurse get(String name) {
        String sql = "SELECT emergency_nurse_id, name, password FROM emergency_nurse WHERE name = ?";
        return get(sql, name);
    }
}

