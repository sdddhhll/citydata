package com.dhl.citydata;

import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class CityData6 {
	public static void main(String[] args) throws Exception {
		InputStream stream = ClassLoader.getSystemResourceAsStream("db.properties");
		Properties properties = new Properties();
		properties.load(stream);
		DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = dataSource.getConnection();
		String sql = " select * from idaas_organ t where t.cant_type='10' ";
		ResultSetHandler<List<Map<String, Object>>> rsh = new MapListHandler();
		List<Map<String,Object>> cantList = queryRunner.query(connection, sql, rsh);
		for (int i = 0; i < cantList.size(); i++) {
			String sql2 = " select * from idaas_organ t where t.cant_type='20' and t.super_code='"+cantList.get(i).get("cant_id")+"' ";
			ResultSetHandler<List<Map<String, Object>>> rsh2 = new MapListHandler();
			List<Map<String,Object>> cantList2 = queryRunner.query(connection, sql2, rsh2);
			for (int j=0;j< cantList2.size();j++) {
				String sql4 = "INSERT INTO `uc_data_permission`(`dp_id`, `dp_name`, `dp_type`, `parent_id`, `dp_stru_path`) VALUES ('ct_"+cantList2.get(j).get("org_code")+"','"+cantList2.get(j).get("cant_name")+"', 'ct20', 'ct_"+cantList.get(i).get("org_code")+"', '#root#cn#ct_"+cantList.get(i).get("org_code")+"#ct_"+cantList2.get(j).get("org_code")+"');";
				System.out.println(sql4);
			}
		}
	}
}
