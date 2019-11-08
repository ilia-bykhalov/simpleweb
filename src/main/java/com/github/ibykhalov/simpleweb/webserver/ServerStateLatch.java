package com.github.ibykhalov.simpleweb.webserver;

import com.github.ibykhalov.simpleweb.exception.ServerStateException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;


public final class ServerStateLatch {
    private final ReentrantLock latchAccessLock = new ReentrantLock();
    private volatile CountDownLatch latch = new CountDownLatch(0);

    public boolean tryStartServer() {
        try {
            latchAccessLock.lock();
            if (latch.getCount() == State.STOPPED.getValue()) {

                latch = new CountDownLatch(State.RUNNING.getValue());
                return true;

            } else {
                return false;
            }
        } finally {
            latchAccessLock.unlock();
        }
    }

    public void beginAndAwaitShutDown() {
        try {
            latchAccessLock.lock();
            if (latch.getCount() == State.RUNNING.getValue()) {
                latch.countDown();//TO SHUTTING_DOWN_IN_PROCESS
            }
        } finally {
            latchAccessLock.unlock();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void completeShutDown() {
        try {
            latchAccessLock.lock();
            if (latch.getCount() != State.SHUTTING_DOWN_IN_PROCESS.getValue()) {
                throw new ServerStateException("Try complete shut down when state is " + latch.getCount());
            } else {
                latch.countDown();//TO STOPPED
            }
        } finally {
            latchAccessLock.unlock();
        }
    }

    public boolean isRunning() {
        return latch.getCount() == State.RUNNING.getValue();
    }


    private enum State {
        STOPPED(0),
        SHUTTING_DOWN_IN_PROCESS(1),
        RUNNING(2);

        private final int value;

        State(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
