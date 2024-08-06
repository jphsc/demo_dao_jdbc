package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDAO {

void insert(Seller obj);
	
	void update(Seller obj);
	
	void deleteById(Integer id);
	
	void findById(Integer id);
	
	List<Seller> findAll();
}
