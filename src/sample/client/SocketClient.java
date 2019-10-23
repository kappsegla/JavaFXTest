package sample.client;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;


/**
 * This class can be used for sending and receiving Java Strings over TCP/IP from JavaFX.
 * sendMessage will run the sending in a thread so a call from JavaFX thread won't be blocked.
 * Interface implementations sent to setReceiveListener will be wrapped in a Platform.runLater
 * automatically.
 */

public class SocketClient {

    Socket socket;
    PrintWriter writer;
    BufferedReader reader;
    public ExecutorService threadPool;

    public SocketClient() {
        threadPool = Executors.newFixedThreadPool(2);
    }

    //<editor-fold desc="connectedProperty">
    private SimpleBooleanProperty connected = new SimpleBooleanProperty(false);

    public boolean isConnected() {
        return connected.get();
    }

    public SimpleBooleanProperty connectedProperty() {
        return connected;
    }

    public void setConnected(boolean connected) {
        Platform.runLater(() -> this.connected.set(connected));
    }
    //</editor-fold>

    public void connect(String host, Integer port) {
        if( isConnected() && socket != null ) {
            try {
                socket.close();
                socket = null;
                setConnected(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Task<Socket> task = new Task<Socket>() {
            @Override
            protected Socket call() throws Exception {
                return new Socket(host, port);
            }
        };
        task.setOnRunning(this::connecting);
        task.setOnFailed(this::connectionFailed);
        task.setOnSucceeded(this::connected);

        task.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                socket = newValue;
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                threadPool.submit(this::receiveMessages);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        threadPool.submit(task);
    }

    private void connected(WorkerStateEvent workerStateEvent) {
        System.out.println("Connected");
        setConnected(true);
    }

    private void connectionFailed(WorkerStateEvent workerStateEvent) {
        System.out.println("Failed");
    }

    private void connecting(WorkerStateEvent workerStateEvent) {
        System.out.println("Connecting...");
    }

    public void sendMessage(String message) {
        //Send message to server
        if (connected.get())
            threadPool.submit(() -> writer.println(message));
    }

    Consumer<String> listener = null;

    /**
     * Sets a receive listener that will be called every time a new message is received.
     * Safe to use from JavaFX because the code will be wrappen in a Platform.runLater(() -> {} );
     * @param listener
     */
    public void setReceiveListener(Consumer<String> listener) {
        this.listener = listener;
    }

    private void receiveMessages() {
        while (true) {
            try {
                String message = reader.readLine();
                Platform.runLater(() -> {
                if (listener != null)
                    listener.accept(message);
                });
            }catch (IOException e) {
                setConnected(false);
                return;
            }
        }
    }

    public void close() {
        try {
            if( socket != null)
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
