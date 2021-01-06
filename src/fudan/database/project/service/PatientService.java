package fudan.database.project.service;

import fudan.database.project.dao.*;
import fudan.database.project.dao.impl.*;
import fudan.database.project.entity.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PatientService {
    private final PatientDAO patientDAO = new PatientDAOJdbcImpl();
    private final WardNurseDAO wardNurseDAO = new WardNurseDAOJdbcImpl();
    private final BedDAO bedDAO = new BedDAOJdbcImpl();
    private final TestResultDAO testResultDAO = new TestResultDAOJdbcImpl();
    private final DailyRecordDAO dailyRecordDAO = new DailyRecordDAOJdbcImpl();
    private final int MaxNumberInMild = 3;
    private final int MaxNumberInSevere = 2;
    private final int MaxNumberInCritical = 1;

    public List<Patient> getPatientsWaitingToTransfer(int areaId){
        return patientDAO.getPatientsWaitingToTransfer(areaId);
    }

    public List<Patient> getPatientsByArea(int areaId){
        return patientDAO.getPatientsByArea(areaId);
    }

    public List<Patient> getPatientsByAreaAndLifeStatus(int areaId, int lifeStatus){
        return patientDAO.getPatientsByAreaAndLifeStatus(areaId, lifeStatus);
    }

    public List<Patient> getPatientsByNurseId(int nurseId){
        return patientDAO.getPatientsByNurseId(nurseId);
    }

    public void updateEvaluationOfPatient(String patientId, int evaluation){
        patientDAO.updateEvaluationOfPatient(patientId, evaluation);
    }

    public void updateLifeStatusOfPatient(String patientId, int lifeStatus){
        patientDAO.updateLifeStatusOfPatient(patientId, lifeStatus);
    }

    public void addTestResult(String patientId, Date date, String result, int evaluation){
        TestResult testResult = new TestResult(patientId, date, result, evaluation);
        testResultDAO.save(testResult);
    }

    public void addDailyRecord(String patientId, float temperature, String symptom, int lifeStatus, Date date){
        DailyRecord dailyRecord = new DailyRecord(patientId, temperature, symptom, lifeStatus, date);
        dailyRecordDAO.save(dailyRecord);
    }

    public Patient getPatientByBed(int bedId){
        return patientDAO.getPatientByBedId(bedId);
    }

    public String moveArea(String patientId, int toAreaId){
        int bedId = -1;
        //检查对应区域是否还有病床
        List<Bed> beds =  bedDAO.getFreeBedsByArea(toAreaId);
        if(beds.size()>0){
            bedId = beds.get(0).getBedId();
        }
        //检查对应区域是否还有病床护士
        int nurseId = getFreeNurseId(toAreaId);
        //如果没有病床/护士空闲
        if(bedId==-1||nurseId==-1){
            //返回提示消息
            return "There is no free bed or nurse in assigned area";
        }
        //否则即有空床+护士
        //修改床信息
        Patient patient = patientDAO.getById(patientId);
        bedDAO.updateByPatientId("", bedDAO.getBedByPatientId(patientId).getBedId());
        bedDAO.updateByPatientId(patientId, bedId);
        //修改护士信息
        patientDAO.updateNurseIdOfPatient(patientId, nurseId);
        return "Move success";
    }

    public String registerPatient(String name, String address, String gender, String telephone, int evaluation){
        int bedId = -1;
        UUID uuid = UUID.randomUUID();
        String strId = uuid.toString().replaceAll("-", "");
        Patient patient = new Patient(strId, name, address, gender, telephone, evaluation);
        //检查对应区域是否还有病床
        List<Bed> beds =  bedDAO.getFreeBedsByArea(evaluation);
        if(beds.size()>0){
            bedId = beds.get(0).getBedId();
        }

        //检查对应区域是否还有病床护士
        int nurseId = getFreeNurseId(evaluation);

        //如果没有病床/护士空闲
        if(bedId==-1||nurseId==-1){
            //加入隔离病房，等待空位
            patient.setAreaId(4);
            patient.setNurseId(-1);
            //待改
            patient.setLifeStatus(2);
            patientDAO.save(patient);
            //返回提示消息
            return "There is no free bed or nurse in assigned area, please wait in isolated area";
        }

        patient.setAreaId(evaluation);
        patient.setNurseId(nurseId);
        patient.setLifeStatus(2);
        patientDAO.save(patient);
        bedDAO.updateByPatientId(strId, bedId);
        return "Check in hospital success";
    }

    private int getLimitNumber(int area_id){
        if(area_id==1) return MaxNumberInMild;
        if(area_id==2) return MaxNumberInSevere;
        if(area_id==3) return MaxNumberInCritical;
        return -1;
    }

    private int getFreeNurseId(int areaId){
        int nurseId = -1;
        List<WardNurse> wardNurses = wardNurseDAO.getWardNursesByArea(areaId);
        int limitNumber = getLimitNumber(areaId);
        for(WardNurse wardNurse : wardNurses){
            int numberOfPatients = patientDAO.getNumberOfPatientsWithSameNurse(wardNurse.getWardNurseId());
            if(numberOfPatients < limitNumber){
                nurseId = wardNurse.getWardNurseId();
                break;
            }
        }
        return nurseId;
    }

//    public static void main(String args[]){
//        for(int i=0; i<10; i++){
//            UUID uuid = UUID.randomUUID();
//            String strId = uuid.toString().replaceAll("-", "");
//            System.out.println(strId);
//        }
//    }
}
