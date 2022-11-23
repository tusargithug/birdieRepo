package net.thrymr.enums;

public enum Gender {
    MALE, FEMALE, OTHERS,NONE;

    public static Gender getValue(String value){
        if(value!=null&&!value.isEmpty()) {
            String upperCaseValue = value.toUpperCase();
            return switch (upperCaseValue) {
                case "MALE" -> MALE;
                case "FEMALE" -> FEMALE;
                case "OTHERS" -> OTHERS;
                default -> NONE;
            };
        }else {
            return NONE;
        }
    }
}

