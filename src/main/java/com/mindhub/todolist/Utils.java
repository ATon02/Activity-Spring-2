package com.mindhub.todolist;


import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.respositories.UserEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Utils {
    @Bean
    public CommandLineRunner initData(UserEntityRepository userEntityRepository){
        return args->{
            UserEntity user1 = new UserEntity("User 1","020202020","demo@demo.com");
            userEntityRepository.save(user1);
            UserEntity user2 = new UserEntity("User 2","0101010101","demo1@demo.com");
            userEntityRepository.save(user2);
        };
    }
}
