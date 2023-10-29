import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Arrays;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        SellerDao sellerdao = DaoFactory.createSellerDao();
        System.out.println("===test 1 Find by id===");
        Seller seller = sellerdao.findById(3);
        System.out.println(seller);
        System.out.println("===test 2 Find by department===");
        Department dep = new Department(2, "aaaa");
        List<Seller> sellers = sellerdao.findByDepartment(dep);
        System.out.println(Arrays.toString(sellers.toArray()));
        DB.closeConnection();
    }
}