package net.thrymr.enums;

public enum SlotStatus {
	AVAILABLE, BOOKED, BLOCKED,DELETED,RESCHEDULED,NONE;
	public static SlotStatus getValue(String value){
		if(value!=null && value.isEmpty()){
			String upperCase=value.toUpperCase();
			return switch (upperCase){
				case "available","AVAILABLE"->AVAILABLE;
				case "booked","BOOKED"->BOOKED;
				case "blocked","BLOCKED"->BLOCKED;
				case "deleted","DELETED"->DELETED;
				case "rescheduled","RESCHEDULED"->RESCHEDULED;
				default->NONE;
			};
		}else {
			return NONE;
		}
	}
}