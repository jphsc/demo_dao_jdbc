package application;

import java.util.Date;

import model.dao.DAOFactory;
import model.dao.ISellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		Department obj = new Department(1, "Books");
		System.out.println(obj);
		
		Seller newSeller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.00, obj);
		
		ISellerDAO sellerDao = DAOFactory.createSellerDao();
		sellerDao.insertSeller(newSeller);
		
		Seller sellerFound = new Seller();
		sellerFound = sellerDao.findSellerById(1);
		
		System.out.println(sellerFound);
		
	}

}
