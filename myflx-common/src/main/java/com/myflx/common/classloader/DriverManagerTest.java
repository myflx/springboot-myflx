package com.myflx.common.classloader;


import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author LuoShangLin
 */
public class DriverManagerTest {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/fe?characterEncoding=UTF-8";
        // 通过java库获取数据库连接
        Connection conn = java.sql.DriverManager.getConnection(url, "root", "root@0001");
        System.out.println(conn);
    }
}
