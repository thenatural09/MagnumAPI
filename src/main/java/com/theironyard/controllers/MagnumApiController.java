package com.theironyard.controllers;

import com.theironyard.entities.TomALike;
import com.theironyard.entities.User;
import com.theironyard.services.TomALikeRepository;
import com.theironyard.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * Created by Troy on 11/9/16.
 */
@RestController
public class MagnumApiController {
    @Autowired
    UserRepository users;

    @Autowired
    TomALikeRepository toms;

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

    @RequestMapping(path="/tomalikes",method = RequestMethod.GET)
    public Iterable<TomALike> getTomalikes() {
        return toms.findAll();
    }

    @RequestMapping(path="tomalikes",method = RequestMethod.POST)
    public void addTomalike(HttpSession session, String comment, MultipartFile photo, HttpServletResponse response) throws Exception {
        String name = (String) session.getAttribute("name");
        if (name == null) {
            throw new Exception("Not logged in");
        }
        User user = users.findFirstByName(name);
        File dir = new File("public");
        dir.mkdirs();

        File photoFile = File.createTempFile("photo",photo.getOriginalFilename(), dir);
        FileOutputStream fos = new FileOutputStream(photoFile);
        fos.write(photo.getBytes());

        TomALike tom = new TomALike(photoFile.getName(),comment,0,user);
        toms.save(tom);
        response.sendRedirect("/#/tomalikes");
    }


    @RequestMapping (path = "favs", method = RequestMethod.POST)
    public void addFav(HttpSession session, @RequestBody HashMap fav) throws Exception {
        String name = (String) session.getAttribute("name");
        if (name == null) {
            throw new Exception("Not logged in");
        }
        Integer id = (Integer) fav.get("tomId");
        Boolean looksLikeTom = (Boolean) fav.get("looksLikeTom");
        if (id == null || looksLikeTom == null) {
            throw new Exception("Invalid request body");
        }
        TomALike tom = toms.findOne(id);
        tom.setVotes(tom.getVotes() + (looksLikeTom ? 1 : -1));
        toms.save(tom);
    }

}
