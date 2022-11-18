package net.thrymr.enums;

public enum SlotShift {
    NONE, MORNING, AFTERNOON, EVENING;

    public static SlotShift getType(String name) {
        if (name != null && !name.isEmpty()) {
            String upperCase = name.toUpperCase();
            return switch (upperCase) {
                case "morning", "MORNING" -> MORNING;
                case "afternoon", "AFTERNOON" -> AFTERNOON;
                case "evening", "EVENING" -> EVENING;
                default -> NONE;
            };
        } else {
            return NONE;
        }
    }
}