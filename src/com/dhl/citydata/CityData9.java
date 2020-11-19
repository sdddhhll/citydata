package com.dhl.citydata;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.io.IOUtils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class CityData9 {
	public static void main(String[] args) throws Exception {
		InputStream stream = ClassLoader.getSystemResourceAsStream("db.properties");
		Properties properties = new Properties();
		properties.load(stream);
		DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = dataSource.getConnection();
		String sql = "SELECT u.inv_id,u.user_name,u.user_dept,u.user_phone,u.user_email,u.user_province,u.user_city,u.user_position,u.user_ask FROM inv_question_userinfo1 u  WHERE  u.inv_allow='ty'";
		
		ResultSetHandler<List<Map<String, Object>>> rsh = new MapListHandler();
		List<Map<String,Object>> userList = queryRunner.query(connection, sql, rsh);
		
		for(int i=0;i<userList.size();i++) {
			Map<String,Object> uMap = userList.get(i);
			for(int j=1;j<=6;j++) {
				sql = "select CONCAT(o.inv_prefix,o.inv_options) ans,o.inv_options_type,a.inv_answer_text FROM inv_question_options1 o,inv_question_answer1 a where o.inv_question_id = a.inv_question_id AND o.inv_id = a.inv_options_id and a.inv_user_id = '"+userList.get(i).get("inv_id")+"' AND a.inv_question_id = '"+j+"'";
				ResultSetHandler<List<Map<String, Object>>> qrsh = new MapListHandler();
				List<Map<String,Object>> qList = queryRunner.query(connection, sql, qrsh);
				String answ = "";
				for(int k=0;k<qList.size();k++) {
					Map<String,Object> kmap = qList.get(k);
					if (k == qList.size()-1) {
						
						if(kmap.get("inv_options_type").equals("1")) {
							answ = answ + kmap.get("ans") +"["+ kmap.get("inv_answer_text") +"]";
						}else {
							answ = answ + kmap.get("ans");
						}
					}else {
						if(kmap.get("inv_options_type").equals("1")) {
							answ = answ + answ +"["+ kmap.get("inv_answer_text") +"]" +"；";
						}else {
							answ = answ + kmap.get("ans") +"；";
						}
					}
					
				}
				uMap.put(j+"", answ);	
			}
			
			//System.out.println(uMap);
		}
		
		StringBuffer sb = new StringBuffer();
		Writer writer = new FileWriter("F:\\var\\ty.txt");
		for(int i=0;i<userList.size();i++) {
			Map<String,Object> uMap = userList.get(i);
			sb.append(uMap.get("user_name"));
			sb.append("\t");
			sb.append(uMap.get("user_dept"));
			sb.append("\t");
			sb.append(uMap.get("user_phone"));
			sb.append("\t");
			sb.append(uMap.get("user_email"));
			sb.append("\t");
			sb.append(uMap.get("user_province"));
			sb.append("\t");
			sb.append(uMap.get("user_city"));
			sb.append("\t");
			sb.append(uMap.get("user_position"));
			sb.append("\t");
			sb.append(uMap.get("user_ask"));
			sb.append("\t");
			sb.append(uMap.get("1"));
			sb.append("\t");
			sb.append(uMap.get("2"));
			sb.append("\t");
			sb.append(uMap.get("3"));
			sb.append("\t");
			sb.append(uMap.get("4"));
			sb.append("\t");
			sb.append(uMap.get("5"));
			sb.append("\t");
			sb.append(uMap.get("6"));
			sb.append("\r\n");
		}
		
		IOUtils.write(sb, writer);
		writer.flush();
		IOUtils.closeQuietly(writer);
		System.out.println("完成");
	}
}
