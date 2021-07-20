package aplication;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {

    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();



        Seller seller1 = new Seller(null, "Greg", "greg@hotmail", new Date(), 4000.0, new Department(1,null));
        sellerDao.insert(seller1);
    }
}
