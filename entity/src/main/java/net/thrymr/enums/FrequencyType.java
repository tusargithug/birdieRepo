package net.thrymr.enums;

public enum FrequencyType {
    NONE,ONE_TIME,RECURRING;
    public final FrequencyType getType(String value){
        if(value!=null && value.isEmpty()){
            String upperCase=value.toUpperCase();
            return switch (upperCase){
                case "ONE_TIME","ONE TIME" -> ONE_TIME;
                case "RECURRING" -> RECURRING;
                default -> NONE;
            };
        }else {
           return NONE;
        }
    }
}
