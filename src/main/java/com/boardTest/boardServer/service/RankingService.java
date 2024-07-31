package com.boardTest.boardServer.service;

import java.util.List;

public interface RankingService {
    boolean setUserScore(String userId, int score);
    Long getUserRanking(String userId);

    List<String> getTopRank(int limit);

}
