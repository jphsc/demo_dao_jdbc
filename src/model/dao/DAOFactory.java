package model.dao;

import model.dao.impl.SellerDAOJDBC;

public class DAOFactory {

	public static ISellerDAO createSellerDao() {
		
		return new SellerDAOJDBC();
	}
}
