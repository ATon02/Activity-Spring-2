package com.mindhub.todolist.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.mindhub.todolist.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mindhub.todolist.dtos.DTOUserEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.respositories.UserEntityRepository;
import com.mindhub.todolist.services.UserEntityService;

@Service
public class UserEntityServiceImpl implements UserEntityService {
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public DTOUserEntity create(UserEntity userEntity) {
        try {
            if (userEntityRepository.existsByEmail(userEntity.getEmail())) {
                throw new DataIntegrityViolationException("Bad request: Email is duplicated");
            }
            if (!userEntity.validObject()) {
                throw new InvalidObject("Check data contain empty fields");
            }
            if (!userEntity.validEmail()) {
                throw new InvalidFormatException("The email format is not valid");
            }
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            UserEntity userEntitySave = userEntityRepository.save(userEntity);
            return new DTOUserEntity(userEntitySave);
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

    @Override
    public DTOUserEntity fetch(String id) {
        try {
            long UserEntityId = Long.parseLong(id);
            if (UserEntityId > 0) {
                UserEntity UserEntityFetch = userEntityRepository.findById(UserEntityId).orElseThrow(()->new NotFoundException("Not Found UserEntity With Id: " + id));
                return new DTOUserEntity(UserEntityFetch);
            }
            throw new InvalidFormatException("Bad request: Invalid ID format, id is lower or equals to 0");
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Bad request: Invalid ID format, id is not a number");
        }
    }

    @Override
    public DTOUserEntity update(String id, UserEntity userEntity)  {
        try {
            long userEntityId = Long.parseLong(id);
            if (userEntityId > 0) {
                UserEntity userEntityOld = userEntityRepository.findById(userEntityId).orElseThrow(() -> new NotFoundException("Not Found UserEntity With Id: " + id));
                if (userEntityOld.equals(userEntity)) {
                    throw new NotUpdateException("No Fields Were Updated For UserEntity With Id: " + id);
                }
                if (!userEntity.validObject()) {
                    throw new InvalidObject("Check data contain empty fields");
                }
                if (!userEntity.validEmail()) {
                    throw new InvalidFormatException("The email format is not valid");
                }
                userEntityOld.setUsername(valueToSave(userEntity.getUsername(),userEntityOld.getUsername()));
                userEntityOld.setPassword(passwordEncoder.encode(valueToSave(userEntity.getPassword(),userEntityOld.getPassword())));
                if (userEntity.getEmail() != null && !userEntity.getEmail().equals(userEntityOld.getEmail())) {
                    if(userEntityRepository.existsByEmail(userEntity.getEmail())){
                        throw new DataIntegrityViolationException("Bad request: Email is duplicated");
                    }
                    userEntityOld.setEmail(userEntity.getEmail());
                }
                UserEntity userEntityUpdate = userEntityRepository.save(userEntityOld);
                return new DTOUserEntity(userEntityUpdate);
            } else {
                throw new InvalidFormatException("Bad request: Invalid ID format, id is lower or equals to 0");
            }
        } catch (NumberFormatException ex) {
            throw new InvalidFormatException("Bad request: Invalid ID format, id is not a number");
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Bad request: Email is duplicated");
        }catch (InvalidObject ex) {
            throw new InvalidObject("Check data contain empty fields");
        }catch (InvalidFormatException fex){
            throw new InvalidFormatException("The email format is not valid");
        }
    }

    @Override
    public void delete(String id) {
        try {
            long userEntityId = Long.parseLong(id);
            if (userEntityId > 0) {
                userEntityRepository.deleteById(userEntityId);
            }else{
                throw new InvalidFormatException("Bad request: Invalid ID format, id is lower or equals to 0");
            }
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Bad request: Invalid ID format, id is not a number");
        } catch(DataIntegrityViolationException dex){
            throw new DataIntegrityViolationException("Bad request: User with id: " + id + " is related to tasks");
        }
    }

    @Override
    public List<DTOUserEntity> fetchAll() {
        try {
            List<UserEntity> users =  userEntityRepository.findAll();
            List<DTOUserEntity> DtoUsers = new ArrayList<>();
            for(UserEntity userEntity : users){
                DtoUsers.add(new DTOUserEntity(userEntity));
            }
            return DtoUsers;
        } catch (Exception ex) {
            throw new NumberFormatException("Not Found");
        }
    }

    // Method return the value to save, comparing the new and old value
    private String valueToSave(String newValue, String oldValue){
        return (newValue != null && !newValue.equals(oldValue)) ? newValue : oldValue;
    }

    @Override
    public DTOUserEntity fetchByEmail(String email) {
        try {
            UserEntity UserEntityFetch = userEntityRepository.findByEmail(email).orElseThrow(()->new NotFoundException("Not Found UserEntity With Email: " + email));
            return new DTOUserEntity(UserEntityFetch);
        } catch (Exception ex) {
            throw new NumberFormatException("Bad request: Unsuccessful fetch");
        }
    }

    @Override
    public DTOUserEntity updateSelf(long id, UserEntity userEntity) {
        try {
            long userEntityId = Long.parseLong(String.valueOf(id));
            if (userEntityId > 0) {
                UserEntity userEntityOld = userEntityRepository.findById(userEntityId).orElseThrow(() -> new NotFoundException("Not Found UserEntity With Id: " + id));
                if (userEntityOld.equals(userEntity)) {
                    throw new NotUpdateException("No Fields Were Updated For UserEntity With Id: " + id);
                }
                if (!userEntity.validObject()) {
                    throw new InvalidObject("Check data contain empty fields");
                }
                if (!userEntity.validEmail()) {
                    throw new InvalidFormatException("The email format is not valid");
                }
                userEntityOld.setUsername(valueToSave(userEntity.getUsername(),userEntityOld.getUsername()));
                userEntityOld.setPassword(passwordEncoder.encode(valueToSave(userEntity.getPassword(),userEntityOld.getPassword())));
                if (userEntity.getEmail() != null && !userEntity.getEmail().equals(userEntityOld.getEmail())) {
                    if(userEntityRepository.existsByEmail(userEntity.getEmail())){
                        throw new DataIntegrityViolationException("Bad request: Email is duplicated");
                    }
                    userEntityOld.setEmail(userEntity.getEmail());
                }
                UserEntity userEntityUpdate = userEntityRepository.save(userEntityOld);
                return new DTOUserEntity(userEntityUpdate);
            } else {
                throw new InvalidFormatException("Bad request: Invalid ID format, id is lower or equals to 0");
            }
        } catch (NumberFormatException ex) {
            throw new InvalidFormatException("Bad request: Invalid ID format, id is not a number");
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Bad request: Email is duplicated");
        }catch (InvalidObject ex) {
            throw new InvalidObject("Check data contain empty fields");
        }catch (InvalidFormatException fex){
            throw new InvalidFormatException("The email format is not valid");
        }
    }
}
