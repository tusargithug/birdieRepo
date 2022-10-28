package net.thrymr.services.impl;

import net.thrymr.constant.Constants;
import net.thrymr.dto.CounsellorSlotResponseDto;
import net.thrymr.dto.slotRequest.SlotDetailsDto;
import net.thrymr.dto.slotRequest.TimeSlotDto;
import net.thrymr.enums.SlotShift;
import net.thrymr.enums.SlotStatus;
import net.thrymr.model.AppUser;
import net.thrymr.model.Counsellor;
import net.thrymr.model.CounsellorSlot;
import net.thrymr.repository.AppUserRepo;
import net.thrymr.repository.CounsellorRepo;
import net.thrymr.repository.CounsellorSlotRepo;
import net.thrymr.services.CounsellorSlotService;
import net.thrymr.utils.DateUtils;
import net.thrymr.utils.Validator;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CounsellorSlotServiceImpl implements CounsellorSlotService {

    private final CounsellorRepo counsellorRepo;

    private final CounsellorSlotRepo counsellorSlotRepo;

    private final AppUserRepo appUserRepo;

    public CounsellorSlotServiceImpl(CounsellorRepo counsellorRepo, CounsellorSlotRepo counsellorSlotRepo, AppUserRepo appUserRepo) {
        this.counsellorRepo = counsellorRepo;
        this.counsellorSlotRepo = counsellorSlotRepo;
        this.appUserRepo = appUserRepo;
    }


    @Override
    public String createCounsellorSlot(TimeSlotDto request) throws ParseException {
        CounsellorSlot slot = new CounsellorSlot();
        if (Validator.isValid(request.getCounsellorId())) {
            Optional<Counsellor> optionalCounsellor = counsellorRepo.findById(request.getCounsellorId());
            if (optionalCounsellor.isPresent()) {
                slot.setCounsellor(optionalCounsellor.get());
            }

        } else {
            return "Counsellor not found";
        }
        List<CounsellorSlot> counsellorSlots = new ArrayList<>();
        if (Validator.isValid(request.getSlots())) {
            for (SlotDetailsDto detailsDto : request.getSlots()) {
                switch (request.getSlotShift()) {
                    case "MORNING":
                        slot.setSlotShift(SlotShift.MORNING);
                        break;
                    case "AFTERNOON":
                        slot.setSlotShift(SlotShift.AFTERNOON);
                        break;
                    case "EVENING":
                        slot.setSlotShift(SlotShift.EVENING);
                        break;
                    default:
                        return "please select any shift";
                }
                slot.setStartTime(DateUtils.toParseLocalTime(detailsDto.getStartTime(), Constants.TIME_FORMAT_2));
                slot.setEndTime(DateUtils.toParseLocalTime(detailsDto.getEndTime(), Constants.TIME_FORMAT_2));
                slot.setDays(DayOfWeek.valueOf(detailsDto.getDayOfWeek()));
                slot.setSlotStatus(SlotStatus.valueOf(detailsDto.getSlotStatus()));
                slot.setSlotShift(SlotShift.getType(detailsDto.getSlotShift()));
                Date todayDate= new Date();
                if(detailsDto.getDate()!=null){
                    slot.setSlotDt(DateUtils.toFormatStringToDate(detailsDto.getToDate(), Constants.DATE_FORMAT));
                    if(!todayDate.before(slot.getSlotDt())){
                        return "Date should not before the Today date";
                    }

                }else if(detailsDto.getFromDate()!=null && detailsDto.getToDate() != null) {
                    Date fromDate=DateUtils.toFormatStringToDate(detailsDto.getFromDate(), Constants.DATE_FORMAT);
                    Date toDate=DateUtils.toFormatStringToDate(detailsDto.getToDate(), Constants.DATE_FORMAT);
                    if(todayDate.after(fromDate) && todayDate.before(toDate)) {
                        return "From Date should not before the Today date";
                    }
                    long dateDifference= ChronoUnit.DAYS.between(fromDate.toInstant(), toDate.toInstant());
                    for(int i=0;i<dateDifference;i++){
                      /*  slot.setStartTime(DateUtils.toParseLocalTime(detailsDto.getStartTime(), Constants.TIME_FORMAT_2));
                        slot.setEndTime(DateUtils.toParseLocalTime(detailsDto.getEndTime(),Constants.TIME_FORMAT_2));
                        slot.setDays(DayOfWeek.valueOf(detailsDto.getDayOfWeek()));
                        slot.setSlotStatus(SlotStatus.AVAILABLE);
                        slot.setSlotShift(SlotShift.getType( detailsDto.getSlotShift() ));*/
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(fromDate);
                        cal.add(Calendar.DATE, 1);
                        slot.setSlotDt(cal.getTime());
                    }
                    //need to implement
                }else {
                    return "required date";
                }
                if (detailsDto.getAppUserId() != null) {
                    Optional<AppUser> optionalAppUserId = appUserRepo.findById(detailsDto.getAppUserId());
                    if (optionalAppUserId.isPresent()) {
                        slot.setAppUser(optionalAppUserId.get());
                    }
                }
                if (detailsDto.getCounsellorId() != null) {
                    Optional<Counsellor> optionalCounsellorId = counsellorRepo.findById(detailsDto.getCounsellorId());
                    if (optionalCounsellorId.isPresent()) {
                        slot.setCounsellor(optionalCounsellorId.get());
                    }
                }
                counsellorSlots.add(slot);
            }

        }
        counsellorSlotRepo.saveAll(counsellorSlots);
        return "Counsellor slots saved successfully";
    }

    @Override
    public List<CounsellorSlot> getCounsellorSlot() {
        List<CounsellorSlot> counsellorSlotList=counsellorSlotRepo.findAll();
        return counsellorSlotList;
    }
    /*public List<CounsellorSlot> getCounsellorSlot(TimeSlotDto response) {
        List<CounsellorSlot> counsellorSlotList=counsellorSlotRepo.findAll();
        if(response!=null){
            for(CounsellorSlot slotDetails:counsellorSlotList){
                Calendar calender = Calendar.getInstance();
                LocalTime time=LocalTime.now();
                if(slotDetails.getEndTime().isBefore(time)){
                   slotDetails.setSlotStatus(SlotStatus.DELETED);
                }
                counsellorSlotList.add(slotDetails);
            }
            counsellorSlotRepo.saveAll(counsellorSlotList);
        }

        return counsellorSlotList;
    }*/

    @Override
    public CounsellorSlot getCounsellorSlotById(Long id) {
        CounsellorSlot counsellorSlot = null;
        if(id!=null) {
            Optional<CounsellorSlot> optionalCounsellorSlot = counsellorSlotRepo.findById(id);
            counsellorSlot=optionalCounsellorSlot.get();
        }
        return counsellorSlot;
    }
}
