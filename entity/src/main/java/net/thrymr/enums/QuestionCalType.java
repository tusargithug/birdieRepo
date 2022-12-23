package net.thrymr.enums;


public enum QuestionCalType {
	FORWARD, REVERSE, NONE;
	public  final QuestionCalType getType(String value){
		if(value!=null && !value.isEmpty()){
			String upperCase=value.toUpperCase();
			return switch (upperCase){
				case "FORWARD" -> FORWARD;
				case "REVERSE" -> REVERSE;
				default -> NONE;
			};
		}else {
			return NONE;
		}
	}
}
