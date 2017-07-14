package com.techprimers.elastic.load;

import com.techprimers.elastic.model.Users;
import com.techprimers.elastic.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component // this is @Bean
public class Loaders {

    // It's like MongoOperations!!! Spring Data rules!
    @Autowired
    ElasticsearchOperations operations;

    // we're gonna inject repo, which we created
    @Autowired
    UsersRepository usersRepository;

    // Due to @PostConstruct this method will be executed whenever this loaders be completed, so when Spring Boot app starts
    @PostConstruct
    @Transactional // let this method be transactional,  so if something fails, it can roll back!
    public void loadAll(){

        // this creates mapping for our model
        operations.putMapping(Users.class);
        System.out.println("Loading Data");
        
        // load the data into the repo (to ES!)
        usersRepository.save(getData());
        System.out.printf("Loading Completed");

    }

    private List<Users> getData() {
        
        // Let's get some temp data here
        List<Users> userses = new ArrayList<>();
        
        userses.add(new Users("Ajay", 123L, "Accounting", 12000L));
        userses.add(new Users("Jaga", 1234L, "Finance", 22000L));
        userses.add(new Users("Thiru", 1235L, "Accounting", 12000L));
        
        return userses;
    }
}
