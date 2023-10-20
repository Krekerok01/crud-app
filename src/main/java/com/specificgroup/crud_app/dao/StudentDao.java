package com.specificgroup.crud_app.dao;

import com.specificgroup.crud_app.dao.impl.specification.StudentSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Student;
import java.util.List;

public interface StudentDao {
    Long create(CreateRequest request);
    List<Student> getBySpecification(StudentSpecification specification);
    List<Student> get();
    Long update(UpdateRequest request, Long contactDetailsId);
    boolean deleteByContactDetailId(Long contactDetailsId);
    Long getContactDetailsIdByStudentId(Long id);
}
