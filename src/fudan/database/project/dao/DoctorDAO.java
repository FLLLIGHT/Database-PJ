package fudan.database.project.dao;

import fudan.database.project.entity.Doctor;

import java.util.List;

public interface DoctorDAO {
    public List<Doctor> getAll();

    public Doctor get(String name);
}
