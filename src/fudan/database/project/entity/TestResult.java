package fudan.database.project.entity;

import java.util.Date;

public class TestResult {
    private int testResultId;
    private String patientId;
    private Date date;
    private String testResult;
    private int evaluation;

    public TestResult(String patientId, Date date, String testResult, int evaluation) {
        this.patientId = patientId;
        this.date = date;
        this.testResult = testResult;
        this.evaluation = evaluation;
    }

    public int getTestResultId() {
        return testResultId;
    }

    public void setTestResultId(int testResultId) {
        this.testResultId = testResultId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }
}
