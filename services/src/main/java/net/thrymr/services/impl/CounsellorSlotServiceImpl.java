package net.thrymr.services.impl;

import net.thrymr.constant.Constants;
import net.thrymr.dto.CounsellorSlotDto;
import net.thrymr.dto.response.*;
import net.thrymr.enums.SlotShift;
import net.thrymr.enums.SlotStatus;
import net.thrymr.model.*;
import net.thrymr.repository.*;
import net.thrymr.services.CounsellorSlotService;
import net.thrymr.utils.DateUtils;
import net.thrymr.utils.Validator;
import org.apache.commons.collections4.CollectionUtils;
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

    @Autowired
    CounsellorSlotTimingsRepo counsellorSlotTimingsRepo;


    @Override
    public String createCounsellorSlot(CounsellorSlotDto request) throws ParseException {
        CounsellorSlot slot = new CounsellorSlot();
        Date fromDate = DateUtils.toFormatStringToDate(request.getFromDate(), Constants.DATE_FORMAT);
        Date toDate = DateUtils.toFormatStringToDate(request.getToDate(), Constants.DATE_FORMAT);
        long interval = 86400000;
        long endTime = toDate.getTime();
        long curTime = fromDate.getTime();
        List<Date> dateList = new ArrayList<>();
        while (curTime <= endTime) {
            dateList.add(new Date(curTime));
            curTime += interval;
        }
        List<CounsellorSlotTimings> counsellorSlotTimingsList = new ArrayList<>();
        if (Validator.isValid(request.getCounsellorId())) {
            Optional<Counsellor> optionalCounsellor = counsellorRepo.findById(request.getCounsellorId());
            if (optionalCounsellor.isPresent()) {
                slot.setCounsellor(optionalCounsellor.get());
            } else {
                return "This counsellor id not present in database";
            }
            for (Date date : dateList) {
                slot.getCounsellor().setAvailableSlots( slot.getCounsellor().getAvailableSlots() + 12);
                for (String slotTimings : request.getSlotTimings()) {
                    slot.getCounsellor().setAvailableSlots(slot.getCounsellor().getAvailableSlots() - 1);
                }
            }
        } else {
            return "Counsellor not found";
        }
        Date todayDate = new Date();
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
        slot.setSearchKey(getCounsellorSearchKey(slot));
        counsellorSlotRepo.save(slot);
        if (request.getSlotTimings() != null && !request.getSlotTimings().isEmpty()) {
            for (String slotTime : request.getSlotTimings()) {
                for (Date date : dateList) {
                    CounsellorSlotTimings counsellorSlotTimings = new CounsellorSlotTimings();
                    Optional<Counsellor> optionalCounsellor = counsellorRepo.findById(request.getCounsellorId());
                    optionalCounsellor.ifPresent(counsellorSlotTimings::setCounsellor);
                    counsellorSlotTimings.setSlotStatus(SlotStatus.BOOKED);
                    counsellorSlotTimings.setSlotTiming(DateUtils.toParseLocalTime(slotTime, Constants.TIME_FORMAT_2));
                    LocalTime localTime = DateUtils.toParseLocalTime(slotTime, Constants.TIME_FORMAT_2);
                    LocalTime localTime1 = DateUtils.toParseLocalTime("12:00", Constants.TIME_FORMAT_2);
                    LocalTime localTime2 = DateUtils.toParseLocalTime("16:00", Constants.TIME_FORMAT_2);
                    if (localTime.isBefore(localTime1)) {
                        counsellorSlotTimings.setSlotShift(SlotShift.MORNING);
                    }
                    if (localTime.equals(localTime1) || (localTime.isAfter(localTime1) && localTime.isBefore(localTime2))) {
                        counsellorSlotTimings.setSlotShift(SlotShift.AFTERNOON);
                    }
                    if (localTime.isAfter(localTime2)) {
                        counsellorSlotTimings.setSlotShift(SlotShift.EVENING);
                    }
                    counsellorSlotTimings.setSlotDate(date);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    DayOfWeek[] Days = new DayOfWeek[]{DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
                    counsellorSlotTimings.setSlotDay(Days[cal.get(Calendar.DAY_OF_WEEK) - 1]);
                    counsellorSlotTimings.setSlotDays(request.getSlotDays());
                    counsellorSlotTimings.setSearchKey(getCounsellorSlotTimingsSearchKey(counsellorSlotTimings));
                    counsellorSlotTimingsList.add(counsellorSlotTimings);
                }
            }
            counsellorSlotTimingsRepo.saveAll(counsellorSlotTimingsList);
        }
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
            return paginationResponse;
        } else {
            List<CounsellorSlot> counsellorSlotList = counsellorSlotRepo.findAll(counsellorSlotSpecification);
            if (!counsellorSlotList.isEmpty()) {
                paginationResponse.setCounsellorSlotList(counsellorSlotList);
                return paginationResponse;
            }
        }
        return new PaginationResponse();
    }

    @Override
    public List<CounsellorSlotResponse> getCounsellorSlotById(Long counsellorId) {
        if (Validator.isValid(counsellorId)) {
            List<CounsellorSlotTimings> counsellorSlotTimingsList = counsellorSlotTimingsRepo.findAllByCounsellorId(counsellorId);
            if (!counsellorSlotTimingsList.isEmpty()) {
                List<CounsellorSlotResponse> counsellorSlotResponsesList = new ArrayList<>();
                for (CounsellorSlotTimings counsellorSlotTimings : counsellorSlotTimingsList) {
                    Optional<CounsellorSlotResponse> counsellorSlotResponseOptional = counsellorSlotResponsesList.stream().filter(counsellorSlotResponse -> counsellorSlotResponse.getId().equals(counsellorSlotTimings.getCounsellor().getId())).findAny();
                    var counsellorSlotResponse = counsellorSlotResponseOptional.orElse(new CounsellorSlotResponse());
                    counsellorSlotResponse.setId(counsellorSlotTimings.getCounsellor().getId());
                    counsellorSlotResponse.setCounsellorName(counsellorSlotTimings.getCounsellor().getCounsellorName());
                    counsellorSlotResponse.setShiftTimings(counsellorSlotTimings.getCounsellor().getShiftTimings());
                    counsellorSlotResponse.setVendorName(counsellorSlotTimings.getCounsellor().getVendor().getVendorName());
                    counsellorSlotResponse.setSiteName(counsellorSlotTimings.getCounsellor().getSite().getSiteName());
                    counsellorSlotResponse.setIsActive(counsellorSlotTimings.getIsActive());
                    counsellorSlotResponse.setIsDeleted(counsellorSlotTimings.getIsDeleted());
                    SlotDetailsResponse slotDetailsResponse = new SlotDetailsResponse();
                    slotDetailsResponse.setSlotTiming(counsellorSlotTimings.getSlotTiming());
                    slotDetailsResponse.setSlotDay(counsellorSlotTimings.getSlotDay());
                    slotDetailsResponse.setSlotDays(counsellorSlotTimings.getSlotDays());
                    slotDetailsResponse.setSlotDate(counsellorSlotTimings.getSlotDate());
                    slotDetailsResponse.setSlotShift(counsellorSlotTimings.getSlotShift());
                    slotDetailsResponse.setSlotStatus(counsellorSlotTimings.getSlotStatus());
                    counsellorSlotResponse.getSlotDetailsResponses().add(slotDetailsResponse);
                    if (!counsellorSlotResponseOptional.isPresent()) {
                        counsellorSlotResponsesList.add(counsellorSlotResponse);
                    }
                }
                return counsellorSlotResponsesList.stream().filter(counsellorSlotResponse -> counsellorSlotResponse.getIsDeleted().equals(Boolean.FALSE)).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String updateCounsellorSlot(CounsellorSlotDto request) throws ParseException {
        List<CounsellorSlotTimings> counsellorSlotTimingsList = new ArrayList<>();
        if (Validator.isValid(request.getCounsellorId())) {
            List<CounsellorSlot> counsellorSlotList = counsellorSlotRepo.findAllByCounsellorId(request.getCounsellorId());
            CounsellorSlot slot = null;
            if (!counsellorSlotList.isEmpty()) {
                for (CounsellorSlot counsellorSlot : counsellorSlotList) {
                    Optional<Counsellor> optionalCounsellor = counsellorRepo.findById(request.getCounsellorId());
                    optionalCounsellor.ifPresent(counsellorSlot::setCounsellor);
                    Date todayDate = new Date();
                    if (Validator.isValid(request.getFromDate()) && Validator.isValid(request.getToDate())) {
                        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(request.getFromDate());
                        if (date2.getDate() == todayDate.getDate() || (date2.after(todayDate) && (request.getToDate() != null && request.getFromDate() != null))) {
                            counsellorSlot.setFromDate(DateUtils.toFormatStringToDate(request.getFromDate(), Constants.DATE_FORMAT));
                            counsellorSlot.setToDate(DateUtils.toFormatStringToDate(request.getToDate(), Constants.DATE_FORMAT));
                        } else {
                            return "please provide from today's date";
                        }
                    }
                    counsellorSlot.setSearchKey(getCounsellorSearchKey(counsellorSlot));
                    counsellorSlotRepo.save(counsellorSlot);
                }
                if (request.getSlotTimings() != null && !request.getSlotTimings().isEmpty()) {
                    List<LocalTime> slotTimingsFromRequest = new ArrayList<>();
                    for (String slotTime : request.getSlotTimings()) {
                        slotTimingsFromRequest.add(DateUtils.toParseLocalTime(slotTime, Constants.TIME_FORMAT_2));
                    }
                    if (!slotTimingsFromRequest.isEmpty()) {
                        Optional<Counsellor> optionalCounsellor1 = counsellorRepo.findById(request.getCounsellorId());
                        Counsellor counsellor = null;
                        if (optionalCounsellor1.isPresent()) {
                            counsellor = optionalCounsellor1.get();
                        }
                        for (LocalTime slotTime : slotTimingsFromRequest) {
                            Date fromDate = DateUtils.toFormatStringToDate(request.getFromDate(), Constants.DATE_FORMAT);
                            Date toDate = DateUtils.toFormatStringToDate(request.getToDate(), Constants.DATE_FORMAT);
                            long interval = 86400000; // 1 hour in milliseconds
                            long endTime = toDate.getTime(); // create your endtime here, possibly using Calendar or Date
                            long curTime = fromDate.getTime();
                            List<Date> dateList = new ArrayList<>();
                            while (curTime <= endTime) {
                                dateList.add(new Date(curTime));
                                curTime += interval;
                            }
                            for (Date date : dateList) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                if (!counsellorSlotTimingsRepo.existsBySlotTimingAndCounsellorIdAndSlotDateAndSlotDay(slotTime, request.getCounsellorId(), date, DayOfWeek.of(cal.get(Calendar.DAY_OF_WEEK)))) {
                                    CounsellorSlotTimings insertNewRecord = new CounsellorSlotTimings();
                                    insertNewRecord.setCounsellor(counsellor);
                                    insertNewRecord.setSlotStatus(SlotStatus.BOOKED);
                                    insertNewRecord.setSlotTiming(DateUtils.toParseLocalTime(String.valueOf(slotTime), Constants.TIME_FORMAT_2));
                                    LocalTime localTime = DateUtils.toParseLocalTime(String.valueOf(slotTime), Constants.TIME_FORMAT_2);
                                    LocalTime localTime1 = DateUtils.toParseLocalTime("12:00", Constants.TIME_FORMAT_2);
                                    LocalTime localTime2 = DateUtils.toParseLocalTime("16:00", Constants.TIME_FORMAT_2);
                                    if (localTime.isBefore(localTime1)) {
                                        insertNewRecord.setSlotShift(SlotShift.MORNING);
                                    }
                                    if (localTime.equals(localTime1) || (localTime.isAfter(localTime1) && localTime.isBefore(localTime2))) {
                                        insertNewRecord.setSlotShift(SlotShift.AFTERNOON);
                                    }
                                    if (localTime.equals(localTime2) || localTime.isAfter(localTime2)) {
                                        insertNewRecord.setSlotShift(SlotShift.EVENING);
                                    }
                                    insertNewRecord.setSlotDate(date);
                                    insertNewRecord.setSlotDay(DayOfWeek.of(cal.get(Calendar.DAY_OF_WEEK)));
                                    for (DayOfWeek slotDay : request.getSlotDays()) {
                                        if (!insertNewRecord.getSlotDays().contains(slotDay)) {
                                            insertNewRecord.setSlotDays(request.getSlotDays());
                                        }
                                    }
                                    insertNewRecord.getCounsellor().setAvailableSlots(insertNewRecord.getCounsellor().getAvailableSlots() - 1);
                                    insertNewRecord.setSearchKey(getCounsellorSlotTimingsSearchKey(insertNewRecord));
                                    counsellorSlotTimingsList.add(insertNewRecord);
                                } else {
                                    List<CounsellorSlotTimings> counsellorSlotTimingsList1 = counsellorSlotTimingsRepo.findAll();
                                    for (CounsellorSlotTimings counsellorSlotTimings : counsellorSlotTimingsList1) {
                                        for (DayOfWeek slotDay1 : request.getSlotDays()) {
                                            if (!counsellorSlotTimings.getSlotDays().contains(slotDay1) && (counsellorSlotTimings.getSlotTiming().equals(DateUtils.toParseLocalTime(String.valueOf(slotTime), Constants.TIME_FORMAT_2))
                                                    && !counsellorSlotTimings.getSlotDate().equals(DateUtils.toFormatStringToDate(request.getFromDate(), Constants.DATE_FORMAT)))) {
                                                counsellorSlotTimings.getSlotDays().add(slotDay1);
                                            }
                                        }
                                    }
                                }
                                counsellorSlotTimingsRepo.saveAll(counsellorSlotTimingsList);
                            }
                        }
                    }
                    return "Counsellor slots updated successfully";
                }
            }
        }
        return "This counsellor id not present in slot database";
    }
    @Override
    public String removeCounsellorSlotsById(CounsellorSlotDto request) throws ParseException {
        if (Validator.isValid(request.getCounsellorId()) && counsellorSlotTimingsRepo.existsByCounsellorId(request.getCounsellorId())) {
            Date fromDate = DateUtils.toFormatStringToDate(request.getFromDate(), Constants.DATE_FORMAT);
            Date toDate = DateUtils.toFormatStringToDate(request.getToDate(), Constants.DATE_FORMAT);
            long interval = 86400000;
            long endTime = toDate.getTime();
            long curTime = fromDate.getTime();
            List<Date> dateList = new ArrayList<>();
            while (curTime <= endTime) {
                dateList.add(new Date(curTime));
                curTime += interval;
            }
            List<LocalTime> slotTimingsFromRequest = new ArrayList<>();
            for (String slotTime : request.getSlotTimings()) {
                slotTimingsFromRequest.add(DateUtils.toParseLocalTime(slotTime, Constants.TIME_FORMAT_2));
            }
            for (LocalTime slotTime : slotTimingsFromRequest) {
                for (Date date : dateList) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    List<CounsellorSlotTimings> counsellorSlotTimingsList = counsellorSlotTimingsRepo.findAllBySlotTimingAndCounsellorIdAndSlotDateAndSlotDay(slotTime, request.getCounsellorId(), date, DayOfWeek.of(cal.get(Calendar.DAY_OF_WEEK)));
                    if (!counsellorSlotTimingsList.isEmpty()) {
                        for (CounsellorSlotTimings counsellorSlotTimings : counsellorSlotTimingsList) {
                            if (counsellorSlotTimingsRepo.existsBySlotTimingAndCounsellorIdAndSlotDateAndSlotDay(slotTime, request.getCounsellorId(), date, DayOfWeek.of(cal.get(Calendar.DAY_OF_WEEK)))) {
                                counsellorSlotTimings.setIsActive(Boolean.FALSE);
                                counsellorSlotTimings.setIsDeleted(Boolean.TRUE);
                                counsellorSlotTimings.setSlotStatus(SlotStatus.DELETED);
                                for(DayOfWeek slotDay : request.getSlotDays()){
                                    counsellorSlotTimings.getSlotDays().remove(slotDay);
                                }
                                counsellorSlotTimingsRepo.save(counsellorSlotTimings);
                            }
                        }
                    }
                }
            }
            return "slot removed successfully successfully";
        }
        return "This counsellor id is not present database";
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

    @Override
    public String pauseCounsellorSlotsById(CounsellorSlotDto request) throws ParseException {
        if (Validator.isValid(request.getCounsellorId()) && counsellorSlotTimingsRepo.existsByCounsellorId(request.getCounsellorId())) {
            Date fromDate = DateUtils.toFormatStringToDate(request.getFromDate(), Constants.DATE_FORMAT);
            Date toDate = DateUtils.toFormatStringToDate(request.getToDate(), Constants.DATE_FORMAT);
            long interval = 86400000; // 1 hour in milliseconds
            long endTime = toDate.getTime(); // create your endtime here, possibly using Calendar or Date
            long curTime = fromDate.getTime();
            List<Date> dateList = new ArrayList<>();
            while (curTime <= endTime) {
                dateList.add(new Date(curTime));
                curTime += interval;
            }
            List<LocalTime> slotTimingsFromRequest = new ArrayList<>();
            for (String slotTime : request.getSlotTimings()) {
                slotTimingsFromRequest.add(DateUtils.toParseLocalTime(slotTime, Constants.TIME_FORMAT_2));
            }
            for (LocalTime slotTime : slotTimingsFromRequest) {
                for (Date date : dateList) {
                    if (Validator.isValid(request.getCounsellorId())) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        List<CounsellorSlotTimings> counsellorSlotTimingsList = counsellorSlotTimingsRepo.findAllBySlotTimingAndCounsellorIdAndSlotDateAndSlotDay(slotTime, request.getCounsellorId(), date, DayOfWeek.of(cal.get(Calendar.DAY_OF_WEEK)));
                        if (!counsellorSlotTimingsList.isEmpty()) {
                            for (CounsellorSlotTimings counsellorSlotTimings : counsellorSlotTimingsList) {
                                if (counsellorSlotTimingsRepo.existsBySlotTimingAndCounsellorIdAndSlotDateAndSlotDay(slotTime, request.getCounsellorId(), date, DayOfWeek.of(cal.get(Calendar.DAY_OF_WEEK)))) {
                                    counsellorSlotTimings.setIsActive(Boolean.FALSE);
                                    counsellorSlotTimings.setSlotStatus(SlotStatus.BLOCKED);
                                    counsellorSlotTimingsRepo.save(counsellorSlotTimings);
                                }
                            }
                        }
                    }
                }
            }
            return "slot paused successfully";
        }
        return "This counsellor id not present in database";
    }

    public String getCounsellorSearchKey(CounsellorSlot counsellorSlot) {
        String searchKey = "";
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

    public String getCounsellorSlotTimingsSearchKey(CounsellorSlotTimings counsellorSlotTimings) {
        String searchKey = "";
        if (counsellorSlotTimings.getCounsellor() != null) {
            searchKey = searchKey + " " + counsellorSlotTimings.getCounsellor().getCounsellorName();
        }
        if (counsellorSlotTimings.getCounsellor() != null) {
            searchKey = searchKey + " " + counsellorSlotTimings.getCounsellor().getVendor().getVendorName();
        }
        if (counsellorSlotTimings.getCounsellor() != null) {
            searchKey = searchKey + " " + counsellorSlotTimings.getCounsellor().getSite().getSiteName();
        }
        if (counsellorSlotTimings.getSlotTiming() != null) {
            searchKey = searchKey + " " + counsellorSlotTimings.getSlotTiming();
        }
        if (counsellorSlotTimings.getCounsellor().getShiftTimings() != null ){
            searchKey = searchKey + " " + counsellorSlotTimings.getCounsellor().getShiftTimings();
        }
        if(counsellorSlotTimings.getCounsellor().getAvailableSlots() != 0) {
            searchKey = searchKey + " " + counsellorSlotTimings.getCounsellor().getAvailableSlots();
        }
        return searchKey;
    }

}
