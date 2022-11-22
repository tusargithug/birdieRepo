package net.thrymr.enums;

public enum TagType {
    NONE, PTSD, STRESS, ANXIETY, VT, BURNOUT, DEPRESSION;

    public final TagType getValue(String value) {
        if (value != null && !value.isEmpty()) {
            String upperCase = value.toUpperCase();
            return switch (upperCase) {
                case "PTSD" -> PTSD;
                case "STRESS" -> STRESS;
                case "ANXIETY" -> ANXIETY;
                case "VT" -> VT;
                case "BURNOUT" -> BURNOUT;
                case "DEPRESSION" -> DEPRESSION;
                default -> NONE;
            };
        } else {
            return NONE;
        }
    }
}
