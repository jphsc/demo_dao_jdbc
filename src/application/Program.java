package application;

import java.sql.Date;

import model.dao.DAOFactory;
import model.dao.ISellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		Department obj = new Department(1, "Books");
		//System.out.println(obj);

		//Seller newSeller = new Seller(24, "Henry", "rafahsc@live.com", new Date(0), 7500.00, obj);
		Seller newSeller = new Seller();
		
		ISellerDAO sellerDao = DAOFactory.createSellerDao();
		//sellerDao.insertSeller(newSeller);
		
//		Seller sellerFound = new Seller();
//		sellerFound = sellerDao.findSellerById(1);
//		
//		System.out.println(sellerFound);
		
		System.out.println();
		
//		List<Seller> sellersDepComputers = sellerDao.findSellerByDepartment(1);
//		
//		for(Seller seller: sellersDepComputers) {
//			System.out.println(seller);
//		}
		
//		List<Seller> allSellers = sellerDao.findAllSeller();
//		
//		for(Seller seller: allSellers) {
//			System.out.println(seller);
//		}
		
		//sellerDao.insertSeller(newSeller);
		
		//sellerDao.deleteSellerById(22);
		newSeller = sellerDao.findSellerById(24);
		newSeller.setName("Henry");
		newSeller.setEmail("rafahsc@live.com");
		newSeller.setBaseSalary(7500.00);
		newSeller.setBirthDate(new java.sql.Date(0));
		sellerDao.updateSeller(newSeller);
		
	}

}
