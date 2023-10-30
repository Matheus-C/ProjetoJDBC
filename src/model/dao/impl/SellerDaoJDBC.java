package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {
    private final Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("INSERT INTO seller (Name, Email, BirthDate, " +
                    "BaseSalary, DepartmentId) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, Date.valueOf(seller.getBirthDate()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 0){
                rs = st.getGeneratedKeys();
                if(rs.next()){
                    seller.setId(rs.getInt(1));
                }
            }else{
                throw new DbException("Unexpected error!! No rows affected.");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("UPDATE seller SET Name=?, Email=?, BirthDate=?, " +
                    "BaseSalary=?, DepartmentId=? WHERE Id=?");
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, Date.valueOf(seller.getBirthDate()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());
            st.executeUpdate();
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void delete(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("delete from seller where Id=?");
            st.setInt(1, id);
            st.execute();
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
        PreparedStatement st = null;
        ResultSet rs = null;

        Map<Integer, Department> departmentMap = new HashMap<>();
        List<Seller> list = new ArrayList<>();
        try {
            st = conn.prepareStatement("SELECT seller.*, department.Name as DepName " +
                    "FROM seller INNER JOIN department on seller.departmentId = department.id ORDER BY Name");
            rs = st.executeQuery();
            while(rs.next()){
                Department dep = departmentMap.get(rs.getInt("DepartmentId"));
                if(dep == null){
                    dep = instantateDepartment(rs);
                    departmentMap.put(rs.getInt("DepartmentId"), dep);
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
