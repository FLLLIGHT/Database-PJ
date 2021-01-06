package fudan.database.project.dao.impl;

import fudan.database.project.dao.DAO;
import fudan.database.project.dao.DailyRecordDAO;
import fudan.database.project.entity.DailyRecord;

public class DailyRecordDAOJdbcImpl extends DAO<DailyRecord> implements DailyRecordDAO {
    @Override
    public void save(DailyRecord dailyRecord) {
        String sql = "INSERT INTO daily_record(patient_id, temperature, symptom, life_status, date) VALUES(?,?,?,?,?)";
        update(sql, dailyRecord.getPatientId(), dailyRecord.getTemperature(), dailyRecord.getSymptom(), dailyRecord.getLifeStatus(), dailyRecord.getDate());
    }
}
