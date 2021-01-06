package fudan.database.project.dao;

import fudan.database.project.entity.EmergencyNurse;

import java.util.List;

public interface EmergencyNurseDAO {
    public List<EmergencyNurse> getAll();

    public EmergencyNurse get(String name);
}
