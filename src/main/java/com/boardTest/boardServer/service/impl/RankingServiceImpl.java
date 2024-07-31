package com.boardTest.boardServer.service.impl;


import com.boardTest.boardServer.service.RankingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Log4j2
public class RankingServiceImpl implements RankingService {
    private static final String LEADERBOARD_KEY = "leaderBoard";
    @Autowired
    StringRedisTemplate redisTemplate;

    //data set
    //range search
    //user raking search

    public boolean setUserScore(String userId, int score){
        ZSetOperations zSetOps = redisTemplate.opsForZSet(); // sorted set
        zSetOps.add(LEADERBOARD_KEY, userId, score);

        return true;
    }

    public Long getUserRanking(String userId){
        ZSetOperations zSetOps = redisTemplate.opsForZSet(); // sorted set
        Long rank = zSetOps.rank(LEADERBOARD_KEY, userId);

        return rank;
    }

    public List<String> getTopRank(int limit){
        ZSetOperations zSetOps = redisTemplate.opsForZSet(); // sorted set
        Set<String> rangeSet = zSetOps.reverseRange(LEADERBOARD_KEY, 0, limit -1);

        return new ArrayList<>(rangeSet); // 스트링셋->어레이리스트로 변환
    }
}
