import java.io.*;
import java.net.Socket;

/**
 * Created by damaz on 02.11.2017.
 */
public class MonoThreadClientHandler extends Thread{
    private Socket client;
    private String input;
    private BufferedReader in;
    private PrintWriter out;
    private Server server;
    private String name;

    public MonoThreadClientHandler(Socket socket, Server server){
        client = socket;
        this.server = server;
        this.setDaemon(true);
    }

    @Override
    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
            name = in.readLine();
            while(true){
                input = in.readLine();
                if(input.equals("quit")) server.isOver();
                //System.out.println("Message: " + input);
                server.send(input, name);
            }
        }catch (IOException e){}
    }

    public PrintWriter getOut(){return out;}

    public Socket getClient(){return client;}
}
