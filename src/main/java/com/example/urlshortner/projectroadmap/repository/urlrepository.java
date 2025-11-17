package com.example.urlshortner.projectroadmap.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.urlshortner.projectroadmap.entity.urlshortner;

public interface urlrepository extends MongoRepository<urlshortner, String> {
    

}
