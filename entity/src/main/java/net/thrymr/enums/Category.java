package net.thrymr.enums;


public enum Category {
    WORK,
    SELF,
    NONE,
    WORK_SELF;

    public static Category stringToEnum(String value) {
        if (value != null && !value.isEmpty()) {
            String upperCaseValue = value.toUpperCase();
            return switch (upperCaseValue) {
                case "WORK" -> WORK;
                case "SELF" -> SELF;
                case "WORK/SELF" -> WORK_SELF;
                default -> NONE;
            };
        } else {
            return NONE;
        }
    }

}
