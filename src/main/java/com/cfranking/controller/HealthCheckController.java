package com.cfranking.controller;

import com.cfranking.dao.UserDao;
import com.cfranking.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class HealthCheckController {

    Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    @Autowired
    UserDao userDao;

    @RequestMapping("/health/redis")
    public ResponseEntity redisHealthCheck() {

        User user = new User("abc", "Bangladesh", "ABC-ORG", "1010");
        User user2 = new User("cde", "India", "XYZ-ORG", "0101");

        logger.debug("operation: persisting user");

        userDao.persist(user);
        userDao.persist(user2);

        logger.debug("operation: selecting single user");
        logger.debug("user: " + userDao.getUser("abc"));

        logger.debug("operation: selecting  list");
        logger.debug("users: " + userDao.getUsers(Arrays.asList("abc", "cde")));

        logger.debug("operation: removing");

        userDao.delete("abc");
        userDao.delete("cde");

        return new ResponseEntity<>("redis healthy", HttpStatus.OK);
    }
}
