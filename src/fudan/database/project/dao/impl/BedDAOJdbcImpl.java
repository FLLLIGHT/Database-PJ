package fudan.database.project.dao.impl;

import fudan.database.project.dao.BedDAO;
import fudan.database.project.dao.DAO;
import fudan.database.project.entity.Bed;

import java.util.List;

public class BedDAOJdbcImpl extends DAO<Bed> implements BedDAO {
    @Override
    public List<Bed> getFreeBedsByArea(int areaId) {
        String sql = "SELECT bed_id, patient_id, room_id FROM bed WHERE room_id = (SELECT room_id FROM room WHERE area_id = ?) AND patient_id = ''";
        return getForList(sql, areaId);
    }

    @Override
    public List<Bed> getBedsByArea(int areaId) {
        String sql = "SELECT bed_id, patient_id, room_id FROM bed WHERE room_id = (SELECT room_id FROM room WHERE area_id = ?)";
        return getForList(sql, areaId);
    }

    @Override
    public void updateByPatientId(String patientId, int bedId) {
        String sql = "UPDATE bed SET patient_id = ? WHERE bed_id = ?";
        update(sql, patientId, bedId);
    }

    @Override
    public Bed getBedByPatientId(String patientId) {
        String sql = "SELECT bed_id, patient_id, room_id FROM bed WHERE patient_id = ''";
        return get(sql, patientId);
    }
}
