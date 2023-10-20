package com.specificgroup.crud_app.dao;

import com.specificgroup.crud_app.dao.impl.specification.TutorSpecification;
import com.specificgroup.crud_app.dto.CreateRequest;
import com.specificgroup.crud_app.dto.UpdateRequest;
import com.specificgroup.crud_app.entity.Tutor;
import java.util.List;

public interface TutorDao {
    Long create(CreateRequest request);
    List<Tutor> getBySpecification(TutorSpecification specification);
    List<Tutor> get();
    Long update(UpdateRequest request, Long contactDetailsId);
    boolean deleteByContactDetailId(Long contactDetailsId);
    Long getContactDetailsIdByTutorId(Long id);
}
