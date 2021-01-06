package fudan.database.project.dao.impl;

import fudan.database.project.dao.DAO;
import fudan.database.project.dao.WardNurseDAO;
import fudan.database.project.entity.ChiefNurse;
import fudan.database.project.entity.WardNurse;

import java.util.List;

public class WardNurseDAOJdbcImpl extends DAO<WardNurse> implements WardNurseDAO {
    @Override
    public List<WardNurse> getWardNursesByArea(int areaId) {
        String sql = "SELECT ward_nurse_id, name, password, area_id FROM ward_nurse WHERE area_id = ?";
        return getForList(sql, areaId);
    }

    @Override
    public WardNurse get(String name) {
        String sql = "SELECT ward_nurse_id, name, password, area_id FROM ward_nurse WHERE name = ?";
        return get(sql, name);
    }
}
