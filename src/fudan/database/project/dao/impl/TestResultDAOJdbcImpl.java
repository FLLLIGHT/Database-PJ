package fudan.database.project.dao.impl;

import fudan.database.project.dao.DAO;
import fudan.database.project.dao.TestResultDAO;
import fudan.database.project.entity.Patient;
import fudan.database.project.entity.TestResult;

public class TestResultDAOJdbcImpl extends DAO<TestResult> implements TestResultDAO {
    @Override
    public void save(TestResult testResult) {
        String sql = "INSERT INTO test_result(patient_id, test_date, test_result, evaluation) VALUES(?,?,?,?)";
        update(sql, testResult.getPatientId(), testResult.getDate(), testResult.getTestResult(), testResult.getEvaluation());
    }
}
