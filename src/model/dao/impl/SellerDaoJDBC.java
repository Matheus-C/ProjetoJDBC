package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {
    private final Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("select seller.*, department.Name as DepName " +
                    "from seller inner join department on seller.departmentId where " +
                    "seller.id=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if(rs.next()){
                Department dep = new Department();
                dep.setId(rs.getInt("departmentId"));
                dep.setName(rs.getString("DepName"));
                Seller seller = new Seller();
                seller.setId(rs.getInt("Id"));
                seller.setName(rs.getString("Name"));
                seller.setEmail(rs.getString("Email"));
                seller.setBaseSalary(rs.getDouble("BaseSalary"));
                seller.setDepartment(dep);
                seller.setBirthDate(rs.getDate("BirthDate").toLocalDate());
                return seller;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(stmt);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
