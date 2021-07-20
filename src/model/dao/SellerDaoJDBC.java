package model.dao;

import dbServices.DB;
import dbServices.DbExeception;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements  SellerDao{

    private Connection conn = null;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT seller.*, department.name as depName FROM seller INNER JOIN department ON seller.departmentId = department.id WHERE seller.id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()){
                Department department = instancienceDepartment(rs);
                Seller seller = instancienceSeller(rs, department);
                return seller;
            }
            return null;
        }catch (SQLException e){
            throw new DbExeception(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    private Seller instancienceSeller(ResultSet rs, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("id"));
        seller.setName(rs.getString("name"));
        seller.setEmail(rs.getString("email"));
        seller.setBirthDate(rs.getDate("birthDate"));
        seller.setBaseSalaty(rs.getDouble("baseSalary"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instancienceDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("departmentId"));
        department.setName(rs.getString("depName"));
        return department;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT seller.*, department.name AS depName\n" +
                    "FROM seller INNER JOIN department\n" +
                    "ON seller.departmentid = department.id\n" +
                    "WHERE seller.departmentid = ? ORDER BY seller.name";
            st = conn.prepareStatement(sql);
            st.setInt(1, department.getId());
            rs = st.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()){

                Department depart = map.get(rs.getInt("departmentId"));

                if (depart == null){
                    depart = instancienceDepartment(rs);
                    map.put(rs.getInt("departmentId"), depart);
                }

                Seller seller = instancienceSeller(rs, depart);
                sellerList.add(seller);
            }
            return sellerList;
        }catch (SQLException e){
            throw new DbExeception(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }
}
