package com.tech.basics.multithreading.service;

import com.tech.basics.multithreading.model.User;
import com.tech.basics.multithreading.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // inject Repository

    Logger logger = LoggerFactory.getLogger(UserService.class);

    //We will read of CSV file and make it as object to persistent DB

    @Async //AsyncCall
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws  Exception{
        long startTime = System.currentTimeMillis();
        List<User> users = parseCSV(file);
        logger.info("saving of user of size {}"+users.size(),""+Thread.currentThread().getName());
        users = userRepository.saveAll(users);
        long endTime = System.currentTimeMillis();
        logger.info("Total Time {}"+(endTime-startTime));
        return CompletableFuture.completedFuture(users);
    }


    //Fetch list of user fromDB
    @Async
    public CompletableFuture<List<User>> findAllUsers(){
        logger.info("get list of users by "+Thread.currentThread().getName());
        List<User> users = userRepository.findAll();
        return CompletableFuture.completedFuture(users);

    }
    private List<User> parseCSV(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try{
            try(final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
                String line;
                while((line=br.readLine())!=null){
                    final String[] data =  line.split(",");
                    final User user = new User(data[0],data[1],data[2]);
                    //logger.info("user"+user.toString());
                    users.add(user);
                }
                return users;
            }
        }catch (final IOException e){
            logger.error("Failed to parse csv file",e);
            throw new Exception("Failed to parse CSV file ",e);
        }

    }







}
