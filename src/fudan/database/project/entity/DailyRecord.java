package fudan.database.project.entity;

import java.util.Date;

public class DailyRecord {
    private int recordId;
    private String patientId;
    private float temperature;
    private String symptom;
    private int lifeStatus;
    private Date date;

    public DailyRecord(String patientId, float temperature, String symptom, int lifeStatus, Date date) {
        this.patientId = patientId;
        this.temperature = temperature;
        this.symptom = symptom;
        this.lifeStatus = lifeStatus;
        this.date = date;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public int getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(int lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
