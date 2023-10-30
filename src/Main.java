import db.DB;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDate;
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
        for(Seller s: sellers){
            System.out.println(s);
        }
        System.out.println("===test 3 Insert seller===");
        seller = new Seller();
        seller.setBirthDate(LocalDate.now());
        seller.setDepartment(dep);
        seller.setEmail("aaa@aaa.com");
        seller.setName("aaa");
        seller.setBaseSalary(3000.0);
        sellerdao.insert(seller);
        System.out.println("===INSERTED===");

        System.out.println("===test 4 Update===");
        seller = new Seller();
        seller.setId(7);
        seller.setBirthDate(LocalDate.now());
        seller.setDepartment(dep);
        seller.setEmail("aaabbb@aaa.com");
        seller.setName("aaabbb");
        seller.setBaseSalary(3000.0);
        sellerdao.update(seller);

        System.out.println("===test 5 Find all===");
        sellerdao.delete(8);
        sellers = sellerdao.findAll();
        System.out.println("===test 6 Find all===");
        for(Seller s: sellers){
            System.out.println(s);
        }
        DB.closeConnection();


        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        System.out.println("===test 1 Find by id===");
        Department department = departmentDao.findById(3);
        System.out.println(department);

       System.out.println("===test 2 Insert department===");
        department = new Department();
       department.setName("aaa");
       departmentDao.insert(department);
        System.out.println("===INSERTED===");

        System.out.println("===test 3 Update===");
        department = new Department();
        department.setId(1);
        department.setName("aaabbb");
        departmentDao.update(department);

        System.out.println("===test 4 delete===");
        departmentDao.delete(5);
        List<Department> departments = departmentDao.findAll();
        System.out.println("===test 5 Find all===");
        for(Department s: departments){
            System.out.println(s);
        }
        DB.closeConnection();
    }

}