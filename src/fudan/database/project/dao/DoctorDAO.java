package fudan.database.project.dao;

import fudan.database.project.entity.Doctor;

import java.util.List;

public interface DoctorDAO {
    public List<Doctor> getAll();
    public void update(Doctor doctor);
    public Doctor get(String name);
    public Doctor get(int areaId);
}
