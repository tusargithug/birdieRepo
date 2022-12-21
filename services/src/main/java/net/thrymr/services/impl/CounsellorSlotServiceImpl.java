package net.thrymr.services.impl;

import net.thrymr.constant.Constants;
import net.thrymr.dto.CounsellorSlotDto;
import net.thrymr.dto.response.PaginationResponse;
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
import java.time.DayOfWeek;
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
            for (String slotTimings : request.getSlotTimings()) {
                optionalCounsellor.get().setAvailableSlots(optionalCounsellor.get().getAvailableSlots() - 1);
            }
        } else {
            return "Counsellor not found";
        }
        for (String slotTime : request.getSlotTimings()) {
            slot.getSlotTimings().add(DateUtils.toParseLocalTime(slotTime, Constants.TIME_FORMAT_2));
        }
        slot.setSlotDays(request.getSlotDays());
        slot.setSlotStatus(SlotStatus.BOOKED);
        slot.setSlotShift(request.getSlotShift());
        Date todayDate = new Date();
        /*if (Validator.isValid(request.getSlotDate())) {
            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(request.getSlotDate());

            if (date1.getDate() == todayDate.getDate() || date1.after(todayDate)) {
                slot.setSlotDate(DateUtils.toFormatStringToDate(request.getSlotDate(), Constants.DATE_FORMAT));
            } else {
                return "please provide from today's date";
            }
        }*/
        if (Validator.isValid(request.getFromDate()) && Validator.isValid(request.getToDate())) {
            Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(request.getFromDate());
            if (date2.getDate() == todayDate.getDate() || (date2.after(todayDate) && (request.getToDate() != null && request.getFromDate() != null))) {
                slot.setFromDate(DateUtils.toFormatStringToDate(request.getFromDate(), Constants.DATE_FORMAT));
                slot.setToDate(DateUtils.toFormatStringToDate(request.getToDate(), Constants.DATE_FORMAT));
            } else {
                return "please provide from today's date";
            }
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
    public List<CounsellorSlot> getCounsellorSlotById(Long counsellorId) {
        if (Validator.isValid(counsellorId)) {
            List<CounsellorSlot> counsellorSlotList = counsellorSlotRepo.findAllByCounsellorId(counsellorId);
            if(!counsellorSlotList.isEmpty()){
                return counsellorSlotList;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String updateCounsellorSlot(CounsellorSlotDto request) throws ParseException {
        if (Validator.isValid(request.getCounsellorId())) {
            List<CounsellorSlot> counsellorSlotList = counsellorSlotRepo.findAllByCounsellorId(request.getCounsellorId());
            if (!counsellorSlotList.isEmpty()) {
                for (CounsellorSlot slot : counsellorSlotList) {
                    Optional<Counsellor> optionalCounsellor = counsellorRepo.findById(request.getCounsellorId());
                    optionalCounsellor.ifPresent(slot::setCounsellor);
                    for (String slotTimings : request.getSlotTimings()) {
                        optionalCounsellor.get().setAvailableSlots(optionalCounsellor.get().getAvailableSlots() - 1);
                    }
                    for (String slotTime : request.getSlotTimings()) {
                        slot.getSlotTimings().add(DateUtils.toParseLocalTime(slotTime, Constants.TIME_FORMAT_2));
                    }
                    if (!request.getSlotDays().isEmpty()) {
                        for(DayOfWeek dayOfWeek : request.getSlotDays()) {
                            slot.getSlotDays().add(dayOfWeek);
                        }
                    }
                    slot.setSlotStatus(SlotStatus.BOOKED);
                    if (request.getSlotShift() !=null && !request.getSlotShift().isEmpty()) {
                        for(SlotShift slotShift : request.getSlotShift()) {
                            slot.getSlotShift().add(slotShift);
                        }
                    }
                    Date todayDate = new Date();
                    /*if (Validator.isValid(request.getSlotDate())) {
                        Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(request.getSlotDate());
                        if (date1.getDate() == todayDate.getDate() || date1.after(todayDate)) {
                            slot.setSlotDate(DateUtils.toFormatStringToDate(request.getSlotDate(), Constants.DATE_FORMAT));
                        } else {
                            return "please provide from today's date";
                        }
                    }*/
                    if (Validator.isValid(request.getFromDate()) && Validator.isValid(request.getToDate())) {
                        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(request.getFromDate());
                        if (date2.getDate() == todayDate.getDate() || date2.after(todayDate) && (request.getToDate() != null && !todayDate.before(date2))) {
                            slot.setFromDate(DateUtils.toFormatStringToDate(request.getFromDate(), Constants.DATE_FORMAT));
                            slot.setToDate(DateUtils.toFormatStringToDate(request.getToDate(), Constants.DATE_FORMAT));
                        } else {
                            return "please provide from today's date";
                        }
                    }
                    if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE)) {
                        slot.setIsActive(request.getIsActive());
                    }
                    slot.setIsBooked(Boolean.TRUE);
                    slot.setSearchKey(getCounsellorSearchKey(slot));
                    counsellorSlotRepo.save(slot);
                    return "Counsellor slots updated successfully";
                }
            }
        }
        return "This counsellor id not present in slot management database";
    }

    @Override
    public String removeCounsellorSlotsById(CounsellorSlotDto request) {
        if (Validator.isValid(request.getCounsellorId())) {
            List<CounsellorSlot> counsellorSlotList = counsellorSlotRepo.findAllByCounsellorId(request.getCounsellorId());
            if (!counsellorSlotList.isEmpty()) {
                for (CounsellorSlot counsellorSlot : counsellorSlotList) {
                    if (!request.getSlotDays().isEmpty() || !request.getSlotTimings().isEmpty()) {
                        for (String slotTime : request.getSlotTimings()) {
                            LocalTime localTimes = DateUtils.toParseLocalTime(slotTime, Constants.TIME_FORMAT_2);
                            if (counsellorSlot.getSlotTimings().contains(localTimes)) {
                                counsellorSlot.getSlotTimings().remove(localTimes);
                                counsellorSlotRepo.save(counsellorSlot);
                            }
                            counsellorSlot.getCounsellor().setAvailableSlots(counsellorSlot.getCounsellor().getAvailableSlots() + 1);
                        }
                        for (DayOfWeek dayOfWeeks : request.getSlotDays()) {
                            if (counsellorSlot.getSlotDays().contains(dayOfWeeks)) {
                                counsellorSlot.getSlotDays().remove(dayOfWeeks);
                                counsellorSlotRepo.save(counsellorSlot);
                            }
                        }
                        return "slot removed successfully";
                    }
                    return "please provide slot timings or slot days";
                }
            }
            return "slot details list is empty";
        }
        return "please provide valid counsellor id";
    }

    @Override
    public String deleteAllCounsellorSlots() {
        List<CounsellorSlot> counsellorSlotList = counsellorSlotRepo.findAll();
        if (!counsellorSlotList.isEmpty()) {
            for (CounsellorSlot counsellorSlot : counsellorSlotList) {
                    counsellorSlot.setIsActive(Boolean.FALSE);
                    counsellorSlot.setIsDeleted(Boolean.TRUE);
                    counsellorSlotRepo.save(counsellorSlot);
            }
            return "deleted successfully";
        }
        return "counsellor List is empty";
    }

    public String getCounsellorSearchKey(CounsellorSlot counsellorSlot) {
        String searchKey = "";
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
        if (counsellorSlot.getFromDate() != null) {
            searchKey = searchKey + " " + counsellorSlot.getFromDate();
        }
        if (counsellorSlot.getToDate() != null) {
            searchKey = searchKey + " " + counsellorSlot.getToDate();
        }
        return searchKey;
    }

}
