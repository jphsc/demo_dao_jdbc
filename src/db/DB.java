package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	private static Connection conn;
	
	private static Properties loadProperties() {
		try {
			FileInputStream fs = new FileInputStream("db.properties");
			Properties props = new Properties();
			props.load(fs);
			return props;
		} catch(IOException e) {
			throw new DbException("Erro ao ler o arquivo de configuração do banco: "+e.getMessage());
		}
	}
	
	public static Connection getConnecttion() {
		if (conn == null) {
			try{
				Class.forName("com.mysql.cj.jdbc.Driver");
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				return conn = DriverManager.getConnection(url, props);
			} catch(SQLException e) {
				throw new DbException("Não foi possível obter as configurações, erro: "+e.getMessage());
			} catch (Exception e) {
				throw new DbException("Erro exception: "+e);
			}
		}
		return conn;
	}
	
	public static void closeConnection() {
		if (conn != null) {
			try{
				conn.close();
			} catch(SQLException e) {
				throw new DbException("Não foi possível fechar a conexão, erro: "+e.getMessage());
			}
		}
	}
	
	public static void closeStatement(Statement st) {
		if(st != null) {
			try {
				st.close();
			} catch(Exception ex) {
				throw new DbException(ex.getMessage());
			}
		}
	}
	
	public static void closePreparedStatement(PreparedStatement ps) {
		if(ps != null) {
			try {
				ps.close();
			} catch(Exception ex) {
				throw new DbException(ex.getMessage());
			}
		}
	}
	
	public static void closeResultset(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch(Exception ex) {
				throw new DbException(ex.getMessage());
			}
		}
	}
}
