package ca.uvic.ecg.webModel;

import ca.uvic.ecg.model.*;
import ca.uvic.ecg.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Component
public class SendEmailIfDisconnected {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    EcgTestRepository ecgTestRepository;
    @Autowired
    EcgRawDataRepository ecgRawDataRepository;
    @Autowired
    NurseRepository nurseRepository;
    @Autowired
    PatientInfoRepository patientInfoRepository;
    @Autowired
    AppointmentRecordRepository appointmentRecordRepository;
    @Autowired
    DevicesRepository devicesRepository;

    private final int findDataPeriod = 5;
    private final int NOBLUETOOTH =2;

    @Scheduled(cron = "0 0/2 * * * *")
    public void checkConnection() {
        System.out.println("checking connection");
        SimpleMailMessage message = new SimpleMailMessage();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        nowTime.add(Calendar.MINUTE,-findDataPeriod);
        Calendar fiveMinsBefore = nowTime;
        //reset nowTIme
        nowTime.add(Calendar.MINUTE,findDataPeriod);

        try{
            do{
                List<EcgTest> tests = ecgTestRepository.findByStatusAndDeletedFalseOrStatusAndDeletedFalse(StatusTypes.StatusType.HOOKUP,StatusTypes.StatusType.RECORDING);

                if(tests==null||tests.isEmpty()){
                    System.out.println("There is no ecgTest that is hooking up or recording right now");
                    break;
                }
                for(EcgTest ecgTest : tests) {
                    if(nurseRepository.findByNurseIdAndDeletedFalse(ecgTest.getNurseId())== null){
                        System.out.println("Unable to find nurse with nurseId: "+ecgTest.getNurseId());
                        continue;
                    }
                    String email = nurseRepository.findByNurseIdAndDeletedFalse(ecgTest.getNurseId()).getNurseEmail();
                    PatientInfo patient = patientInfoRepository.findByPatientIdAndClinicIdAndDeletedFalse(ecgTest.getPatientId(),ecgTest.getClinicId());
                    String firstNameOfPatient = patient.getPatientFirstName();
                    String midNameOfPatient = patient.getPatientMidName();
                    String lastNameOfPatient = patient.getPatientLastName();
                    String phoneNumber = patient.getPhoneNumber();

                    //check Bluetooth
                    List<EcgRawData> rawDatas = ecgRawDataRepository.findByEcgTestIdAndClinicIdAndDeletedFalse(ecgTest.getEcgTestId(),ecgTest.getClinicId());
                    if(!rawDatas.isEmpty()){
                        EcgRawData rawData = rawDatas.get(rawDatas.size()-1);
                        if(rawData.getPhoneStatus()==NOBLUETOOTH){
                            message.setFrom("ArbutusHolsterEcg");
                            message.setTo(email);
                            message.setSubject("Connection notification");
                            message.setText("The connection between sensor and the phone (bluetooth) is disconnected, the patient name is"
                                    + firstNameOfPatient +" "+midNameOfPatient+" "+ lastNameOfPatient + " The phone Number of this " +
                                    "patient is " +phoneNumber);
                            mailSender.send(message);
                            continue;
                        }
                    }
                    //send email to nurse if internet between server and phone is disconnected
                    //find the data received in findDataPeriod(5) mins
                    List<EcgRawData> recentData = ecgRawDataRepository.findByReceivedTimeAfterAndEcgTestIdAndDeletedFalse(fiveMinsBefore,ecgTest.getEcgTestId());
                    if(recentData.isEmpty()){

                        //send email if no data is received within findDataPeriod(5) mins
                        message.setFrom("ArbutusHolsterEcg");
                        message.setTo(email);
                        message.setSubject("Connection notification");
                        message.setText("The connection between server and the phone is disconnected, the patient name is"
                                + firstNameOfPatient +" "+midNameOfPatient+" "+ lastNameOfPatient + " The phone Number of this " +
                                "patient is " +phoneNumber);
                        mailSender.send(message);
                        continue;
                    }
                    //send email to nurse if device is not returned after data
                    List<AppointmentRecord> appointmentRecords = appointmentRecordRepository.
                            findByDeviceActualReturnTimeIsNullAndDeviceReturnDateAfter(nowTime);
                    for(AppointmentRecord appointmentRecord :appointmentRecords){
                        Nurse nurse = nurseRepository.findByNurseIdAndDeletedFalse(appointmentRecord.getNurseId());
                        String email2 = nurse.getNurseEmail();
                        PatientInfo patientInfo = patientInfoRepository.findByPatientIdAndClinicIdAndDeletedFalse(
                                appointmentRecord.getPatientId(),appointmentRecord.getClinicId());
                        Device device = devicesRepository.findByDeviceIdAndClinicIdAndDeletedFalse(appointmentRecord.getDeviceId(),appointmentRecord.getClinicId());
                        String deviceName = device.getDeviceName();
                        String firstName = patientInfo.getPatientFirstName();
                        String lastname = patientInfo.getPatientLastName();
                        String phone = patientInfo.getPhoneNumber();
                        //send email if no data is received within findDataPeriod(5) mins
                        message.setFrom("ArbutusHolsterEcg");
                        message.setTo(email2);
                        message.setSubject("Return Phone reminder");
                        message.setText("Please remind the patient to return the device, the patient name is"
                                + firstName +" "+ lastname + " The phone Number of this " +
                                "patient is " +phone+", the device name is "+deviceName);
                        mailSender.send(message);
                        continue;
                    }
                }

            }while(false);

        }catch(Exception e){
            System.out.println("Exception is "+e);
        }
    }
}
