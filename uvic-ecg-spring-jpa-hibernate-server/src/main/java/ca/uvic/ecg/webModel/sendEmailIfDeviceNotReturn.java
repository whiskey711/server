package ca.uvic.ecg.webModel;

import ca.uvic.ecg.model.*;
import ca.uvic.ecg.repository.AppointmentRecordRepository;
import ca.uvic.ecg.repository.NurseRepository;
import ca.uvic.ecg.repository.PatientInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.TimeZone;

import java.util.List;

@Component
public class sendEmailIfDeviceNotReturn {

    @Autowired
    AppointmentRecordRepository appointmentRecordRepository;
    @Autowired
    NurseRepository nurseRepository;
    @Autowired
    PatientInfoRepository patientInfoRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Scheduled(cron = "0 0/10 * * * *")
    public void checkReturn(){
        System.out.println("checking devices");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        try{
            do{
                List<AppointmentRecord> list = appointmentRecordRepository.findByAppointmentStartTimeBeforeAndDeletedFalseAndDeviceActualReturnTimeIsNull(nowTime);
                for(AppointmentRecord appointmentRecord:list){
                    if(appointmentRecord.getDelayDeviceReturnTime()!=null &&appointmentRecord.getDelayDeviceReturnTime().before(nowTime)) {
                        continue;
                    }
                    SimpleMailMessage message = new SimpleMailMessage();
                    if(nurseRepository.findByNurseIdAndDeletedFalse(appointmentRecord.getNurseId())== null){
                        System.out.println("Unable to find nurse with nurseId: "+appointmentRecord.getNurseId());
                        continue;
                    }
                    Nurse nurse = nurseRepository.findByNurseIdAndDeletedFalse(appointmentRecord.getNurseId());
                    String email = nurse.getNurseEmail();
                    if(patientInfoRepository.findByPatientIdAndClinicIdAndDeletedFalse(appointmentRecord.getPatientId(),appointmentRecord.getClinicId())== null){
                        System.out.println("Unable to find patient with nurseId: "+appointmentRecord.getPatientId());
                        continue;
                    }
                    PatientInfo patientInfo = patientInfoRepository.findByPatientIdAndClinicIdAndDeletedFalse(appointmentRecord.getPatientId(),appointmentRecord.getClinicId());
                    String patientName = patientInfo.getPatientFirstName()+patientInfo.getPatientMidName()+patientInfo.getPatientLastName();
                    String phone = patientInfo.getPhoneNumber();
                    message.setFrom("ArbutusHolsterEcg");
                    message.setTo(email);
                    message.setSubject("Device notification");
                    message.setText("The device has not been returned yet, the patient name is"
                            + patientName+ " The phone Number of this " +
                            "patient is " +phone);
                    mailSender.send(message);

                    String pEmail = patientInfo.getEmail();
                     message.setTo(pEmail);
                    message.setSubject("Device notification");
                    message.setText("If you already return your device, please let the clinic know, otherwise, please return your device.");
                    mailSender.send(message);
                }
            }while(false);
        }catch(Exception e){
            System.out.println("Exception is "+e);
        }

    }
}
