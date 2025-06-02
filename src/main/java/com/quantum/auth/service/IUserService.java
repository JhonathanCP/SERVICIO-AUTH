package com.quantum.auth.service;

import com.quantum.auth.model.User;

public interface IUserService extends ICRUD<User, Integer>{

    User findOneByUsername(String username);

}
