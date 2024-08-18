package com.mindhub.todolist;


import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.models.enums.UserRoles;
import com.mindhub.todolist.respositories.UserEntityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Utils {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(UserEntityRepository userEntityRepository){
        return args->{
            UserEntity user1 = new UserEntity("User 1",passwordEncoder.encode("020202020"),"demo@demo.com",UserRoles.ADMIN);
            userEntityRepository.save(user1);
            UserEntity user2 = new UserEntity("User 2",passwordEncoder.encode("0101010101"),"demo1@demo.com",UserRoles.USER);
            userEntityRepository.save(user2);
            UserEntity user3 = new UserEntity("User 3",passwordEncoder.encode("0303030303"),"demo2@demo.com",UserRoles.COORDINATOR);
            userEntityRepository.save(user3);
        };
    }
}
