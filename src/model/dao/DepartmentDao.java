package model.dao;

import java.util.List;

public interface DepartmentDao {

    void insert(DepartmentDao obj);
    void update(DepartmentDao obj);
    void deleteById(Integer id);
    DepartmentDao findById(Integer id);
    List<DepartmentDao> findAll();
}
