package fudan.database.project.dao.impl;

import fudan.database.project.dao.DAO;
import fudan.database.project.dao.TestResultDAO;
import fudan.database.project.entity.Patient;
import fudan.database.project.entity.TestResult;

import java.util.List;

public class TestResultDAOJdbcImpl extends DAO<TestResult> implements TestResultDAO {
    @Override
    public void save(TestResult testResult) {
        String sql = "INSERT INTO test_result(patient_id, test_date, test_result, evaluation) VALUES(?,?,?,?)";
        update(sql, testResult.getPatientId(), testResult.getDate(), testResult.getTestResult(), testResult.getEvaluation());
    }

    @Override
    public List<TestResult> getTestResultsByPatientId(String patientId) {
        String sql = "SELECT * FROM test_result WHERE patient_id = ? ORDER BY test_date DESC";
        return getForList(sql, patientId);
    }
}
