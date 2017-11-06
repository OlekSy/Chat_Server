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
    private ServerSocket serverSocket;
    private boolean notOver = true;
    private ClientCheck checker;
    private StringBuilder clientList = new StringBuilder();
    private String tempClientList;

    public static void main(String[] args){
        new Server().start();
        System.out.println("Server is up and running!");
    }

    @Override
    public void run(){
        checker = new ClientCheck();
        MonoThreadClientHandler temp;
        try {
            serverSocket = new ServerSocket(PORT);
            while(notOver) {
                Socket socket = serverSocket.accept();
                temp = new MonoThreadClientHandler(socket, this, checker);
                temp.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
                for (MonoThreadClientHandler newTemp : checker.getClients()) {
                    newTemp.getClient().close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void send(String message, String name){
        if(!checker.getClients().isEmpty()) {
            for (MonoThreadClientHandler temp : checker.getClients()) {
                temp.getOut().println("<" + name + ">: " + message);
            }
        }
    }

    public void sendList(){
        if(!checker.getClients().isEmpty()){
            clientList = new StringBuilder();
            for(MonoThreadClientHandler temp : checker.getClients()){
                clientList.append(temp.getUserName()).append("@~#");
            }
            tempClientList = clientList.toString();
            for(MonoThreadClientHandler temp : checker.getClients()){
                temp.getOut().println("\\list");
                temp.getOut().println(tempClientList);
            }
        }
    }

    public void isOver(){
        notOver = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
