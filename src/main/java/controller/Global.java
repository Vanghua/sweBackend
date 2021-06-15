package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import util.JdbcUtilV2;
import util.MailUtil;
import util.SMSUtil;

public class Global {
	// database configuration
	public static String databaseDriver = "com.mysql.cj.jdbc.Driver";
	public static String databaseUrl = "jdbc:mysql://rm-uf69u28mol7no0lm8ko.mysql.rds.aliyuncs.com:3306/soft";
	public static String databaseUser = "soft_admin";
	public static String databasePasswd = "soft_admin";
	public static JdbcUtilV2 ju = new JdbcUtilV2(databaseDriver, databaseUrl, databaseUser, databasePasswd);
	
	// mail configuration
	public static String mailFrom = "2416116991@qq.com";
	public static String mailPassword = "bgzbueqduyqzdjgh";
	public static MailUtil mu = new MailUtil(mailFrom, mailPassword);	
	
	// account type dictionary
	public static HashMap<String, String> ChineseType = new HashMap<String, String>();
	static {
		ChineseType.put("all", "全局管理员");
		ChineseType.put("user", "普通用户");
		ChineseType.put("trans", "运输员");
		ChineseType.put("assign", "调度管理员");
		ChineseType.put("order", "订单管理员");
		ChineseType.put("people", "人事管理员");
		ChineseType.put("financial", "财务管理员");
		ChineseType.put("warehouse", "仓库管理员");
	}

	// good priority dictionary
	public static HashMap<String, Integer> goodPriorityDict = new HashMap<>();
	static{
		goodPriorityDict.put("日用品", 1);
		goodPriorityDict.put("食品", 2);
		goodPriorityDict.put("文件", 2);
		goodPriorityDict.put("衣物", 1);
		goodPriorityDict.put("数码产品", 3);
		goodPriorityDict.put("贵重物品", 5);
		goodPriorityDict.put("其它", 3);
	}
	
	// SMS configuration
	/* demo
	 *  目前这个接口只能用于收到快递的提示，具体请询问 【朱胤璘】
	 */
	public static String Uid = "popo1234";
	public static String SMSkey = "d41d8cd98f00b204e980";
	public static SMSUtil su = new SMSUtil(Uid, SMSkey);
	
	// Lng Lat -> Distance
	public static final double EARTH_RADIUS = 6371393; // 平均半径,单位m 
	
    public static double getDistance(Double lat1,Double lng1,Double lat2,Double lng2) {
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        double radiansAX = Math.toRadians(lng1); // A经弧度
        double radiansAY = Math.toRadians(lat1); // A纬弧度
        double radiansBX = Math.toRadians(lng2); // B经弧度
        double radiansBY = Math.toRadians(lat2); // B纬弧度

        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
            + Math.sin(radiansAY) * Math.sin(radiansBY);
        double acos = Math.acos(cos); // 反余弦值

        return EARTH_RADIUS * acos; // 最终结果
    }
    
    
    // dijkstra
    public static String AssignLevel3Route(String FromL3Address, String ToL3Address){

    	// 获取所有三级仓库--樊华修改
    	ArrayList<HashMap<String, Object>> totalWarehouse = 
    			Global.ju.query("select warehouse_address as address, " +
						" cast(warehouse_lng as double) as lng, " +
						" cast(warehouse_lat as double) as lat " +
						" from warehouse where warehouse_type = 3");
		
    	
    	String route = "";
    	
    	int num = totalWarehouse.size();
    	Double[][] adjMatrix = new Double[num][num];
    	for(int i = 0; i < num; ++i) {
    		// 之前打的是i<num--樊华修改
    		for(int j = 0; j < num; ++j) {
    			HashMap<String, Object> a = totalWarehouse.get(i);
    			HashMap<String, Object> b = totalWarehouse.get(j);
    			
    			adjMatrix[i][j] = adjMatrix[j][i] = getDistance((Double)a.get("lat"), (Double)a.get("lng"), 
    					(Double)b.get("lat"), (Double)b.get("lng"));
    			if(i == j)
					adjMatrix[i][j] = Double.MAX_VALUE;
    		}
    	}
    	
    	Double [] result = new Double [adjMatrix.length];
        boolean[] used = new boolean[adjMatrix.length];  
        int [] pre = new int[adjMatrix.length];
        Stack<Integer> path = new Stack<>();
        
        // 修改get中的字段内容，原来为address，现在改为warehouse_address--樊华修改
        int source = 0, dest = 0;
        for(int i = 0; i < num; ++i) {
        	if(FromL3Address.equals((String) totalWarehouse.get(i).get("warehouse_address"))) {
        		source = i;
        	}
        	if(ToL3Address.equals((String) totalWarehouse.get(i).get("warehouse_address"))) {
        		dest = i;
        	}
        }
        
        used[source] = true;  
        for(int i = 0;i < adjMatrix.length;i++) {
            result[i] = adjMatrix[source][i];
            used[i] = false;
        }
    
        
        // 下面的三个循环原来都是从1开始，现在改为从0开始--樊华修改
        for(int i = 0;i < adjMatrix.length;i++) {
            Double min = Double.MAX_VALUE;    
            int k = 0;
            for(int j = 0;j < adjMatrix.length;j++) {
                if(!used[j] && min > result[j]) {
                    min = result[j];
                    k = j;
                }
            }
            // 之前的代码没有让始发三级仓库进入前缀数组--樊华修改
            if(i == 1)
            	pre[k] = source;
            used[k] = true; 
            if(k == dest) break;
            
            for(int j = 1;j < adjMatrix.length;j++) {  
                if(!used[j]) { 
                    if(result[j] > min + adjMatrix[k][j]) {
                        result[j] = min + adjMatrix[k][j];
                         pre[j] = k;
                    }
                }
            }

        }

        path.add(dest);
        int cur = dest;
        while(cur != source) {
        	path.add(pre[cur]);
        	cur = pre[cur];
        }
        
        boolean first = true;
        while(!path.isEmpty()) {
        	if(first) first = false; else route += "|";
        	// 把get中的字段address改为warehouse_address
        	route += (String) totalWarehouse.get(path.pop()).get("warehouse_address");
        }
        return route;
    }
}
