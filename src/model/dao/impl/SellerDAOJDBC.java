package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.ISellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class SellerDAOJDBC implements ISellerDAO{

	private Connection conn;
	
	public SellerDAOJDBC(Connection conn) {
		this.conn = conn;
	}
	
	public void insertSeller(Seller obj) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("INSERT INTO seller (Name, Email, Birthdate, BaseSalary, DepartmentId) "
					+ "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			
			int rows = ps.executeUpdate();
			
			if(rows > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				
				if(rs.next()) {
					obj.setId(rs.getInt(1));
					System.out.println(String.format("Registros alterados: %d - Id gerado: %d", rows, rs.getInt(1)));
				} else {
					throw new DbException("Erro inesperado, nenhuma linha foi afetada");
				}
				
				DB.closeResultset(rs);
			}

		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closePreparedStatement(ps);
		}
	}

	public void updateSeller(Seller obj) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ? ");
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			ps.setInt(6, obj.getId());
			int res = ps.executeUpdate();
			
			if(res > 0) {
				System.out.println("Número de linhas afetadas: "+res);
			} else {
				throw new DbException("Erro inesperado, nenhum registro foi alterado!");
			}
			
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closePreparedStatement(ps);
		}
	}

	@Override // *********** REVER ESSE ***********
	public void deleteSellerById(Integer id) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("DELETE FROM seller where id = ?");
			ps.setInt(1, id);
			
			int res = ps.executeUpdate();
			
			if(res > 0) {
				System.out.println(String.format("Numero de linhas afetadas: %d", res));
			} else {
				throw new DbException("Erro inesperado, nenhuma linha foi afetada");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Seller findSellerById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs =  null;
		
		try {
			ps = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
					+ "FROM seller JOIN department ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				Department department = instantiateDepartment(rs);
				Seller seller = instantiateSeller(rs, department);
				
				return seller;
			}
			return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closePreparedStatement(ps);
			DB.closeResultset(rs);
		}
	}

	@Override
	public List<Seller> findAllSeller() {
		Statement st = null;
		ResultSet rs =  null;
		
		try {
			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> departments = new HashMap<Integer, Department>();
			
			st = conn.createStatement();
			rs = st.executeQuery("SELECT seller.*, department.Name as DepName FROM seller JOIN department ON DepartmentId = department.Id");
			
			while(rs.next()) {
				Department department = departments.get(rs.getInt("DepartmentId"));
				
				if(department == null) {
					department = instantiateDepartment(rs);
					departments.put(rs.getInt("DepartmentId"), department);
				}
				
				Seller seller = instantiateSeller(rs, department);
				sellers.add(seller);
			}
			return sellers;
			
		} catch (Exception e) {
			e.printStackTrace();
		}return null;
	}

	@Override
	public List<Seller> findSellerByDepartment(Integer departmentId) {
		PreparedStatement ps = null;
		ResultSet rs =  null;
		List<Seller> sellers = new ArrayList<Seller>();
		Map<Integer, Department> map = new HashMap<Integer, Department>();
		
		try {
			ps = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
					+ "FROM seller JOIN department ON seller.DepartmentId = department.Id "
					+ "WHERE department.Id = ? "
					+ "ORDER BY seller.Name");
			ps.setInt(1, departmentId);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				
				Department department = map.get(rs.getInt("DepartmentId"));
				
				if(department == null) {
					department = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), department);
				}
				
				Seller seller = instantiateSeller(rs, department);
				sellers.add(seller);
			}
			
			return sellers;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			DB.closePreparedStatement(ps);
			DB.closeResultset(rs);
		}
		
		return null;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department department = new Department();
		department.setId(rs.getInt("DepartmentId"));
		department.setName(rs.getString("DepName"));
		return department;
	}

	private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setDepartment(department);
		
		return seller;
	}

}
