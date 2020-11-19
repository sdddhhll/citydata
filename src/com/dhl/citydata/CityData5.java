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

public class CityData5 {
	public static void main(String[] args) throws Exception {
		InputStream stream = ClassLoader.getSystemResourceAsStream("db.properties");
		Properties properties = new Properties();
		properties.load(stream);
		DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = dataSource.getConnection();
		String sql = " select * from test.pub_cants t ";
		ResultSetHandler<List<Map<String, Object>>> rsh = new MapListHandler();
		List<Map<String,Object>> cantList = queryRunner.query(connection, sql, rsh);
		for (int i = 0; i < cantList.size(); i++) {
			String sql3 = "INSERT INTO `test`.`pub_cant`(`CANT_ID`, `CANT_NAME`, `SHORT_NAME`, `CANT_TYPE`, `SUPER_CODE`, `ORG_CODE`, `COUNTRY_CODE`, `CANT_NOTE`) VALUES ('"+cantList.get(i).get("cant_id")+"', '"+cantList.get(i).get("cant_name")+"', '"+cantList.get(i).get("cant_name")+"', 'N', '1', '-1', 'CN', '')";
			
		}
	}
}
