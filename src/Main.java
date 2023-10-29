import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        SellerDao sellerdao = DaoFactory.createSellerDao();
        Department obj = new Department(1, "Books");
        Seller seller = new Seller(1, "Bob", "bob@gmail.com", LocalDate.now(), 3000.0, obj);
        System.out.println(seller);
    }
}