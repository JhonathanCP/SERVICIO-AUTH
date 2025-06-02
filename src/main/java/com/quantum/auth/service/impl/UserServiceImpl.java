package com.quantum.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantum.auth.model.User;
import com.quantum.auth.repo.IGenericRepo;
import com.quantum.auth.repo.IUserRepo;
import com.quantum.auth.service.IUserService;

@Service
public class UserServiceImpl extends CRUDImpl<User, Integer> implements IUserService {

    @Autowired
    private IUserRepo userRepo;

    @Override
    protected IGenericRepo<User, Integer> getRepo() {
        return userRepo;
    }

    @Override
    public User findOneByUsername(String username) {
        return userRepo.findOneByUsername(username);
    }
    
}