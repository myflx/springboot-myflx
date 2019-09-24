package com.myflx.autoconfig.encapsulate;

import java.util.Objects;

public class UserServiceImpl implements IUserService {
    public boolean access(UserDTO userDTO) {
        return Objects.nonNull(userDTO) && Objects.equals(userDTO.getUserName(), "admin") &&
                Objects.equals(userDTO.getPassword(), "admin");
    }
}
