package net.thrymr.services.impl;

import net.thrymr.constant.Constants;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public String createCounsellorSlot(TimeSlotDto request) {
        CounsellorSlot slot=new CounsellorSlot();
        if(Validator.isValid(request.getCounsellorId())){
            Optional<Counsellor> optionalCounsellor=counsellorRepo.findById(request.getCounsellorId());
            if(optionalCounsellor.isPresent()){
                slot.setCounsellor(optionalCounsellor.get());
            }

        }else {
            return "Counsellor not found";
        }
        List<CounsellorSlot>counsellorSlots=new ArrayList<>();
        if(Validator.isValid(request.getSlots())){
            for(SlotDetailsDto detailsDto:request.getSlots()){
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
                slot.setEndTime(DateUtils.toParseLocalTime(detailsDto.getEndTime(),Constants.TIME_FORMAT_2));
                slot.setDays(DayOfWeek.valueOf(detailsDto.getDayOfWeek()));
                slot.setSlotStatus(SlotStatus.AVAILABLE);
                slot.setSlotShift(SlotShift.getType( detailsDto.getSlotShift() ));
                LocalDate todayDate=LocalDate.now();
                if(detailsDto.getDate()!=null){
                    slot.setSlotDt(DateUtils.toParseLocalDate(detailsDto.getDate(),Constants.DATE_FORMAT));
                    if( !todayDate.isBefore(slot.getSlotDt())){
                        return "Date should not before the Today date";
                    }

                }else if(detailsDto.getFromDate()!=null && detailsDto.getToDate()!=null){
                   LocalDate fromDate=DateUtils.toParseLocalDate(detailsDto.getFromDate(),Constants.DATE_FORMAT);
                    LocalDate toDate=DateUtils.toParseLocalDate(detailsDto.getToDate(),Constants.DATE_FORMAT);
                    if( todayDate.isAfter(fromDate) && todayDate.isBefore(toDate)) {
                        return "From Date should not before the Today date";
                    }
                    long dateDifference= ChronoUnit.DAYS.between(fromDate, toDate);

                    for(int i=0;i<dateDifference;i++){
                      /*  slot.setStartTime(DateUtils.toParseLocalTime(detailsDto.getStartTime(), Constants.TIME_FORMAT_2));
                        slot.setEndTime(DateUtils.toParseLocalTime(detailsDto.getEndTime(),Constants.TIME_FORMAT_2));
                        slot.setDays(DayOfWeek.valueOf(detailsDto.getDayOfWeek()));
                        slot.setSlotStatus(SlotStatus.AVAILABLE);
                        slot.setSlotShift(SlotShift.getType( detailsDto.getSlotShift() ));*/

                        slot.setSlotDt(fromDate.plusDays(i));
                    }
                    //need to implement

                }else {
                    return "required date";
                }


                counsellorSlots.add(slot );
            }

        }
        counsellorSlotRepo.saveAll(counsellorSlots);
        return "Counsellor slots saved successfully";
    }

    @Override
    public List<CounsellorSlot> getCounsellorSlot( String empId) {
        Optional<AppUser> optionalAppUser = appUserRepo.findByEmpId(empId);
        AppUser appUser = null;
        if (optionalAppUser.isPresent()){
            appUser=optionalAppUser.get();
        }
        List<CounsellorSlot> counsellorSlotList = counsellorSlotRepo.findByAppUser(String.valueOf(appUser));
        return counsellorSlotList;
    }


}
