package com.cmeiyuan.concurrency.sample;

import com.cmeiyuan.concurrency.annotation.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cmy on 2018/6/3
 */
@ThreadSafe
@Slf4j
public class CountSample2 {

    private static final int clientTotal = 5000;
    private static final int threadTotal = 50;
    private static AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        Semaphore semaphore = new Semaphore(threadTotal);

        for (int i = 0; i < clientTotal; i++) {
            executorService.submit(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        countDownLatch.await();

        executorService.shutdown();

        log.info("count={}", count.get());

    }

    private static void add() {
        count.getAndIncrement();
        count.incrementAndGet();
    }

}
