package fudan.database.project.entity;

public class ChiefNurse {
    private int chiefNurseId;
    private String name;
    private String password;
    private int areaId;

    public int getChiefNurseId() {
        return chiefNurseId;
    }

    public void setChiefNurseId(int chiefNurseId) {
        this.chiefNurseId = chiefNurseId;
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

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }
}


