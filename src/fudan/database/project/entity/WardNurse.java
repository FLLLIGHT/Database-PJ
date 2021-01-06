package fudan.database.project.entity;

public class WardNurse {
    private int wardNurseId;
    private String name;
    private String password;
    private int areaId;

    public WardNurse(String name, String password, int areaId) {
        this.name = name;
        this.password = password;
        this.areaId = areaId;
    }

    public int getWardNurseId() {
        return wardNurseId;
    }

    public void setWardNurseId(int wardNurseId) {
        this.wardNurseId = wardNurseId;
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
