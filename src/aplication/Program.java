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

        Seller seller = sellerDao.findById(2);
        System.out.println(seller);

        List<Seller> sellerList = sellerDao.findByDepartment(seller.getDepartment());

        sellerList.forEach(System.out::println);

    }
}
