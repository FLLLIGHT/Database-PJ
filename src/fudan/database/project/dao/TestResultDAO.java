package fudan.database.project.dao;

import fudan.database.project.entity.TestResult;

import java.util.List;

public interface TestResultDAO {
    public void save(TestResult testResult);
    public List<TestResult> getTestResultsByPatientId(String patientId);
}
