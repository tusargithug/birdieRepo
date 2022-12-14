package net.thrymr.services.impl;

import net.thrymr.constant.Constants;
import net.thrymr.dto.CounsellorSlotDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.dto.slotRequest.SlotDetailsDto;
import net.thrymr.dto.slotRequest.TimeSlotDto;
import net.thrymr.enums.SlotShift;
import net.thrymr.enums.SlotStatus;
import net.thrymr.model.*;
import net.thrymr.repository.AppUserRepo;
import net.thrymr.repository.AppointmentRepo;
import net.thrymr.repository.CounsellorRepo;
import net.thrymr.repository.CounsellorSlotRepo;
import net.thrymr.services.CounsellorSlotService;
import net.thrymr.utils.DateUtils;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CounsellorSlotServiceImpl implements CounsellorSlotService {

    @Autowired
    CounsellorRepo counsellorRepo;

    @Autowired
    CounsellorSlotRepo counsellorSlotRepo;

    @Autowired
    AppUserRepo appUserRepo;

    @Autowired
    AppointmentRepo appointmentRepo;


    @Override
    public String createCounsellorSlot(CounsellorSlotDto request) throws ParseException {
        CounsellorSlot slot = new CounsellorSlot();
        if (Validator.isValid(request.getCounsellorId())) {
            Optional<Counsellor> optionalCounsellor = counsellorRepo.findById(request.getCounsellorId());
            optionalCounsellor.ifPresent(slot::setCounsellor);
        } else {
            return "Counsellor not found";
        }
        slot.setStartTime(DateUtils.toParseLocalTime(request.getStartTime(), Constants.TIME_FORMAT_2));
        slot.setEndTime(DateUtils.toParseLocalTime(request.getEndTime(), Constants.TIME_FORMAT_2));
        slot.setSlotDays(request.getSlotDays());
        slot.setSlotStatus(SlotStatus.valueOf(request.getSlotStatus()));
        slot.setSlotShift(request.getSlotShift());
        Date todayDate = new Date();
        if (Validator.isValid(request.getSlotDate())) {
            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(request.getSlotDate());

            if ( date1.getDate()==todayDate.getDate() || date1.after(todayDate) ) {
                slot.setSlotDate(DateUtils.toFormatStringToDate(request.getSlotDate(), Constants.DATE_FORMAT));
            } else {
                return "please provide from today's date";
            }
        }
        if (Validator.isValid(request.getFromDate())) {
            Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(request.getFromDate());
            if (date2.getDate()==todayDate.getDate() || date2.after(todayDate) && (request.getToDate() != null && !todayDate.before(date2))) {
                slot.setFromDate(DateUtils.toFormatStringToDate(request.getFromDate(), Constants.DATE_FORMAT));
                slot.setToDate(DateUtils.toFormatStringToDate(request.getToDate(), Constants.DATE_FORMAT));
            } else {
                return "please provide from today's date";
            }
        }
        if (request.getAppUserId() != null) {
            Optional<AppUser> optionalAppUserId = appUserRepo.findById(request.getAppUserId());
            optionalAppUserId.ifPresent(slot::setAppUser);
        }
        if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE)) {
            slot.setIsActive(request.getIsActive());
        }
        slot.setSearchKey(getCounsellorSearchKey(slot));
        counsellorSlotRepo.save(slot);
        return "Counsellor slots saved successfully";
    }

    @Override
    public PaginationResponse getAllCounsellorSlotPagination(CounsellorSlotDto request) {
        Pageable pageable = null;
//
//        if (request.getPageSize() != null && request.getPageNumber() != null) {
//            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
//        }
//        if (request.getPageSize() != null && request.getPageNumber() != null) {
//            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.DESC, "createdOn");
//        }
//        Specification<CounsellorSlot> counsellorSlotSpecification = (root, query, criteriaBuilder) -> {
//            List<Predicate> predicateList = new ArrayList<>();
//            Join<Counsellor, Vendor> vendorJoin = root.join("vendor");
//            Join<CounsellorSlot, Counsellor> counsellorJoin = root.join("counsellor");
//            Join<Counsellor, Site> siteJoin= root.join("site");
//            if();
//        };
//
//        List<CounsellorSlot> counsellorSlotList = counsellorSlotRepo.findAll();
//        if (!counsellorSlotList.isEmpty()) {
//            return counsellorSlotList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
//        }
//        return new ArrayList<>();
        return null;
    }

    @Override
    public CounsellorSlot getCounsellorSlotById(Long id) {
        CounsellorSlot counsellorSlot = null;
        if (Validator.isValid(id)) {
            Optional<CounsellorSlot> optionalCounsellorSlot = counsellorSlotRepo.findById(id);
            if (optionalCounsellorSlot.isPresent() && optionalCounsellorSlot.get().getIsActive().equals(Boolean.TRUE)) {
                counsellorSlot = optionalCounsellorSlot.get();
                return counsellorSlot;
            }
        }
        return new CounsellorSlot();
    }

    @Override
    public String rescheduledCounsellorSlot(CounsellorSlotDto request) throws ParseException {
        LocalTime localTime = LocalTime.now();
        CounsellorSlot counsellorSlot;
        if (Validator.isValid(request.getCounsellorSlotId())) {
            Optional<CounsellorSlot> optionalCounsellorSlot = counsellorSlotRepo.findById(request.getCounsellorSlotId());
            if (optionalCounsellorSlot.isPresent()) {
                counsellorSlot = optionalCounsellorSlot.get();
                Duration duration = Duration.between(LocalTime.now(), optionalCounsellorSlot.get().getStartTime());
                if (duration.toMinutes() >= 30) {
                    if (Validator.isValid(request.getCounsellorId())) {
                        Optional<Counsellor> optionalCounsellor = counsellorRepo.findById(request.getCounsellorId());
                        optionalCounsellor.ifPresent(counsellorSlot::setCounsellor);
                    } else {
                        return "Counsellor not found";
                    }
                    if(Validator.isValid(request.getStartTime())) {
                        counsellorSlot.setStartTime(DateUtils.toParseLocalTime(request.getStartTime(), Constants.TIME_FORMAT_2));
                    }if(Validator.isValid(request.getEndTime())) {
                        counsellorSlot.setEndTime(DateUtils.toParseLocalTime(request.getEndTime(), Constants.TIME_FORMAT_2));
                    }
                    if(request.getSlotDays() != null && request.getSlotDays().isEmpty()) {
                        counsellorSlot.setSlotDays(request.getSlotDays());
                    }
                    if(request.getSlotStatus() != null) {
                        counsellorSlot.setSlotStatus(SlotStatus.valueOf(request.getSlotStatus()));
                    }
                    if(request.getSlotShift() != null) {
                        counsellorSlot.setSlotShift(request.getSlotShift());
                    }
                    Date todayDate = new Date();
                    if (Validator.isValid(request.getSlotDate())) {
                        Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(request.getSlotDate());
                        if ( date1.getDate()==todayDate.getDate() || date1.after(todayDate) ) {
                            counsellorSlot.setSlotDate(DateUtils.toFormatStringToDate(request.getSlotDate(), Constants.DATE_FORMAT));
                        } else {
                            return "please provide from today's date";
                        }
                    }
                    if (Validator.isValid(request.getFromDate())) {
                        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(request.getFromDate());
                        if (date2.getDate()==todayDate.getDate() || date2.after(todayDate) && (request.getToDate() != null && !todayDate.before(date2))) {
                            counsellorSlot.setFromDate(DateUtils.toFormatStringToDate(request.getFromDate(), Constants.DATE_FORMAT));
                            counsellorSlot.setToDate(DateUtils.toFormatStringToDate(request.getToDate(), Constants.DATE_FORMAT));
                        } else {
                            return "please provide from today's date";
                        }
                    }
                    if (request.getAppUserId() != null) {
                        Optional<AppUser> optionalAppUserId = appUserRepo.findById(request.getAppUserId());
                        optionalAppUserId.ifPresent(counsellorSlot::setAppUser);
                    }
                    if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE)) {
                        counsellorSlot.setIsActive(request.getIsActive());
                    }
                    counsellorSlot.setSearchKey(getCounsellorSearchKey(counsellorSlot));
                    counsellorSlotRepo.save(counsellorSlot);
                    return "Counsellor slots saved successfully";
                }
            } else {
                return "rescheduling is not possible";
            }
        }
        return "Invalid Slot Id";
    }

    @Override
    public String cancelCounsellorSlot(Long id) {
        CounsellorSlot counsellorSlot = null;
        if (Validator.isValid(id)) {
            Optional<CounsellorSlot> optionalCounsellorSlot = counsellorSlotRepo.findById(id);
            if (optionalCounsellorSlot.isPresent()) {
                counsellorSlot = optionalCounsellorSlot.get();
                counsellorSlot.setIsActive(Boolean.FALSE);
                counsellorSlot.setIsDeleted(Boolean.TRUE);
                counsellorSlot.setSlotStatus(SlotStatus.DELETED);
                counsellorSlot.setSearchKey(getCounsellorSearchKey(counsellorSlot));
                counsellorSlotRepo.save(counsellorSlot);
            }
        } else {
            return "invalid slot Id";
        }
        return "slot canceled successfully";
    }

    public String getCounsellorSearchKey(CounsellorSlot counsellorSlot) {
        String searchKey = "";
        if (counsellorSlot.getStartTime() != null) {
            searchKey = searchKey + " " + counsellorSlot.getStartTime();
        }
        if (counsellorSlot.getEndTime() != null) {
            searchKey = searchKey + " " + counsellorSlot.getEndTime();
        }
        if (counsellorSlot.getSlotDate() != null) {
            searchKey = searchKey + " " + counsellorSlot.getSlotDate();
        }
        if (counsellorSlot.getSlotShift() != null) {
            searchKey = searchKey + " " + counsellorSlot.getSlotShift();
        }
        if (counsellorSlot.getSlotDays() != null) {
            searchKey = searchKey + " " + counsellorSlot.getSlotDays();
        }
        if (counsellorSlot.getSlotStatus() != null) {
            searchKey = searchKey + " " + counsellorSlot.getSlotStatus();
        }
        if (counsellorSlot.getAppUser() != null) {
            searchKey = searchKey + " " + counsellorSlot.getAppUser();
        }
        if (counsellorSlot.getCounsellor() != null) {
            searchKey = searchKey + " " + counsellorSlot.getCounsellor();
        }
        if (counsellorSlot.getIsActive() != null) {
            searchKey = searchKey + " " + counsellorSlot.getIsActive();
        }
        if(counsellorSlot.getFromDate() != null) {
            searchKey = searchKey + " " + counsellorSlot.getFromDate();
        }
        if(counsellorSlot.getToDate() != null) {
            searchKey = searchKey + " " + counsellorSlot.getToDate();
        }
        return searchKey;
    }

}
