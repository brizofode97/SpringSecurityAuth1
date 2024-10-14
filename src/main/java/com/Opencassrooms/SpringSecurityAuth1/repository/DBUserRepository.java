package com.Opencassrooms.SpringSecurityAuth1.repository;

import com.Opencassrooms.SpringSecurityAuth1.model.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBUserRepository extends JpaRepository<DBUser, Long> {
    public DBUser findByUsername(String username);
}
