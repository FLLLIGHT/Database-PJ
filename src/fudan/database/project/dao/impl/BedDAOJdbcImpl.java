package fudan.database.project.dao.impl;

import fudan.database.project.dao.BedDAO;
import fudan.database.project.dao.DAO;
import fudan.database.project.entity.Bed;

import java.util.List;

public class BedDAOJdbcImpl extends DAO<Bed> implements BedDAO {
    @Override
    public List<Bed> getFreeBedsByArea(int areaId) {
        String sql = "SELECT bedId, patientId, roomId FROM bed WHERE roomId = (SELECT roomId FROM room WHERE areaId = ?) AND patientId = ''";
        return getForList(sql, areaId);
    }

    @Override
    public List<Bed> getBedsByArea(int areaId) {
        String sql = "SELECT bedId, patientId, roomId FROM bed WHERE roomId = (SELECT roomId FROM room WHERE areaId = ?)";
        return getForList(sql, areaId);
    }

    @Override
    public void updateByPatientId(String patientId, int bedId) {
        String sql = "UPDATE bed SET patientId = ? WHERE bedId = ?";
        update(sql, patientId, bedId);
    }

    @Override
    public Bed getBedByPatientId(String patientId) {
        String sql = "SELECT bedId, patientId, roomId FROM bed WHERE patientId = ''";
        return get(sql, patientId);
    }
}
