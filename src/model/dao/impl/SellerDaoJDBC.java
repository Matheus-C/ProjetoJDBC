package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
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
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("delete from seller where Id=?");
            st.setInt(1, id);
            st.executeQuery();
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
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
                Department dep = instantateDepartment(rs);
                return instantateSeller(rs, dep);
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

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        Department dep = null;
        List<Seller> list = new ArrayList<>();
        try {
            st = conn.prepareStatement("SELECT seller.*, department.Name as DepName " +
                    "FROM seller INNER JOIN department on seller.departmentId = department.id WHERE departmentId = ? ORDER BY Name");
            st.setInt(1, department.getId());
            rs = st.executeQuery();
            while(rs.next()){
                if(dep == null){
                    dep = instantateDepartment(rs);
                }
                list.add(instantateSeller(rs, dep));
            }
            return list;
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Department instantateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
            dep.setId(rs.getInt("departmentId"));
            dep.setName(rs.getString("DepName"));
            return dep;

    }

    private Seller instantateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
            seller.setId(rs.getInt("Id"));
            seller.setName(rs.getString("Name"));
            seller.setEmail(rs.getString("Email"));
            seller.setBaseSalary(rs.getDouble("BaseSalary"));
            seller.setDepartment(dep);
            seller.setBirthDate(rs.getDate("BirthDate").toLocalDate());
            return seller;

    }
}
