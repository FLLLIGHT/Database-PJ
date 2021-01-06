package fudan.database.project.dao;

import fudan.database.project.entity.DailyRecord;

import java.util.List;

public interface DailyRecordDAO {
    public void save(DailyRecord dailyRecord);
    public List<DailyRecord> getLatest3Record(String patientId);
}
