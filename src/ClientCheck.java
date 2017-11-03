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
        while (true) {
            if(!clients.isEmpty()) {
                for (MonoThreadClientHandler temp : clients) {
                    if (!temp.isAlive()) {
                        clients.remove(temp);
                    }
                }
            }
        }
    }

    public List<MonoThreadClientHandler> getClients(){return clients;}

    public void addToClientsList(MonoThreadClientHandler temp){clients.add(temp);}

    public void removeFromClientsList(MonoThreadClientHandler temp){clients.remove(temp);}
}
