package net.thrymr.enums;

public enum Alerts {
    RED_ALERT,ORANGE_ALERT,GREEN_ALERT,NONE;
    public static Alerts alerts(String value){
        if(value!=null && !value.isEmpty()){
            String upperCase=value.toUpperCase();
            return switch (upperCase){
                case "RED_ALERT" ,"RED ALERT" ->RED_ALERT;
                case "ORANGE_ALERT","ORANGE ALERT" ->ORANGE_ALERT;
                case "GREEN_ALERT","GREEN ALERT"->GREEN_ALERT;
                default -> NONE;
            };
        }else {
            return NONE;
        }
    }
}
