package model.dao;

import db.DB;
import model.dao.impl.SellerDAOJDBC;

public class DAOFactory {

	public static ISellerDAO createSellerDao() {
		
		return new SellerDAOJDBC(DB.getConnecttion());
	}
}
