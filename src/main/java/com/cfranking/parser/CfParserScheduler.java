package com.cfranking.parser;

import com.cfranking.client.CfClient;
import com.cfranking.model.CfContest;
import com.cfranking.model.CfContestList;
import com.cfranking.model.CfRanklistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class CfParserScheduler {

    @Autowired
    CfClient cfClient;

    // TODO : Change the rate later
    @Scheduled(fixedRate = 2000)
    public void parseStandings() throws InterruptedException, ExecutionException {
        List<Integer> contestIdList = getActiveContest();
        if (CollectionUtils.isEmpty(contestIdList)) {
            return;
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Callable<CfRanklistResponse>> tasks = contestIdList.stream()
                .map(id -> getRunnableTask(id))
                .collect(Collectors.toList());
        // TODO : Collect results and insert into redis
        List<Future<CfRanklistResponse>> rankLists = executorService.invokeAll(tasks);

        // shutdown executor service
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    private Callable<CfRanklistResponse> getRunnableTask(int contestId) {

        return () -> cfClient.getContestResults(contestId);
    }

    /**
     * @return
     */
    private List<Integer> getActiveContest() {
        CfContestList contestList = cfClient.getContestList();
        // TODO : convert and find active list
        return Arrays.asList(1065);
    }
}
