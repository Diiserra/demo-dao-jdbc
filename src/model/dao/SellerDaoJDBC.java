package model.dao;

import dbServices.DB;
import dbServices.DbExeception;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
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
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String idGenereter = "SELECT AUTOINCREMENT.nextval id FROM dual";
            st = conn.prepareStatement(idGenereter);
            rs = st.executeQuery();
            if (rs.next()){
                int idSeller = rs.getInt("id");
                obj.setId(idSeller);
                String sql = "INSERT INTO seller (id, name, email, birthDate, baseSalary, departmentId) VALUES (?, ?, ?, ?, ?, ?)";
                st = conn.prepareStatement(sql);
                st.setInt(1, obj.getId());
                st.setString(2, obj.getName());
                st.setString(3, obj.getEmail());
                st.setDate(4, new Date(obj.getBirthDate().getTime()));
                st.setDouble(5, obj.getBaseSalaty());
                st.setInt(6, obj.getDepartment().getId());
                int rows = st.executeUpdate();
                System.out.println(rows);
            }else {
                throw new DbExeception("Error, não foi possivel inserir");
            }
        }catch (SQLException e){
            throw new DbExeception(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE seller SET name = ?, email = ?, birthDate = ?, baseSalary = ?, departmentId = ? WHERE id = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalaty());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            int rows = st.executeUpdate();
            System.out.println(rows);
        }catch (SQLException e){
            throw new DbExeception(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            String sql = "DELETE FROM seller WHERE id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, id);

            int rows = st.executeUpdate();

            if (rows <= 0){
                throw new DbExeception("Id informado não existe");
            }
        }catch (SQLException e){
            throw new DbExeception(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
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
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT seller.*, department.name AS depName\n" +
                    "FROM seller INNER JOIN department\n" +
                    "ON seller.departmentid = department.id\n" +
                    "ORDER BY seller.name";
            st = conn.prepareStatement(sql);
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
