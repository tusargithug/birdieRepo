package net.thrymr.enums;

public enum TestStyles {
    NONE,ARIAL,ARIAL_ITALIC,ARIAL_BOLD,ARIAL_BOLD_ITALIC;
    public final TestStyles getType(String value){
        if(value!=null && !value.isEmpty()) {
            String upperCase = value.toUpperCase();
            return switch (upperCase) {
                case "ARIAL"->ARIAL;
                case "ARIAL_ITALIC","ARIAL ITALIC"->ARIAL_ITALIC;
                case "ARIAL_BOLD","ARIAL BOLD"->ARIAL_BOLD;
                case "ARIAL_BOLD_ITALIC","ARIAL BOLD ITALIC"->ARIAL_BOLD_ITALIC;
                default -> NONE;
            };
        }else {
            return NONE;
        }
    }
}
