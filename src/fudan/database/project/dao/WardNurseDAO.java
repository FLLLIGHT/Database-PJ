package fudan.database.project.dao;

import fudan.database.project.entity.WardNurse;

import java.util.List;

public interface WardNurseDAO {
    public List<WardNurse> getWardNursesByArea(int areaId);

    public WardNurse get(String name);
    public WardNurse get(int wardNurseId);


    public void save(WardNurse wardNurse);

    public void delete(int nurseId);
}
