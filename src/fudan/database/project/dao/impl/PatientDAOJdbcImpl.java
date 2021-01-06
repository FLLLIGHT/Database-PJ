package fudan.database.project.dao.impl;

import fudan.database.project.dao.DAO;
import fudan.database.project.dao.PatientDAO;
import fudan.database.project.entity.Patient;

import java.util.List;

public class PatientDAOJdbcImpl extends DAO<Patient> implements PatientDAO {
    @Override
    public void save(Patient patient) {
        String sql = "INSERT INTO patient(patient_id, name, address, gender, telephone, area_id, evaluation, life_status, nurse_id) VALUES(?,?,?,?,?,?,?,?,?)";
        update(sql, patient.getPatientId(), patient.getName(), patient.getAddress(), patient.getGender(), patient.getTelephone(),
                patient.getAreaId(), patient.getEvaluation(), patient.getLifeStatus(), patient.getNurseId());
    }

    @Override
    public List<Patient> getAll() {
        return null;
    }

    @Override
    public Patient getById(String patientId) {
        String sql = "SELECT * FROM patient WHERE patient_id = ?";
        return get(sql, patientId);
    }

    @Override
    public void updateEvaluationOfPatient(String patientId, int evaluation) {
        String sql = "UPDATE patient SET evaluation = ? WHERE patientId = ?";
        update(sql, evaluation, patientId);
    }

    @Override
    public void updateNurseIdOfPatient(String patientId, int nurseId) {
        String sql = "UPDATE patient SET nurseId = ? WHERE patientId = ?";
        update(sql, nurseId, patientId);
    }

    @Override
    public void updateLifeStatusOfPatient(String patientId, int lifeStatus) {
        String sql = "UPDATE patient SET life_status = ? WHERE patientId = ?";
        update(sql, lifeStatus, patientId);

    }

    @Override
    public List<Patient> getPatientsWaitingToTransfer(int areaId) {
        String sql = "SELECT * FROM patient WHERE area_id != evaluation and area_id = ?";
        return getForList(sql, areaId);
    }

    @Override
    public List<Patient> getPatientsByArea(int areaId) {
        String sql = "SELECT * FROM patient WHERE area_id = ?";
        return getForList(sql, areaId);
    }

    @Override
    public List<Patient> getPatientsByAreaAndLifeStatus(int areaId, int lifeStatus) {
        String sql = "SELECT * FROM patient WHERE area_id = ? AND life_status = ?";
        return getForList(sql, areaId, lifeStatus);
    }

    @Override
    public List<Patient> getPatientsByNurseId(int nurseId) {
        String sql = "SELECT * FROM patient WHERE nurse_id = ?";
        return getForList(sql, nurseId);
    }

    @Override
    public Integer getNumberOfPatientsWithSameNurse(int nurseId) {
        String sql = "SELECT COUNT(patient_id) FROM patient WHERE nurse_id = ?";
        return getForValue(sql, nurseId);
    }

}
