package net.thrymr.enums;

public enum SlotShift {
    MORNING, AFTERNOON, EVENING;

    public static SlotShift getType(String name){
        return switch (name) {
            case "morning", "MORNING" -> MORNING;
            case "afternoon", "AFTERNOON" -> AFTERNOON;
            case "evening", "EVENING" -> EVENING;
            default -> null;
        };
    }
}