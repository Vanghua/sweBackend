package com.example.demo;

import classer.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

// 加上@RestController注解表示这个代码是专门面处理网络请求的，当收到请求后会通过下述的Mapping进行匹配，如果匹配成功则执行对应代码
@RestController
public class HelloController {
    // 假设当前代码在http://39.96.175.246:8051上运行
    // 接收get请求: 如果请求的url是http://39.96.175.246:8051/api/test，且请求类型是get，那么将执行下述代码
    // 下述代码的作用是测试是否交互成功
    @GetMapping("/api/test")
    public String test(){
        // return表示向web服务器发送响应，响应内容是"通过get请求域服务器交互成功
        return "通过get请求与服务器交互成功";
    }
    @GetMapping("/test")
    public String hello(){
        return "赵玉淋太好了";
    }

    // 接收Post请求: 如果请求的url是http://39.96.175.246:8051/api/login，且请求类型是post，那么将执行下述代码
    // 下述代码的作用是测试简略的登录功能
    // @RequestBody作用是:这个请求请求体内容是一个User类型(前端发送的数据格式符合已经声明的User类格式)，为了校验登录信息是否正确，下述代码需要接受
    // 但是在传输过程中对象是被转换成字符串的，此时@RequestBody将字符串转换成User对象
    @PostMapping("/api/login")
    public String login(@RequestBody User user){
        // 如果用户名是Danny那么反馈给前端"登陆成功"
        if(user.getName().equals("Danny"))
            return "登录成功";
        else
            return "登录失败";
    }

    // 接收Post请求: 如果请求的url是http://39.96.175.246:8051/api/userName，且请求类型是post，那么将执行下述代码
    // 下述代码的职能是测试与数据库的连接
    @PostMapping("api/Sql")
    public List<String> Sql() {
        List<String> ar = new ArrayList<String>();
        Connection conn = null;
        // 查询类
        Statement stmt = null;
        // 结果类
        ResultSet rs =null;
        // 数据库连接地址
        String url = "jdbc:mysql://rm-uf69u28mol7no0lm8ko.mysql.rds.aliyuncs.com:3306";
        // sql存储查询语句
        String sql = "";
        try {
            // 创建连接
            conn = DriverManager.getConnection(url,"super_user","popo123!");
            // 创建查询
            stmt = conn.createStatement();
            // 使用物流仓储管理系统数据库
            stmt.execute("use ProblemArchive");
            // sql查询语句，数据库已经存在一个test表，只有一个属性，就是存储姓名
            sql = "select * from test";
            // 获取查询结果保存至rs
            rs = stmt.executeQuery(sql);
            // 遍历查询结果
            while(rs.next()) {
                // 添加表的第一列到ar列表(表的列从1计数)
                ar.add(rs.getString(1));
            }
            conn.close();
            stmt.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ar;
    }
    @PostMapping("/api/HH")
    public String hh(){
        return "黄皓666";
    }
}
