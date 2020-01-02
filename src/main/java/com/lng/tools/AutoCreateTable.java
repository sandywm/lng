package com.lng.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;

public class AutoCreateTable {

	@Autowired
	private ContantsProperties cp;
	
	public void createTable() throws ClassNotFoundException, SQLException {
		//连接数据库
		Class.forName(cp.getDriver());
		String url = cp.getUrl() + "&useSSL=false";
		Connection conn = DriverManager.getConnection(url, cp.getUsername(), cp.getPassword());
		Statement stat = conn.createStatement();
		String tableName = "lng_price_detail_"+CurrentTime.getSimpleTime();
		//获取数据库表名
		ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);
		// 判断表是否存在，如果存在则什么都不做，否则创建表
		if( rs.next() ){
			return;
		}else {
			stat.executeUpdate("CREATE TABLE "+tableName+"("
					+ "id varchar(100) PRIMARY KEY NOT NULL COMMENT 'id',"
					+ "price int(11) NOT NULL COMMENT '价格',"
					+ "price_time varchar(50) NOT NULL COMMENT '时间',"
					+ "remark varchar(100) NULL COMMENT '备注',"
					+ "add_time varchar(50) NOT NULL COMMENT '添加时间',"
					+ "gf_id varchar(100) NOT NULL COMMENT '液厂编号',"
					+ "constraint fk foreign key (gf_id) references gas_factory(id)"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
		}
		// 释放资源
		stat.close();
		conn.close();
	}
}
