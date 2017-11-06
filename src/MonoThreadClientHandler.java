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
    private String switchString;
    ClientCheck checker;

    public MonoThreadClientHandler(Socket socket, Server server, ClientCheck checker){
        client = socket;
        this.checker = checker;
        this.server = server;
        this.setDaemon(true);
    }

    @Override
    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
            name = in.readLine();
            checker.addToClientsList(this);
            server.sendList(checker.getClients());
            while(true){
                input = in.readLine();
                switchString = input.substring(1, input.length());
                if(input.charAt(0) == '\\'){
                    switch(switchString){
                        case "quit":{
                            server.isOver();
                            break;
                        }
                        case "out":
                            checker.removeFromClientsList(this);
                            server.sendList(checker.getClients());
                            break;
                    }
                }
                //System.out.println("Message: " + input);
                server.send(input, name);
            }
        }catch (IOException e){}
    }

    public PrintWriter getOut(){return out;}

    public Socket getClient(){return client;}

    public String getUserName(){return name;}
}
