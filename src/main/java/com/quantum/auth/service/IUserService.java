package com.quantum.auth.service;

import com.quantum.auth.model.Dependency;
import com.quantum.auth.model.Role;
import com.quantum.auth.model.User;

public interface IUserService extends ICRUD<User, Integer>{

    User findOneByUsername(String username);

    Role findRoleById(Integer id);

    Dependency findDependencyById(Integer id);

}
