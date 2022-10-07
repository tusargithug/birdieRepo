package net.thrymr.enums;

public enum AppointmentStatuses {
    NO_SHOW,
    CANCELLED,
    BOOKED,
    CHECK_IN,
    RESCHEDULE,
    PENDING;




    public String getSimpleName(){
        if (this.name().equals(AppointmentStatuses.BOOKED.name())) {
            return "Booked";

        }else if(this.name().equals(AppointmentStatuses.CHECK_IN.name())){
            return "Checked In";
        } else if (this.name().equals(AppointmentStatuses.CANCELLED.name())) {
            return "Cancelled";

        }else if(this.name().equals(AppointmentStatuses.NO_SHOW.name())){
            return "No Show";
        } else if (this.name().equals(AppointmentStatuses.RESCHEDULE.name())) {
            return "Reschedule";

        }else if(this.name().equals(AppointmentStatuses.PENDING.name())){
            return "Pending";
        }    else {
            return null;
        }

}

}
