package fudan.database.project.dao.impl;

import fudan.database.project.dao.DAO;
import fudan.database.project.dao.PatientDAO;
import fudan.database.project.entity.Patient;

import java.util.List;

public class PatientDAOJdbcImpl extends DAO<Patient> implements PatientDAO {
    @Override
    public void save(Patient patient) {
        String sql = "INSERT INTO patient(patientId, name, address, gender, telephone, areaId, evaluation, lifeStatus, nurseId) VALUES(?,?,?,?,?,?,?,?,?)";
        update(sql, patient.getPatientId(), patient.getName(), patient.getAddress(), patient.getGender(), patient.getTelephone(),
                patient.getAreaId(), patient.getEvaluation(), patient.getLifeStatus(), patient.getNurseId());
    }

    @Override
    public List<Patient> getAll() {
        return null;
    }

    @Override
    public Patient getById(String patientId) {
        String sql = "SELECT * FROM patient WHERE patientId = ?";
        return get(sql, patientId);
    }

    @Override
    public Patient getPatientByBedId(int bedId) {
        String sql = "SELECT * FROM patient WHERE patientId = (SELECT patientId FROM bed WHERE bedId = ?)";
        return get(sql, bedId);
    }

    @Override
    public void updateEvaluationOfPatient(String patientId, int evaluation) {
        String sql = "UPDATE patient SET evaluation = ? WHERE patientId = ?";
        update(sql, evaluation, patientId);
    }

    @Override
    public void updateNurseIdAndAreaOfPatient(String patientId, int nurseId, int areaId) {
        String sql = "UPDATE patient SET nurseId = ?, areaId = ? WHERE patientId = ?";
        update(sql, nurseId, areaId, patientId);
    }

    @Override
    public void updateLifeStatusOfPatient(String patientId, int lifeStatus) {
        String sql = "UPDATE patient SET lifeStatus = ? WHERE patientId = ?";
        update(sql, lifeStatus, patientId);

    }

    @Override
    public List<Patient> getPatientsWaitingToTransfer(int areaId) {
        String sql = "SELECT * FROM patient WHERE areaId != evaluation and areaId = ?";
        return getForList(sql, areaId);
    }

    @Override
    public List<Patient> getPatientsByArea(int areaId) {
        String sql = "SELECT * FROM patient WHERE areaId = ?";
        return getForList(sql, areaId);
    }

    @Override
    public List<Patient> getPatientsByAreaAndLifeStatus(int areaId, int lifeStatus) {
        String sql = "SELECT * FROM patient WHERE areaId = ? AND lifeStatus = ?";
        return getForList(sql, areaId, lifeStatus);
    }

    @Override
    public List<Patient> getPatientsByLifeStatus(int lifeStatus) {
        String sql = "SELECT * FROM patient WHERE lifeStatus = ?";
        return getForList(sql, lifeStatus);
    }

    @Override
    public List<Patient> getPatientsByEvaluation(int evaluation) {
        String sql = "SELECT * FROM patient WHERE evaluation = ?";
        return getForList(sql, evaluation);
    }

    @Override
    public List<Patient> getPatientsByNurseId(int nurseId) {
        String sql = "SELECT * FROM patient WHERE nurseId = ?";
        return getForList(sql, nurseId);
    }

    @Override
    public Integer getNumberOfPatientsWithSameNurse(int nurseId) {
        String sql = "SELECT COUNT(patientId) FROM patient WHERE nurseId = ?";
        return getForValue(sql, nurseId);
    }

}
