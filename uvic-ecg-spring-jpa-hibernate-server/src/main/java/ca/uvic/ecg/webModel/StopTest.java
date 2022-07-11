package ca.uvic.ecg.webModel;

import ca.uvic.ecg.model.EcgTest;
import ca.uvic.ecg.model.StatusTypes;
import ca.uvic.ecg.repository.EcgTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Component
public class StopTest {

    @Autowired
    EcgTestRepository ecgTestRepository;


    @Scheduled(cron = "0 0/2 * * * *")
    public void timeTest(){
        System.out.println("stoping test");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar nowTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        //find the ecgTests that is hooking up or recording
        try{
            do{
                List<EcgTest> tests = ecgTestRepository.findByStatusAndDeletedFalseOrStatusAndDeletedFalse(StatusTypes.StatusType.HOOKUP, StatusTypes.StatusType.RECORDING);

                if(tests==null||tests.isEmpty()){
                    System.out.println("There is no ecgTest that is hooking up or recording right now");
                    break;
                }
                for(EcgTest ecgTest : tests){
                    if(nowTime.after(ecgTest.getScheduledEndTime())){
                        ecgTest.setStatus(StatusTypes.StatusType.TERMINATED);

                        ecgTest.setActualEndTime(nowTime);
                        ecgTestRepository.save(ecgTest);
                    }
                }
            }while(false);

        }catch(Exception e){
            System.out.println("Exception is "+e);
        }
    }
}
