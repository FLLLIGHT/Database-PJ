package fudan.database.project.dao;

import fudan.database.project.entity.Patient;

import java.util.List;

public interface PatientDAO {
    public void save(Patient patient);

    public List<Patient> getAll();

    public Patient getById(String patientId);

    public void updateEvaluationOfPatient(String patientId, int evaluation);

    public void updateLifeStatusOfPatient(String patientId, int lifeStatus);

    public void updateNurseIdOfPatient(String patientId, int nurseId);

    public List<Patient> getPatientsByArea(int areaId);

    public List<Patient> getPatientsByAreaAndLifeStatus(int areaId, int lifeStatus);

    public List<Patient> getPatientsWaitingToTransfer(int areaId);

    public List<Patient> getPatientsByNurseId(int nurseId);

    public Integer getNumberOfPatientsWithSameNurse(int nurseId);
}
