package net.thrymr.enums;

public enum FileType {

    VIDEO, IMAGE, DOCUMENT,PDF,AUDIO,ZIP,EMOJI, NONE;

    public final FileType getValue(String value){
        if(value!=null && !value.isEmpty()) {
            String upperCase = value.toUpperCase();
            return switch (upperCase) {
                case "VIDEO" -> VIDEO;
                case "AUDIO" -> AUDIO;
                case "DOCUMENT" -> DOCUMENT;
                case "IMAGE" -> IMAGE;
                case "PDF" -> PDF;
                case "ZIP" -> ZIP;
                case "EMOJI" -> EMOJI;
                default -> NONE;
            };
        }else {
            return NONE;
        }
    }

}
