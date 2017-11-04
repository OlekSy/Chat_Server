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

    public static void main(String[] args){
        new Server().start();
    }

    @Override
    public void run(){
        checker = new ClientCheck();
        checker.start();
        MonoThreadClientHandler temp;
        try {
            serverSocket = new ServerSocket(PORT);
            while(notOver) {
                Socket socketText = serverSocket.accept();
                Socket socketClients = serverSocket.accept();
                temp = new MonoThreadClientHandler(socketText, socketClients, this);
                //System.out.println(temp.getClientText() + "\n" + temp.getClientClients());
                checker.addToClientsList(temp);
                //System.out.println(checker.getClients().size());
                temp.start();
            }
        } catch (IOException e){}
        finally {
            try {
                serverSocket.close();
                for (MonoThreadClientHandler newTemp : checker.getClients()) {
                    newTemp.getClientText().close();
                    newTemp.getClientClients().close();
                }
            } catch(IOException e){}
        }
    }

    public void send(String message, String name){
        if(!checker.getClients().isEmpty()) {
            for (MonoThreadClientHandler temp : checker.getClients()) {
                temp.getOutText().println("<" +
                        name + ">: " + message);
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
