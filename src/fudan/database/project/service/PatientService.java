package fudan.database.project.service;

import fudan.database.project.dao.*;
import fudan.database.project.dao.impl.*;
import fudan.database.project.entity.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PatientService {
    private final PatientDAO patientDAO = new PatientDAOJdbcImpl();
    private final WardNurseDAO wardNurseDAO = new WardNurseDAOJdbcImpl();
    private final BedDAO bedDAO = new BedDAOJdbcImpl();
    private final TestResultDAO testResultDAO = new TestResultDAOJdbcImpl();
    private final DailyRecordDAO dailyRecordDAO = new DailyRecordDAOJdbcImpl();
    private final MessageService messageService = new MessageService();
    ChiefNurseDAO chiefNurseDAO = new ChiefNurseDAOJdbcImpl();
    private final DoctorDAO doctorDAO = new DoctorDAOJdbcImpl();
    private final int MaxNumberInMild = 3;
    private final int MaxNumberInSevere = 2;
    private final int MaxNumberInCritical = 1;

    public List<Patient> getPatientsWaitingToTransfer(int areaId){
        return addNurseName(patientDAO.getPatientsWaitingToTransfer(areaId));
    }

    public void autoTransfer(){
        //从隔离区开始查询
        for(int i=4; i>=1; i--){
            //找到该区域中待转移的病人
            List<Patient> patients = getPatientsWaitingToTransfer(i);
            for(Patient patient : patients){
                int fromArea = patient.getAreaId();
                int toArea = patient.getEvaluation();
                moveArea(patient.getPatientId(), toArea);
            }
        }
    }

    public List<Patient> getPatientsByArea(int areaId){
        return addNurseName(patientDAO.getPatientsByArea(areaId));
    }

    public List<Patient> getPatientsByAreaAndLifeStatus(int areaId, int lifeStatus){
        return addNurseName(patientDAO.getPatientsByAreaAndLifeStatus(areaId, lifeStatus));
    }

    public List<Patient> getPatientsByLifeStatus(int lifeStatus){
        return addNurseName(patientDAO.getPatientsByLifeStatus(lifeStatus));
    }

    public List<Patient> getPatientsByEvaluation(int evaluation){
        return addNurseName(patientDAO.getPatientsByEvaluation(evaluation));
    }

    public List<Patient> getPatientsByNurseId(int nurseId){
        return addNurseName(patientDAO.getPatientsByNurseId(nurseId));
    }

    public List<Patient> getPatientsWaitingToDischargeByArea(int areaId){
        List<Patient> patients = getPatientsByArea(areaId);
        List<Patient> healedPatients = new ArrayList<>();
        for(Patient patient : patients){
            if(checkDailyRecord(patient)&&checkTestResult(patient)){
                healedPatients.add(patient);
            }
        }
        return addNurseName(healedPatients);
    }

    private boolean checkDailyRecord(Patient patient){
        List<DailyRecord> dailyRecords = dailyRecordDAO.getLatest3Record(patient.getPatientId());
        for(DailyRecord dailyRecord : dailyRecords){
            if(dailyRecord.getTemperature() >= 37.3)
                return false;
        }
        return true;
    }

    public void dischargePatient(String patientId){
        //todo: 检查治疗区域是不是轻症
        //todo：再次检查病人是不是真的符合出院条件
        //todo：检查病人的evaluation？
        patientDAO.updateLifeStatusOfPatient(patientId, 1);
        //移开床的关联关系
        bedDAO.updateByPatientId("", bedDAO.getBedByPatientId(patientId).getBedId());
    }

    private boolean checkTestResult(Patient patient){
        List<TestResult> testResults = testResultDAO.getTestResultsByPatientId(patient.getPatientId());
        if(testResults.size()==0) return false;
        if(testResults.get(0).getTestResult().equals("positive")) return false;
        Date date = testResults.get(0).getDate();
        for(int i=1; i<testResults.size(); i++){
            TestResult testResult = testResults.get(i);
            if(testResult.getTestResult().equals("positive")) return false;
            if(differentDaysByMillisecond(testResult.getDate(), date)>=1) return true;
        }
        return false;
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
        checkAndSendDischargeMessage(patientId);
    }

    private void checkAndSendDischargeMessage(String patientId){
        //检查加入这个daily record后是不是符合出院条件，如果是，则给主治医生发消息
        Patient patient = patientDAO.getById(patientId);
        if(checkTestResult(patient)&&checkDailyRecord(patient)){
            String messageContent = "Patient " + patient.getName() + " is waiting to be discharged! please check it out.";
            messageService.sendMessage(doctorDAO.get(patient.getAreaId()).getDoctorId(), 1, messageContent);
        }
    }

    public void addDailyRecord(String patientId, float temperature, String symptom, int lifeStatus, Date date){
        DailyRecord dailyRecord = new DailyRecord(patientId, temperature, symptom, lifeStatus, date);
        dailyRecordDAO.save(dailyRecord);
        checkAndSendDischargeMessage(patientId);
    }

    public Patient getPatientByBed(int bedId){
        return addNurseName(patientDAO.getPatientByBedId(bedId));
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
        Bed bed = bedDAO.getBedByPatientId(patientId);
        //删掉原来的bed信息（如果存在的话）
        if(bed!=null){
            bedDAO.updateByPatientId("", bed.getBedId());
        }
        bedDAO.updateByPatientId(patientId, bedId);
        //修改护士信息
        patientDAO.updateNurseIdAndAreaOfPatient(patientId, nurseId, toAreaId);
        //给护士长发送提醒信息
        String messageContent = "A new patient: " + patient.getName() + " has come to your Area! Please check it out.";
        messageService.sendMessage(chiefNurseDAO.getChiefNurseByArea(toAreaId).getChiefNurseId(), 2, messageContent);
        //如果移动成功，尝试进一步移动（递归调用）
        autoTransfer();
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
        //给护士长发送提醒信息
        String messageContent = "A new patient: " + patient.getName() + " has come to your Area! Please check it out.";
        messageService.sendMessage(chiefNurseDAO.getChiefNurseByArea(evaluation).getChiefNurseId(), 2, messageContent);
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
            int numberOfPatients = patientDAO.getPatientsByNurseId(wardNurse.getWardNurseId()).size();
            if(numberOfPatients < limitNumber){
                nurseId = wardNurse.getWardNurseId();
                break;
            }
        }
        return nurseId;
    }

    private Patient addNurseName(Patient patient){
        patient.setNurseName(wardNurseDAO.get(patient.getNurseId()).getName());
        return patient;
    }

    private List<Patient> addNurseName(List<Patient> patients){
        for(Patient patient : patients){
            if(patient.getNurseId()==-1) continue;
            addNurseName(patient);
        }
        return patients;
    }

    private int differentDaysByMillisecond(Date date1, Date date2)
    {
        return (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
    }



    public static void main(String args[]) throws ParseException {
//        for(int i=0; i<10; i++){
//            UUID uuid = UUID.randomUUID();
//            String strId = uuid.toString().replaceAll("-", "");
//            System.out.println(strId);
//        }
//        DateFormat dateFormat = DateFormat.getDateInstance();
//        Date oldTime = dateFormat.parse("2019-04-07 19:50:11");
//        Date newTime = new Date();
//        int result = differentDaysByMillisecond(oldTime, newTime);
//        System.out.println(result);

    }

}
