package ru.spbau.mit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;
import java.util.function.Function;

public class ThreadPoolImpl implements ThreadPool {

    private final Queue<Runnable> queue = new LinkedList<>();
    private final List<Thread> threads = new ArrayList<>();
    private volatile boolean isShutdown = false;

    public ThreadPoolImpl(int n) {
        for (int i = 0; i < n; i++) {
            Worker thread = new Worker(queue);
            thread.start();
            threads.add(thread);
        }
    }

    @Override
    public <R> LightFuture<R> submit(Supplier<R> supplier) {
        if (isShutdown) {
            return null;
        }

        final LightFutureImpl<R> future = new LightFutureImpl<>(this);

        synchronized (queue) {
            queue.add(() -> {
                try {
                    future.setResult(supplier.get());
                } catch (Exception e) {
                    future.setException(new LightExecutionException(e));
                }
            });

            queue.notify();
        }

        return future;
    }

    @Override
    public void shutdown() {
        this.isShutdown = true;

        for (Thread thread : threads) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) { }
        }
    }

    private void enqueue(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    private class Worker extends Thread {
        private final Queue<Runnable> queue;

        Worker(Queue<Runnable> queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                while (!isInterrupted()) {
                    Runnable task;

                    synchronized (queue) {
                        while (queue.isEmpty()) {
                            queue.wait();
                        }
                        task = queue.poll();
                    }
                    if (task != null) {
                        task.run();
                    }
                }
            } catch (InterruptedException e) { }
        }
    }

    private class LightFutureImpl<R> implements LightFuture<R> {

        private final ThreadPoolImpl threadPool;
        private final Queue<Runnable> blocked = new LinkedList<>();
        private R result;
        private LightExecutionException exception;

        LightFutureImpl(ThreadPoolImpl threadPool) {
            this.threadPool = threadPool;
        }

        @Override
        public synchronized boolean isReady() {
            return result != null || exception != null;
        }

        @Override
        public synchronized R get() throws LightExecutionException, InterruptedException {
            while (!isReady()) {
                wait();
            }

            if (exception != null) {
                throw exception;
            }

            return result;
        }

        @Override
        public synchronized <U> LightFuture<U> thenApply(Function<? super R, ? extends U> function) {
            final LightFutureImpl<U> future = new LightFutureImpl<>(threadPool);
            final Runnable task = () -> {
                try {
                    final R result = get();
                    future.setResult(function.apply(result));
                } catch (Exception e) {
                    future.setException(new LightExecutionException(e));
                }
            };

            if (!isReady()) {
                blocked.add(task);
            } else {
                threadPool.enqueue(task);
            }

            return future;
        }

        public synchronized void setResult(R result) {
            this.result = result;
            notify();
            runBlocked();
        }

        public synchronized void setException(LightExecutionException e) {
            exception = e;
            notify();
            runBlocked();
        }

        private void runBlocked() {
            synchronized (blocked) {
                blocked.forEach(threadPool::enqueue);
                blocked.clear();
            }
        }
    }

}
