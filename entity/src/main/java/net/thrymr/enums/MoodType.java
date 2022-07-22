package net.thrymr.enums;

public enum MoodType {

	POSITIVE, NEGATIVE, NEUTRAL,NONE;
	public static MoodType stringToEnum(String value){
		if(value!=null&&!value.isEmpty()){
			String upperCaseValue=value.toUpperCase();
			return switch (upperCaseValue) {
				case "POSITIVE" -> POSITIVE;
				case "NEGATIVE" -> NEGATIVE;
				case "NEUTRAL" -> NEUTRAL;
				default -> NONE;
			};
		}else {
			return NONE;
		}
	}
}
