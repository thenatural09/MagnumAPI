package com.theironyard.controllers;

import com.theironyard.entities.User;
import com.theironyard.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by Troy on 11/9/16.
 */
@RestController
public class MagnumApiController {
    @Autowired
    UserRepository users;

    @RequestMapping(path = "/users",method = RequestMethod.POST)
    public User addUser(HttpSession session,@RequestBody User user) throws Exception {
        User userFromDb = users.findFirstByName(user.getName());
        if(userFromDb == null) {
            users.save(user);
        }
        else if (!user.getPassword().equals(userFromDb.getPassword())) {
            throw new Exception("Wrong password!");
        }
        session.setAttribute("name",user.getName());
        return user;
    }
}
