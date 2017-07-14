package com.techprimers.elastic.repository;

import com.techprimers.elastic.model.Users;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

// Every repository should extend ElasticsearchRepository
// - we will be taken Users
// - primary key will be Long
public interface UsersRepository extends ElasticsearchRepository<Users, Long> {
    
    List<Users> findByName(String text);
    List<Users> findBySalary(Long salary);
}
// AND WE DON'T NEED ANY IMPLEMENTATION !!! Spring Data Provides it!!!
 