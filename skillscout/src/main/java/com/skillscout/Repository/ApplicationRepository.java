package com.skillscout.Repository;

import com.skillscout.model.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application,Long > {
}
