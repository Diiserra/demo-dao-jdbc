package aplication;

import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Program {

    public static void main(String[] args) {

        Department obj = new Department(1, "Libros");

        Seller seller = new Seller(21, "Diego", "diego", new Date(), 200.00, obj);

        System.out.println(seller);
    }
}
