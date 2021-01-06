package fudan.database.project.dao.impl;

import fudan.database.project.dao.DAO;
import fudan.database.project.dao.DoctorDAO;
import fudan.database.project.entity.Doctor;

import java.util.List;

public class DoctorDAOJdbcImpl extends DAO<Doctor> implements DoctorDAO {
    @Override
    public List<Doctor> getAll() {
        String sql = "SELECT doctorId, name, password, areaId FROM doctor";
        return getForList(sql);
    }

    @Override
    public Doctor get(String name) {
        String sql = "SELECT doctorId, name, password, areaId FROM doctor WHERE name = ?";
        return get(sql, name);
    }
}
