import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by damaz on 02.11.2017.
 */
public class Server extends Thread{
    private static final int PORT = 8080;
    private List<MonoThreadClientHandler> clients;
    private ServerSocket serverSocket;
    private boolean notOver = true;

    public static void main(String[] args){
        new Server().start();
    }

    @Override
    public void run(){
        MonoThreadClientHandler temp;
        clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(PORT);
            while(notOver) {
                Socket socket = serverSocket.accept();
                temp = new MonoThreadClientHandler(socket, this);
                clients.add(temp);
                temp.start();
            }
        } catch (IOException e){}
        finally {
            System.out.println("We are out from while.");
            try {
                serverSocket.close();
                for (MonoThreadClientHandler newTemp : clients) {
                    newTemp.getClient().close();
                }
            } catch(IOException e){}
        }
    }

    public void send(String message){
        for(MonoThreadClientHandler temp : clients){
            temp.getOut().println(message);
        }
    }

    public void isOver(){
        notOver = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
