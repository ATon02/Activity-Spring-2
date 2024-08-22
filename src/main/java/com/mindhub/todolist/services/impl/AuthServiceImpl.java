package com.mindhub.todolist.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mindhub.todolist.configurationsjwt.JwtUtils;
import com.mindhub.todolist.dtos.LoginUser;
import com.mindhub.todolist.exceptions.InvalidFormatException;
import com.mindhub.todolist.exceptions.InvalidObject;
import com.mindhub.todolist.exceptions.NotCreateException;
import com.mindhub.todolist.exceptions.NotFoundException;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.respositories.UserEntityRepository;
import com.mindhub.todolist.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginUser loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity user = userEntityRepository.findByEmail(authentication.getName()).orElseThrow(()->new NotFoundException("Not Found User With Email: " + authentication.getName()));
        String jwt = jwtUtils.generateClaims(authentication.getName(),user.getId());
        return jwt;
    }

    @Override
    public String register(LoginUser loginRequest) {
        try {
            if (!loginRequest.validObject()) {
                throw new InvalidObject("Check data contain empty fields");
            }
            if (userEntityRepository.existsByEmail(loginRequest.email())) {
                throw new DataIntegrityViolationException("Bad request: Email is duplicated");
            }
            if (!loginRequest.validEmail()) {
                throw new InvalidFormatException("The email format is not valid");
            }
            UserEntity user = new UserEntity(loginRequest.password(),loginRequest.email());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userEntityRepository.save(user);
            return this.login(loginRequest);
        } catch (DataIntegrityViolationException dex) {
            throw new DataIntegrityViolationException("Bad request: Email is duplicated");
        } catch (InvalidObject iex) {
            throw new InvalidObject("Check data contain empty fields");
        } catch (InvalidFormatException fex){
            throw new InvalidFormatException("The email format is not valid");
        } catch (Exception ex) {
            throw new NotCreateException("Bad request: UserEntity Not Created");
        } 
    }
    
}
