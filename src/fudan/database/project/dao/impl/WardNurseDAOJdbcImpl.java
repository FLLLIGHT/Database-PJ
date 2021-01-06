package fudan.database.project.dao.impl;

import fudan.database.project.dao.DAO;
import fudan.database.project.dao.WardNurseDAO;
import fudan.database.project.entity.ChiefNurse;
import fudan.database.project.entity.WardNurse;

import java.util.List;

public class WardNurseDAOJdbcImpl extends DAO<WardNurse> implements WardNurseDAO {
    @Override
    public List<WardNurse> getWardNursesByArea(int areaId) {
        String sql = "SELECT wardNurseId, name, password, areaId FROM ward_nurse WHERE areaId = ?";
        return getForList(sql, areaId);
    }

    @Override
    public WardNurse get(String name) {
        String sql = "SELECT wardNurseId, name, password, areaId FROM ward_nurse WHERE name = ?";
        return get(sql, name);
    }

    @Override
    public WardNurse get(int wardNurseId) {
        String sql = "SELECT wardNurseId, name, password, areaId FROM ward_nurse WHERE wardNurseId = ?";
        return get(sql, wardNurseId);
    }

    @Override
    public void save(WardNurse wardNurse) {
        String sql = "INSERT INTO ward_nurse(name, password, areaId) VALUES(?,?,?)";
        update(sql, wardNurse.getName(), wardNurse.getPassword(), wardNurse.getAreaId());
    }

    @Override
    public void delete(int nurseId) {
        String sql = "DELETE FROM ward_nurse WHERE wardNurseId = ?";
        update(sql, nurseId);
    }
}
