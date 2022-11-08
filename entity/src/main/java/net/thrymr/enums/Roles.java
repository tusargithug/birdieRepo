package net.thrymr.enums;

public enum Roles {
    NONE,EMPLOYEE,WELL_BEING_MANGER,COUNSELLOR,VENDOR,ADMIN,DIRECTOR,TEAM_MANAGER,TEAM_LEADER,OP_STREAM,SITE_MANAGER;

    public static Roles roles(String values){
        if(values!= null && !values.isEmpty()){
            String upperCase=values.toUpperCase();
            return switch (upperCase){
              case "EMPLOYEE"->EMPLOYEE;
              case "WELL_BEING_MANGER","WELL BEING MANGER"->WELL_BEING_MANGER;
              case "COUNSELLOR"->COUNSELLOR;
              case "VENDOR"->VENDOR;
              case "ADMIN"->ADMIN;
              case "DIRECTOR"->DIRECTOR;
              case "TEAM_MANAGER","TEAM MANAGER"->TEAM_MANAGER;
              case "TEAM_LEADER","TEAM LEADER"->TEAM_LEADER;
              case "SITE_MANAGER","SITE MANAGER"->SITE_MANAGER;
              default ->NONE;
            };
        }else {
            return NONE;
        }
    }
}
