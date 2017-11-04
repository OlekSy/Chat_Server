import java.io.*;
import java.net.Socket;

/**
 * Created by damaz on 02.11.2017.
 */
public class MonoThreadClientHandler extends Thread{
    private Socket clientText;
    private Socket clientClients;
    private String input;
    private BufferedReader inText;
    private PrintWriter outText;
    private PrintWriter outClients;
    private Server server;
    private String name;

    public MonoThreadClientHandler(Socket socketText, Socket socketClients, Server server){
        clientText = socketText;
        clientClients = socketClients;
        this.server = server;
        this.setDaemon(true);
    }

    @Override
    public void run(){
        try{
            //System.out.println(clientClients.isClosed());
            inText = new BufferedReader(new InputStreamReader(clientText.getInputStream()));
            outText = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientText.getOutputStream())), true);

            outClients = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientClients.getOutputStream())), true);

            //System.out.println(outText + "\n" + outClients);

            //System.out.println(outClients);

            name = inText.readLine();
            while(true){
                input = inText.readLine();
                if(input.equals("quit")) server.isOver();
                //System.out.println("Message: " + input);
                server.send(input, name);
            }
        }catch (IOException e){}
    }

    public PrintWriter getOutText(){return outText;}

    public PrintWriter getOutClients(){return outClients;}

    public Socket getClientText(){return clientText;}

    public Socket getClientClients(){return clientClients;}

    public String getUserName(){return name;}
}
