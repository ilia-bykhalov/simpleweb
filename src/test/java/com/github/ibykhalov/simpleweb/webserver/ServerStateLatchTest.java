package com.github.ibykhalov.simpleweb.webserver;

import com.github.ibykhalov.simpleweb.exception.ServerStateException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServerStateLatchTest {
    @Test
    public void isRunning_Should_ReturnFalse_WhenStateIsSHUT_DOWN_ByDefault() {
        ServerStateLatch latch = new ServerStateLatch();
        assertFalse(latch.isRunning());
    }

    @Test
    public void completeShutDown_Should_ThrowException_WhenStateIsSHUT_DOWN() {
        ServerStateLatch latch = new ServerStateLatch();

        try {
            latch.completeShutDown();
            fail();
        } catch (ServerStateException ignored) {
        }
    }

    @Test
    public void awaitShutDown_Should_Bypass_WhenStateIsSHUT_DOWN() {
        ServerStateLatch latch = new ServerStateLatch();

        latch.beginAndAwaitShutDown();
    }

    @Test
    public void tryStartServer_should_StartServer_WhenStateIsSHUT_DOWN() {
        ServerStateLatch latch = new ServerStateLatch();

        assertTrue(latch.tryStartServer());
    }

    //---- RUNNING

    @Test
    public void tryStartServer_Should_NotStart_WhenStateIsRUNNING() {
        ServerStateLatch latch = new ServerStateLatch();

        assertTrue(latch.tryStartServer());
        assertFalse(latch.tryStartServer());
    }

    @Test
    public void isRunning_Should_ReturnTrue_WhenStateIsRUNNING() {
        ServerStateLatch latch = new ServerStateLatch();

        latch.tryStartServer();
        assertTrue(latch.isRunning());
    }

    @Test
    public void completeShutDown_Should_ThrowException_WhenStateIsRUNNING() {
        ServerStateLatch latch = new ServerStateLatch();

        latch.tryStartServer();

        try {
            latch.completeShutDown();
            fail();
        } catch (ServerStateException ignored) {
        }
    }

    @Test
    public void awaitShutDown_Should_Begins_WhenStateIsRUNNING() throws Exception {
        ServerStateLatch latch = new ServerStateLatch();

        latch.tryStartServer();
        assertTrue(latch.isRunning());

        new Thread(latch::beginAndAwaitShutDown).start();
        Thread.sleep(100);
        assertFalse(latch.isRunning());
    }

    //---- SHUTTING DOWN

    @Test
    public void isRunning_Should_ReturnFalse_WhenStateIsSHUTTING_DOWN_IN_PROCESS() throws Exception {
        ServerStateLatch latch = new ServerStateLatch();

        latch.tryStartServer();
        new Thread(latch::beginAndAwaitShutDown).start();
        Thread.sleep(100);
        assertFalse(latch.isRunning());

    }

    @Test
    public void tryStartServer_Should_NotStart_WhenStateIsSHUTTING_DOWN_IN_PROCESS() throws Exception {
        ServerStateLatch latch = new ServerStateLatch();

        latch.tryStartServer();
        new Thread(latch::beginAndAwaitShutDown).start();
        Thread.sleep(100);
        assertFalse(latch.isRunning());

        assertFalse(latch.tryStartServer());
    }

    @Test
    public void awaitShutDown_Should_Await_WhenStateIsSHUTTING_DOWN_IN_PROCESS() throws Exception {
        ServerStateLatch latch = new ServerStateLatch();

        latch.tryStartServer();
        Thread awaitingShutDownThread1 = new Thread(latch::beginAndAwaitShutDown);
        Thread awaitingShutDownThread2 = new Thread(latch::beginAndAwaitShutDown);
        awaitingShutDownThread1.start();
        awaitingShutDownThread2.start();
        Thread.sleep(100);
        assertFalse(latch.isRunning());

        assertTrue(awaitingShutDownThread1.isAlive());
        assertTrue(awaitingShutDownThread2.isAlive());

        latch.completeShutDown();
        awaitingShutDownThread1.join();
        awaitingShutDownThread1.join();
    }

    @Test
    public void completeShutDown_Should_Complete_WhenStateIsSHUTTING_DOWN_IN_PROCESS() throws Exception {
        ServerStateLatch latch = new ServerStateLatch();

        latch.tryStartServer();
        Thread awaitingShutDownThread = new Thread(latch::beginAndAwaitShutDown);
        awaitingShutDownThread.start();
        Thread.sleep(100);
        assertFalse(latch.isRunning());

        latch.completeShutDown();
        assertFalse(latch.isRunning());
        awaitingShutDownThread.join();
    }

    //-------------

    @Test
    public void canStartAfterStop() throws InterruptedException {
        ServerStateLatch latch = new ServerStateLatch();

        assertTrue(latch.tryStartServer());

        Thread awaitingShutDownThread = new Thread(latch::beginAndAwaitShutDown);
        awaitingShutDownThread.start();
        Thread.sleep(100);
        latch.completeShutDown();
        awaitingShutDownThread.join();

        assertTrue(latch.tryStartServer());
    }
}
