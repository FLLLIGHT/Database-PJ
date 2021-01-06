package fudan.database.project.entity;

public class Patient {
    private String patientId;
    private String name;
    private String address;
    private String gender;
    private String telephone;
    private int areaId;
    private int evaluation;
    private int lifeStatus;
    private int nurseId;

    private String nurseName;

    public Patient(String patientId, String name, String address, String gender, String telephone, int evaluation) {
        this.patientId = patientId;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.telephone = telephone;
        this.evaluation = evaluation;
    }

    public Patient(){
        //
    }

    public Patient(String patientId, String name, String address, String gender, String telephone, int areaId, int evaluation, int lifeStatus, int nurseId) {
        this.patientId = patientId;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.telephone = telephone;
        this.areaId = areaId;
        this.evaluation = evaluation;
        this.lifeStatus = lifeStatus;
        this.nurseId = nurseId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public int getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(int lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public int getNurseId() {
        return nurseId;
    }

    public void setNurseId(int nurseId) {
        this.nurseId = nurseId;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }
}
