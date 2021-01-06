package fudan.database.project.dao.impl;

import fudan.database.project.dao.DAO;
import fudan.database.project.dao.DailyRecordDAO;
import fudan.database.project.entity.DailyRecord;

import java.util.List;

public class DailyRecordDAOJdbcImpl extends DAO<DailyRecord> implements DailyRecordDAO {
    @Override
    public void save(DailyRecord dailyRecord) {
        String sql = "INSERT INTO daily_record(patientId, temperature, symptom, lifeStatus, date) VALUES(?,?,?,?,?)";
        update(sql, dailyRecord.getPatientId(), dailyRecord.getTemperature(), dailyRecord.getSymptom(), dailyRecord.getLifeStatus(), dailyRecord.getDate());
    }

    @Override
    public List<DailyRecord> getLatest3Record(String patientId) {
        String sql = "SELECT * FROM daily_record LIMIT 3 WHERE patientId = ? ORDER BY date DESC";
        return getForList(sql, patientId);
    }
}
