package net.thrymr.controller;

import net.thrymr.services.CounsellorSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CronService {
    @Autowired
    CounsellorSlotService counsellorSlotService;
    //cron format: "second, minute, hour, day, month, weekday"
    /*@Scheduled(cron = "0 50 10 * * SAT", zone = "IST")
    public String deleteAllCounsellorSlot() {
        return counsellorSlotService.deleteAllCounsellorSlots();
    }*/
}