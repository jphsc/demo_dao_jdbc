package model.dao;

import java.util.List;

import model.entities.Seller;

public interface ISellerDAO {

	void insertSeller(Seller obj);
	
	void updateSeller(Seller obj);
	
	void deleteSellerById(Integer id);
	
	Seller findSellerById(Integer id);
	
	List<Seller> findAll();
}
