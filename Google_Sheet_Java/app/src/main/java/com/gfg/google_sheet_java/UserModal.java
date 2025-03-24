package com.gfg.google_sheet_java;

public class UserModal {
    String first_name;
    String last_name;
    String email;
    String avatar;

    UserModal(String first_name, String last_name, String email, String avatar){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.avatar = avatar;
    }

    String getFirst_name(){
        return first_name;
    }

    String getLast_name(){
        return last_name;
    }

    String getEmail(){
        return email;
    }

    String getAvatar(){
        return avatar;
    }
}
