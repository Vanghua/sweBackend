package normal;

import java.sql.*;

public class conSql {
    public static void connectTest() {
        System.out.println("以下是数据库的输出");
        // 连接类
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
                // 输出查询结果的第一列(列数从1计数)
                System.out.println(rs.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
