package com.myflx.autoconfig.encapsulate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDTO {
    private String userName;
    private String password;
    private HashMap<Integer, List<String>> myMap;

    public HashMap<Integer, List<String>> getMyMap() {
        return myMap;
    }

    public void setMyMap(HashMap<Integer, List<String>> myMap) {
        this.myMap = myMap;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
