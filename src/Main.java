import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        Department obj = new Department(1, "Books");
        Seller seller = new Seller(1, "Bob", "bob@gmail.com", LocalDate.now(), 3000.0, obj);
        System.out.println(seller);
    }
}