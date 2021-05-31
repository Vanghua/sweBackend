package util;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

public class JdbcUtilV2 {
    /**
     * 定义需要的变量
     */
    private static Connection connection = null;
    /**
     * 在大多情况下，我们使用的是PrepardStatement 来代替Statement
     * 这样可以防止sql注入
     */
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    /**
     * 连接数据库参数
     */
    private static String username = "";
    private static String password = "";
    private static String driver = "";
    private static String url = "";

    /**
     * 加载驱动，只需一次
     */
    public JdbcUtilV2(String dr, String ur,String user, String passwd) {
        try {
        	username = user;
        	password = passwd;
            driver = dr;
            url = ur;
            Class.forName(driver);
        } catch (Exception e) {
            System.err.println("连接失败，请检查连接参数");
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     *
     * @return 返回Connection
     */
    private static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
        	JOptionPane.showConfirmDialog(null , "获取连接失败,请检查网络是否连通", "系统消息", JOptionPane.CLOSED_OPTION);
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     *  查询表
     *  @return 返回查询结果的List
     */

    public ArrayList<HashMap<String, Object>> query(String sql, Object ...params){
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            setPreparedStatement(params);
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData(); // key name
            int columnCount = metaData.getColumnCount(); // column count
            
            ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            while(resultSet.next()) {
            	HashMap<String, Object> rowData = new HashMap<String, Object>();
            	for(int i = 1; i <= columnCount; ++i) {
            		rowData.put(metaData.getColumnName(i), resultSet.getObject(i));
            	}
            	list.add(rowData);
            }
            return list;
        }catch (Exception e){
            System.err.println("查询失败！");
            e.printStackTrace();
        } finally {
            close();
        }
        return null;
    }
    
    public boolean exists(String sql, Object ...params) {
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            setPreparedStatement(params);
            resultSet = preparedStatement.executeQuery();
            
            return resultSet.next();
        }catch (Exception e){
            System.err.println("查询失败！");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
    /**
     * 可处理insert/delete/update语句
     * @param sql sql语句
     * @param params 占位符参数数组
     * @return 返回bool值，表示是否成功
     */
    public boolean execute(String sql, Object ...params){
        try {
            //获取连接
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            //对占位符进行赋值
            setPreparedStatement(params);
            //提交sql
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e){
            System.err.println("表更新失败!");
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
    
    public void executeVoidProc(String sql, Object ...params){
        try {
            //获取连接
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            //对sql中的占位符进行赋值
            setPreparedStatement(params);
            
            preparedStatement.execute();
        }catch (Exception e){
            System.err.println("查询失败！");
            e.printStackTrace();
        } finally {
            close();
        }
    }
    /**
     * 对sql语句中的占位符进行赋值
     * @param params 参数值
     * @throws SQLException sql异常
     */
    private void setPreparedStatement(Object ... params) throws SQLException {
        if(params != null && params.length > 0){
            for(int i = 0; i < params.length; i++){
                if("null".equals(params[i])){
                    preparedStatement.setNull(i + 1, Types.NULL);
                }else{
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
        }
    }
    
 

    /**
     * 关闭资源的函数
     */
    private void close() {
        if(resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            resultSet = null;
        }
        if(preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            preparedStatement = null;
        }
        if(connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }

}

