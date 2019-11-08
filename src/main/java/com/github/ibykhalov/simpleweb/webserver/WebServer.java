package com.github.ibykhalov.simpleweb.webserver;

import com.github.ibykhalov.simpleweb.exception.ServerStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public final class WebServer implements IWebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    private static final int SOCKET_TIMEOUT = 1000;

    private final int port;
    private final int workersCount;
    private final IRequestHandler requestHandler;

    private final ReentrantLock startStopLock = new ReentrantLock();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicReference<Thread> socketAccepterThread = new AtomicReference<>();

    public WebServer(int port, int workersCount, IRequestHandler requestHandler) {
        this.port = port;
        this.workersCount = workersCount;
        this.requestHandler = requestHandler;
    }

    @Override
    public void start() {
        try {
            startStopLock.lock();
            if (isRunning.compareAndSet(false, true)) {
                socketAccepterThread.set(startAccepterThread());
            } else {
                throw new ServerStateException("WebServer already running!");
            }
        } finally {
            startStopLock.unlock();
        }
    }

    @Override
    public void stop() {
        try {
            startStopLock.lock();
            if (isRunning.compareAndSet(true, false)) {
                awaitAccepterThreadTerminate();
            } else {
                throw new ServerStateException("WebServer already stopped!");
            }
        } finally {
            startStopLock.unlock();
        }
    }

    private void awaitAccepterThreadTerminate() {
        try {
            if (socketAccepterThread.get() != null) {
                socketAccepterThread.get().join();
            }
        } catch (InterruptedException ex) {
            logger.error("error while shutting down", ex);
        }
    }

    private Thread startAccepterThread() {
        ServerSocket server = startSocketServer();
        ExecutorService threadPool = Executors.newFixedThreadPool(workersCount);

        Thread socketAccepterThread = new Thread(() -> {
            try {
                socketAccepterLoop(server, threadPool);
            } catch (Exception ex) {
                logger.error("", ex);
            } finally {
                shutDownQuietly(server, threadPool);
            }
        });
        socketAccepterThread.start();
        return socketAccepterThread;
    }

    private void socketAccepterLoop(ServerSocket server, ExecutorService threadPool) {
        while (isRunning.get()) {
            try {
                Socket socket = server.accept();

                threadPool.submit(new SocketProcessor(socket, requestHandler));
            } catch (SocketTimeoutException soex) {
                logger.debug("timeout");
            } catch (IOException ex) {
                logger.error("Error while accepting socket", ex);
            }
        }
    }

    private void shutDownQuietly(ServerSocket server, ExecutorService executorService) {
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            logger.error("Error while shutting down web workers pool", ex);
        }

        try {
            server.close();
        } catch (Exception ex) {
            logger.error("Error while closing web server", ex);
        }
    }

    private ServerSocket startSocketServer() {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(SOCKET_TIMEOUT);
            logger.info("Listening for connection on port " + port + " ....");
            return server;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
