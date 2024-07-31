package com.boardTest.boardServer.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {

    @Cacheable(cacheNames = "userNameCache") // , key = "Id"
    public String getUserName(String Id) {
        // 외부서비스나 디비호출
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
        }
        System.out.println("name api ");
    ;  if(Id.equals("A")) {
            return "ADM";
        }
        if(Id.equals("B")) {
            return "BBB";
        }
         return "";
    }

    @Cacheable(cacheNames = "userAgeCache") // , key = "Id"
    public int getUserAge(String Id) {
        // 외부서비스나 디비호출
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
        }

        System.out.println("age api ");
    ;  if(Id.equals("A")) {
            return 28;
        }
        if(Id.equals("B")) {
            return 33;
        }
        return 0;
    }
}
