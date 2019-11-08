package com.github.ibykhalov.simpleweb.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WebServer implements IWebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    private static final int SOCKET_TIMEOUT = 1000;

    private final int port;
    private final int workersCount;
    private final IRequestHandler requestHandler;

    private final ServerStateLatch serverStateLatch = new ServerStateLatch();

    public WebServer(int port, int workersCount, IRequestHandler requestHandler) {
        this.port = port;
        this.workersCount = workersCount;
        this.requestHandler = requestHandler;
    }

    @Override
    public void start() {
        if (serverStateLatch.tryStartServer()) {
            startServer();
        }
    }

    @Override
    public void stop() {
        serverStateLatch.beginAndAwaitShutDown();
    }

    private void startServer() {
        ServerSocket server = startSocketServer();
        ExecutorService threadPool = Executors.newFixedThreadPool(workersCount);

        new Thread(() -> {
            try {
                socketAccepterCycle(server, threadPool);
            } catch (Exception ex) {
                logger.error("", ex);
            } finally {
                shutDownQuietly(server, threadPool);
                serverStateLatch.completeShutDown();
            }
        }).start();
    }


    private void socketAccepterCycle(ServerSocket server, ExecutorService threadPool) {
        while (serverStateLatch.isRunning()) {
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
            server.close();
        } catch (Exception ex) {
            logger.error("Error while closing web server", ex);
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        } catch (Exception ex) {
            logger.error("Error while shutting down web workers pool", ex);
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
