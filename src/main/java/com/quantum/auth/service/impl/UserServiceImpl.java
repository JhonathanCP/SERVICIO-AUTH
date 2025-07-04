package com.quantum.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantum.auth.model.Dependency;
import com.quantum.auth.model.Role;
import com.quantum.auth.model.User;
import com.quantum.auth.repo.IDependencyRepo;
import com.quantum.auth.repo.IGenericRepo;
import com.quantum.auth.repo.IRoleRepo;
import com.quantum.auth.repo.IUserRepo;
import com.quantum.auth.service.IUserService;

@Service
public class UserServiceImpl extends CRUDImpl<User, Integer> implements IUserService {

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private IRoleRepo roleRepo;

    @Autowired
    private IDependencyRepo dependencyRepo;

    @Override
    protected IGenericRepo<User, Integer> getRepo() {
        return userRepo;
    }

    @Override
    public User findOneByUsername(String username) {
        return userRepo.findOneByUsername(username);
    }

    @Override
    public Role findRoleById(Integer id) {
        return roleRepo.findById(id).orElse(null);
    }

    @Override
    public Dependency findDependencyById(Integer id) {
        return dependencyRepo.findById(id).orElse(null);
    }
    
}