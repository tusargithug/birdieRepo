package net.thrymr.services.impl;

import net.thrymr.constant.Constants;
import net.thrymr.dto.AppointmentDto;
import net.thrymr.dto.slotRequest.SlotDetailsDto;
import net.thrymr.dto.slotRequest.TimeSlotDto;
import net.thrymr.enums.SlotShift;
import net.thrymr.enums.SlotStatus;
import net.thrymr.model.AppUser;
import net.thrymr.model.UserAppointment;
import net.thrymr.model.master.MtCounsellor;
import net.thrymr.repository.AppUserRepo;
import net.thrymr.repository.AppointmentRepo;
import net.thrymr.repository.CounsellorRepo;
import net.thrymr.services.AppointmentService;
import net.thrymr.utils.DateUtils;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    AppUserRepo appUserRepo;
    @Autowired
    AppointmentRepo appointmentRepo;
    //    @Autowired
//    CounsellorSlotRepo counsellorSlotRepo;
    @Autowired
    CounsellorRepo counsellorRepo;


    @Override
    public String createAppointment(TimeSlotDto request) {
        UserAppointment appointment = new UserAppointment();
        if (Validator.isValid(request.getAppUserId())) {
            Optional<AppUser> optionalAppUser = appUserRepo.findById(request.getAppUserId());
            if (optionalAppUser.isPresent()) {
                appointment.setAppUser(optionalAppUser.get());
            } else {
                return "user not found";
            }
        }
        List<UserAppointment> userAppointments = new ArrayList<>();
        if (Validator.isValid(request.getSlots())) {
            for (SlotDetailsDto detailsDto : request.getSlots()) {
                appointment.setStartTime(DateUtils.toParseLocalTime(detailsDto.getStartTime(), Constants.TIME_FORMAT_2));
                appointment.setEndTime(DateUtils.toParseLocalTime(detailsDto.getEndTime(), Constants.TIME_FORMAT_2));
                appointment.setDays(DayOfWeek.valueOf(detailsDto.getDayOfWeek()));
                appointment.setSlotStatus(SlotStatus.valueOf(detailsDto.getSlotStatus()));
                appointment.setSlotShift(SlotShift.getType(detailsDto.getSlotShift()));
                if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE)) {
                    appointment.setIsActive(detailsDto.getIsActive());
                }
                Date todayDate = new Date();
                if (detailsDto.getDate() != null) {
                    System.out.println(detailsDto.getDate());
                    try {
                        appointment.setSlotDate(DateUtils.toFormatStringToDate(detailsDto.getToDate(), Constants.DATE_FORMAT));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    if (appointment.getSlotDate() != null) {
                        appointment.setIsCurrentAppointment(Boolean.TRUE);
                    }
                    if (!todayDate.before(appointment.getSlotDate())) {
                        return "Date should not before the Today date";
                    }

                } else if (detailsDto.getFromDate() != null && detailsDto.getToDate() != null) {
                    Date fromDate = null;
                    try {
                        fromDate = DateUtils.toFormatStringToDate(detailsDto.getFromDate(), Constants.DATE_FORMAT);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    Date toDate = null;
                    try {
                        toDate = DateUtils.toFormatStringToDate(detailsDto.getToDate(), Constants.DATE_FORMAT);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    if (todayDate.after(fromDate) && todayDate.before(toDate)) {
                        return "From Date should not before the Today date";
                    }
                    long dateDifference = ChronoUnit.DAYS.between(fromDate.toInstant(), toDate.toInstant());
                    for (int i = 0; i < dateDifference; i++) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(fromDate);
                        cal.add(Calendar.DATE, 1);
                        appointment.setSlotDate(cal.getTime());
                    }
                    //need to implement
                } else {
                    return "required date";
                }
                if (Validator.isValid(request.getCounsellorId())) {
                    Optional<MtCounsellor> optionalCounsellorSlot = counsellorRepo.findById(request.getCounsellorId());
                    if (optionalCounsellorSlot.isPresent()) {
                        appointment.setCounsellor(optionalCounsellorSlot.get());
                    }
                }

                userAppointments.add(appointment);

            }
            appointmentRepo.saveAll(userAppointments);
            return "Appointment created successfully";
        }
        return "please select appointment";
    }

    @Override
    public UserAppointment getAppointmentById(Long id) {
        UserAppointment userAppointment = null;
        if (Validator.isValid(id)) {
            Optional<UserAppointment> optionalUserAppointment = appointmentRepo.findById(id);
            if (optionalUserAppointment.isPresent() && optionalUserAppointment.get().getIsActive().equals(Boolean.TRUE)) {
                userAppointment = optionalUserAppointment.get();
                return userAppointment;
            }
        }
        return new UserAppointment();
    }

    @Override
    public String rescheduledUserAppointment(TimeSlotDto request) throws ParseException {
        UserAppointment userAppointment;
        List<UserAppointment> userAppointmentList = new ArrayList<>();
        if (Validator.isValid(request.getId())) {
            Optional<UserAppointment> optionalUserAppointment = appointmentRepo.findById(request.getId());
            if (optionalUserAppointment.isPresent()) {
                userAppointment = optionalUserAppointment.get();
                Duration duration = Duration.between(LocalTime.now(), optionalUserAppointment.get().getStartTime());
                if (duration.toMinutes() >= 30) {
                    for (SlotDetailsDto detailsDto : request.getSlots()) {
                        switch (request.getSlotShift()) {
                            case "MORNING":
                                userAppointment.setSlotShift(SlotShift.MORNING);
                                break;
                            case "AFTERNOON":
                                userAppointment.setSlotShift(SlotShift.AFTERNOON);
                                break;
                            case "EVENING":
                                userAppointment.setSlotShift(SlotShift.EVENING);
                                break;
                            default:
                                return "please select any shift";
                        }
                        if (Validator.isValid(detailsDto.getStartTime())) {
                            userAppointment.setStartTime(DateUtils.toParseLocalTime(detailsDto.getStartTime(), Constants.TIME_FORMAT_2));
                        }
                        userAppointment.setEndTime(DateUtils.toParseLocalTime(detailsDto.getEndTime(), Constants.TIME_FORMAT_2));
                        userAppointment.setDays(DayOfWeek.valueOf(detailsDto.getDayOfWeek()));
                        userAppointment.setSlotStatus(SlotStatus.valueOf(detailsDto.getSlotStatus()));
                        userAppointment.setSlotShift(SlotShift.getType(detailsDto.getSlotShift()));
                        if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE) || request.getIsActive().equals(Boolean.FALSE)) {
                            userAppointment.setIsActive(detailsDto.getIsActive());
                        }
                        Date todayDate = new Date();
                        if (detailsDto.getDate() != null) {
                            userAppointment.setSlotDate(DateUtils.toFormatStringToDate(detailsDto.getToDate(), Constants.DATE_FORMAT));
                            if (!todayDate.before(userAppointment.getSlotDate())) {
                                return "Date should not before the Today date";
                            }

                        } else if (detailsDto.getFromDate() != null && detailsDto.getToDate() != null) {
                            Date fromDate = DateUtils.toFormatStringToDate(detailsDto.getFromDate(), Constants.DATE_FORMAT);
                            Date toDate = DateUtils.toFormatStringToDate(detailsDto.getToDate(), Constants.DATE_FORMAT);
                            if (todayDate.after(fromDate) && todayDate.before(toDate)) {
                                return "From Date should not before the Today date";
                            }
                            long dateDifference = ChronoUnit.DAYS.between(fromDate.toInstant(), toDate.toInstant());
                            for (int i = 0; i < dateDifference; i++) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(fromDate);
                                cal.add(Calendar.DATE, 1);
                                userAppointment.setSlotDate(cal.getTime());
                            }
                            //need to implement
                        } else {
                            return "required date";
                        }
                        if (Validator.isValid(request.getCounsellorId())) {
                            Optional<MtCounsellor> optionalCounsellor = counsellorRepo.findById(request.getCounsellorId());
                            if (optionalCounsellor.isPresent() && optionalUserAppointment.get().getSlotShift().equals(request.getSlotShift())) {
                                userAppointment.setCounsellor(optionalCounsellor.get());
                            }
                        }

                        userAppointmentList.add(userAppointment);
                    }
                    appointmentRepo.saveAll(userAppointmentList);
                    return "appointment rescheduled successfully";
                } else {
                    return "rescheduling is not possible";
                }
            }
        }
        return "Invalid appointment Id";
    }

    @Override
    public List<UserAppointment> getAllAppointment() {
        List<UserAppointment> userAppointmentList = appointmentRepo.findAll();
        return userAppointmentList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
    }


}
