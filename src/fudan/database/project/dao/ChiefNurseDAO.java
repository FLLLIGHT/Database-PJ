package fudan.database.project.dao;

import fudan.database.project.entity.ChiefNurse;

import java.util.List;

public interface ChiefNurseDAO {
    public List<ChiefNurse> getAll();

    public ChiefNurse get(String name);
    public void update(ChiefNurse chiefNurse);
    public ChiefNurse getChiefNurseByArea(int areaId);
}
