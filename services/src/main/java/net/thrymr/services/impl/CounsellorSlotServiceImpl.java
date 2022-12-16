package net.thrymr.services.impl;

import net.thrymr.constant.Constants;
import net.thrymr.dto.CounsellorSlotDto;
import net.thrymr.dto.response.PaginationResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
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
        for(String slotTime : request.getSlotTimings()){
            slot.getSlotTimings().add(DateUtils.toParseLocalTime(slotTime, Constants.TIME_FORMAT_2));
        }
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
        slot.setIsBooked(Boolean.TRUE);
        slot.setSearchKey(getCounsellorSearchKey(slot));
        counsellorSlotRepo.save(slot);
        return "Counsellor slots saved successfully";
    }

    @Override
    public PaginationResponse getAllCounsellorSlotPagination(CounsellorSlotDto request) {
        Pageable pageable = null;

        if (request.getPageSize() != null && request.getPageNumber() != null) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
        }
        if (request.getPageSize() != null && request.getPageNumber() != null) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.DESC, "createdOn");
        }
        if (request.getIsSorted() != null && request.getIsSorted().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.ASC, "counsellor.counsellorName");
        }
        Specification<CounsellorSlot> counsellorSlotSpecification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            Join<CounsellorSlot, Counsellor> counsellorJoin = root.join("counsellor");
            if (request.getSiteIdList() != null && request.getSiteIdList().isEmpty()) {
                Predicate site = criteriaBuilder.and(counsellorJoin.get("counsellor.site.id").in(request.getSiteIdList()));
                predicateList.add(site);
            }
            if (request.getVendorIdList() != null && request.getVendorIdList().isEmpty()) {
                Predicate vendor = criteriaBuilder.and(counsellorJoin.get("counsellor.vendor.id").in(request.getVendorIdList()));
                predicateList.add(vendor);
            }
            if (request.getShiftTimingList() != null && request.getShiftTimingList().isEmpty()) {
                Predicate shiftTimings = criteriaBuilder.and(counsellorJoin.get("shiftTimings").in(request.getShiftTimingList()));
                predicateList.add(shiftTimings);
            }
            if (Validator.isValid(request.getSearchKey())) {
                Predicate searchPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("searchKey")),
                        "%" + request.getSearchKey().toLowerCase() + "%");
                predicateList.add(searchPredicate);
            }
            Predicate isDeletedPredicate = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
            predicateList.add(isDeletedPredicate);
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        });
        PaginationResponse paginationResponse = new PaginationResponse();
        if (request.getPageNumber() != null && request.getPageSize() != null) {
            Page<CounsellorSlot> counsellorSlotsObject = counsellorSlotRepo.findAll(counsellorSlotSpecification, pageable);
            paginationResponse.setCounsellorSlotList(counsellorSlotsObject.stream().collect(Collectors.toList()));
            paginationResponse.setTotalPages(counsellorSlotsObject.getTotalPages());
            paginationResponse.setTotalElements(counsellorSlotsObject.getTotalElements());
        } else {
            List<CounsellorSlot> counsellorSlotList = counsellorSlotRepo.findAll(counsellorSlotSpecification);
            if (!counsellorSlotList.isEmpty()) {
                paginationResponse.setCounsellorSlotList(counsellorSlotList);
            }
            return paginationResponse;
        }
        return new PaginationResponse();
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
                //Duration duration = Duration.between(LocalTime.now(), optionalCounsellorSlot.get().getStartTime());
                //if (duration.toMinutes() >= 30) {
                    if (Validator.isValid(request.getCounsellorId())) {
                        Optional<Counsellor> optionalCounsellor = counsellorRepo.findById(request.getCounsellorId());
                        optionalCounsellor.ifPresent(counsellorSlot::setCounsellor);
                    } else {
                        return "Counsellor not found";
                    }
                   // if(Validator.isValid(request.getStartTime())) {
                    //    counsellorSlot.setStartTime(DateUtils.toParseLocalTime(request.getStartTime(), Constants.TIME_FORMAT_2));
                   // }if(Validator.isValid(request.getEndTime())) {
                     //   counsellorSlot.setEndTime(DateUtils.toParseLocalTime(request.getEndTime(), Constants.TIME_FORMAT_2));
                   // }
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
       // }
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
                counsellorSlot.setIsBooked(Boolean.TRUE);
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
        //if (counsellorSlot.getStartTime() != null) {
           // searchKey = searchKey + " " + counsellorSlot.getStartTime();
        //}
//        if (counsellorSlot.getEndTime() != null) {
//            searchKey = searchKey + " " + counsellorSlot.getEndTime();
//        }
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
            searchKey = searchKey + " " + counsellorSlot.getCounsellor().getCounsellorName();
        }
        if (counsellorSlot.getCounsellor() != null) {
            searchKey = searchKey + " " + counsellorSlot.getCounsellor().getVendor().getVendorName();
        }
        if (counsellorSlot.getCounsellor() != null) {
            searchKey = searchKey + " " + counsellorSlot.getCounsellor().getSite().getSiteName();
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
