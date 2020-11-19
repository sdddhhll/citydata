package com.dhl.citydata;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CityData7 {
	public static void main(String[] args) throws Exception {
		InputStream stream = ClassLoader.getSystemResourceAsStream("db.properties");
		Properties properties = new Properties();
		properties.load(stream);
		DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = dataSource.getConnection();
		String sql = " select * from test.pub_cant p ";
		ResultSetHandler<List<Map<String, Object>>> rsh = new MapListHandler();
		List<Map<String,Object>> cantList = queryRunner.query(connection, sql, rsh);
		for (int i = 0; i < cantList.size(); i++) {
			String sql2 = " select * from test.pub_cants b where b.cant_id='"+cantList.get(i).get("cant_id")+"'";
			ResultSetHandler<List<Map<String, Object>>> rsh2 = new MapListHandler();
			List<Map<String,Object>> cantList2 = queryRunner.query(connection, sql2, rsh2);
			if(cantList2.size()>0) {
//				String sql3 = "update test.pub_cant s set s.cant_name='"+cantList2.get(0).get("cant_name")+"' where s.cant_id='"+cantList.get(i).get("cant_id")+"'";
//				System.out.println(sql3);
//				queryRunner.update(connection,sql3);
				
				String sql4 = "update test.pub_cant s set s.short_name='"+cantList2.get(0).get("cant_name")+"' where s.cant_id='"+cantList.get(i).get("cant_id")+"'";
				System.out.println(sql4);
				queryRunner.update(connection,sql4);
			}
		}
	}
}
