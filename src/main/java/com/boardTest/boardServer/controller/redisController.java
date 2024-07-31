package com.boardTest.boardServer.controller;

import com.boardTest.boardServer.service.RankService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("redis")
@Log4j2
public class redisController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RankService rankingService;

    @GetMapping("/setScore")
    public boolean setScore(@RequestParam("userId") String userId,
                            @RequestParam("score") int score){
//        return "ID1: " + userId  +score;
        return rankingService.setUserScore(userId,score);
    }

    @GetMapping("/getRank")
    public Long getUserRank(@RequestParam("userId") String userId){
        return rankingService.getUserRanking(userId);
    }

    @GetMapping("/getTopRanks")
    public List<String> getTopRanks(){
        return rankingService.getTopRank(3);
    }


    @GetMapping("/user")
    public String redis(@RequestParam("id") String id){

        return "ID1: " + id  ;
    }

    @GetMapping("/setFruit")
    public String setFruit(@RequestParam("name") String name){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("fruit", name);

        return "saved.";
    }

    @GetMapping("/getFruit")
    public String getFruit(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String fruitName = ops.get("fruit");

        return fruitName;
    }

}
