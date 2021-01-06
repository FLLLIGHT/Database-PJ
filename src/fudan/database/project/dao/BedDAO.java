package fudan.database.project.dao;

import fudan.database.project.entity.Bed;

import java.util.List;

public interface BedDAO {
    public List<Bed> getFreeBedsByArea(int areaId);
    public void updateByPatientId(String patientId, int bedId);
    public Bed getBedByPatientId(String patientId);
}
