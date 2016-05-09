package ru.spbau.mit;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class ThreadPoolImplTest {
    private static final int NUMBER_OF_THREADS = 10;
    private static final Function<Integer, Integer> INC = number -> number + 1;
    private static final Supplier<Integer> THROWS_EXCEPTION = () -> {
        throw new RuntimeException();
    };
    private ThreadPool pool;

    @Before
    public void setUp() {
        pool = new ThreadPoolImpl(NUMBER_OF_THREADS);
    }

    @Test
    public void testSubmitSimple() throws InterruptedException, LightExecutionException {
        LightFuture future = pool.submit(() -> true);
        assertTrue((boolean) future.get());
    }

    @Test
    public void testSubmit() throws Exception {

        final Integer[] counter = {0};

        final Supplier<Integer> simple = () -> {
            synchronized (counter) {
                counter[0] += 1;
                return 1;
            }
        };

        final Queue<LightFuture<Integer>> results = new LinkedList<>();
        for (int i = 0; i < 100; ++i) {
            results.add(pool.submit(simple));
        }

        int finalResult = 0;
        while (!results.isEmpty()) {
            LightFuture<Integer> result = results.poll();
            finalResult += result.get();
        }

        pool.shutdown();

        assertEquals(100, finalResult);
        assertEquals(100, (int) counter[0]);
    }

    @Test
    public void testThenApply() throws LightExecutionException, InterruptedException {
        int result = pool.submit(() -> 1).thenApply(INC).thenApply(INC).get();
        pool.shutdown();
        assertEquals(3, result);
    }

    @Test
    public void testShutdown() throws Exception {
        LightFuture<String> good = pool.submit(() -> "Good");
        pool.shutdown();
        LightFuture<String> bad = pool.submit(() -> "Bad");

        assertTrue(good.isReady());
        assertTrue(bad == null);
    }

    @Test(expected = LightExecutionException.class)
    public void testException() throws LightExecutionException, InterruptedException {
        pool.submit(THROWS_EXCEPTION).get();
        pool.shutdown();
    }

    @Test
    public void testIsReady() throws InterruptedException, LightExecutionException {

        LightFuture<Integer> future = pool.submit(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 3;
        });

        assertFalse(future.isReady());
        assertEquals(3, future.get().intValue());
    }

    @Test
    public void testNumberOfThreads() throws InterruptedException {
        final Barrier barrier = new Barrier(10);

        final int[] result = {0};

        for (int i = 0; i < 9; i++) {
            pool.submit(() -> {
                try {
                    barrier.await();
                } catch (InterruptedException ignored) {
                }

                result[0]++;
                return 0;
            });
        }

        barrier.await();
        pool.shutdown();

        assertEquals(9, result[0]);
    }

    private final class Barrier {

        private final int parties;
        private int lack;

        Barrier(int parties) {
            this.parties = parties;
            this.lack = parties;
        }

        public synchronized void await() throws InterruptedException {
            lack -= 1;

            if (lack == 0) {
                notifyAll();
            }

            while (lack != 0) {
                wait();
            }
        }
    }
}








