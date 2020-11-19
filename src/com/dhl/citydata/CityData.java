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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CityData {
	public static void main(String[] args) throws Exception {
		JSONArray priArray = new JSONArray();
		InputStream stream = ClassLoader.getSystemResourceAsStream("db.properties");
		Properties properties = new Properties();
		properties.load(stream);
		DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
		QueryRunner queryRunner = new QueryRunner();
		Connection connection = dataSource.getConnection();
		ResultSetHandler<List<Map<String, Object>>> rsh = new MapListHandler();
		List<Map<String, Object>> list = queryRunner.query(connection, "SELECT t.id,t.area_name FROM dt_area.dt_area t WHERE t.area_parent_id = '0' ORDER BY t.id ASC", rsh);
		System.out.println("省："+list);
		for(Map<String, Object> map : list ) {
			JSONArray cityArray = new JSONArray();
			JSONObject priObject = new JSONObject();
			String pid = map.get("id").toString();
			priObject.put("Code", pid);
			priObject.put("Name", map.get("area_name"));
			
			
			ResultSetHandler<List<Map<String,Object>>> cityRsh = new MapListHandler();
			List<Map<String, Object>> cityList = queryRunner.query(connection, "SELECT t.id,t.area_name FROM dt_area.dt_area t WHERE t.area_parent_id = '"+pid+"' ORDER BY t.id ASC", cityRsh);
			System.out.println("城市："+cityList);
			
			
			for(int i=0;i<cityList.size();i++) {
				JSONObject cityobj = new JSONObject();
				String areaPid = cityList.get(i).get("id").toString();
				cityobj.put("Code", areaPid);
				cityobj.put("sort", i+1);
				cityobj.put("Name", cityList.get(i).get("area_name"));
				cityArray.add(cityobj);
				ResultSetHandler<List<Map<String,Object>>> areaRsh = new MapListHandler();
				List<Map<String, Object>> areaList = queryRunner.query(connection, "SELECT t.id,t.area_name FROM dt_area.dt_area t WHERE t.area_parent_id = '"+areaPid+"' ORDER BY t.id ASC", areaRsh);
				System.out.println("区县："+areaList);
				JSONArray areaArray = new JSONArray();
				for(int j=0;j<areaList.size();j++) {
					JSONObject areaobj = new JSONObject();
					Object areaId = areaList.get(j).get("id");
					Object areaName = areaList.get(j).get("area_name");
					areaobj.put("Code", areaId);
					areaobj.put("Name", areaName);
					areaobj.put("sort", j+1);
					areaArray.add(areaobj);
				}
				cityobj.put("level", areaArray);
			}
			
			if(cityArray.isEmpty()) {
				JSONObject cityobj = new JSONObject();
				cityobj.put("Code", pid);
				cityobj.put("sort", 1);
				cityobj.put("Name", map.get("area_name"));
				JSONObject areaobj = new JSONObject();
				areaobj.put("Code", pid);
				areaobj.put("Name",  map.get("area_name"));
				areaobj.put("sort", 1);
				JSONArray areaArray = new JSONArray();
				areaArray.add(areaobj);
				cityobj.put("level",areaArray);
				cityArray.add(cityobj);
			}
			
			priObject.put("level", cityArray);
			priArray.add(priObject);
		}
		System.out.println(priArray.toJSONString());
	}
}
