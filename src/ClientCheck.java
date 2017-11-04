import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by damaz on 04.11.2017.
 */
public class ClientCheck extends Thread{
    private List<MonoThreadClientHandler> clients;

    ClientCheck(){
        clients = new ArrayList<>();
        this.setDaemon(true);
    }

    @Override
    public void run(){
        String clientsResult;
        while (true) {
            clientsResult = "";
            while(!clients.isEmpty()) {
                for (MonoThreadClientHandler temp : clients) {
                    if (temp.getClientText().isClosed()) {
                        clients.remove(temp);
                    } else {
                        clientsResult += temp.getUserName();
                        clientsResult += "\n";
                    }
                }
                System.out.println(clients.size());
                for(MonoThreadClientHandler temp : clients){
                    System.out.println(temp);
                    temp.getOutClients().println(clientsResult);
                }
            }
        }
    }

    public List<MonoThreadClientHandler> getClients(){return clients;}

    public synchronized void addToClientsList(MonoThreadClientHandler temp){clients.add(temp);}
}
