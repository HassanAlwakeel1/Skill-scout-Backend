package com.skillscout.Repository;

import com.skillscout.model.entity.Application;
import com.skillscout.model.entity.Job;
import com.skillscout.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long > {
    List<Application> findByUser(User user);
    List<Application> findByJob(Job job);

}
