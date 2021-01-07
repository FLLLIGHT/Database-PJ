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
    public void update(Doctor doctor) {
        String sql = "UPDATE doctor SET name = ?, password = ? WHERE doctorId = ?";
        update(sql, doctor.getName(), doctor.getPassword(), doctor.getDoctorId());
    }

    @Override
    public Doctor get(String name) {
        String sql = "SELECT doctorId, name, password, areaId FROM doctor WHERE name = ?";
        return get(sql, name);
    }

    @Override
    public Doctor get(int areaId) {
        String sql = "SELECT doctorId, name, password, areaId FROM doctor WHERE areaId = ?";
        return get(sql, areaId);
    }
}
