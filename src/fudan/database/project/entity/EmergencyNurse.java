package fudan.database.project.entity;

public class EmergencyNurse {
    private int emergencyNurseId;
    private String name;
    private String password;

    public EmergencyNurse() {
        //
    }

    public EmergencyNurse(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getEmergencyNurseId() {
        return emergencyNurseId;
    }

    public void setEmergencyNurseId(int emergencyNurseId) {
        this.emergencyNurseId = emergencyNurseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
