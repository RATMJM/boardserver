package com.boardTest.boardServer;

import com.boardTest.boardServer.service.RankService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class RankTest {
    @Autowired
    private RankService rankService;

    @Test
    void getRanks(){
        rankService.getTopRank(1);
        Instant before = Instant.now();
        Long userRank = rankService.getUserRanking("user_100");
        Duration elapsed = Duration.between(before,Instant.now());

        System.out.println(String.format("Rank(%d) - took %d ms ", userRank, elapsed.getNano() / 1000000));

        before = Instant.now();
        List<String> topRankers= rankService.getTopRank(10);
        elapsed =  Duration.between(before, Instant.now());

        System.out.println(String.format("Rank(%d) - took %d ms ", elapsed.getNano() / 1000000));
    }


    @Test
    void inMemorySortPerformance(){
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0; i<1000000; i++){
            int score= (int)(Math.random()*1000000); //0~999999
            list.add(score);
        }

        Instant before = Instant.now();
        Collections.sort(list);// nlogn
        Duration elapsed = Duration.between(before,Instant.now());
        System.out.println((elapsed.getNano()/ 1000000) + "ms");
    }

    @Test
    void insertScore(){
        for(int i=0; i<1000000; i++){
            int score= (int)(Math.random()*1000000); //0~999999
            String userId = "user_"+i;

            rankService.setUserScore(userId, score);
        }
    }
}
