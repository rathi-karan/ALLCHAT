//Server.java
import java.io.IOException;
import java.net.*;

public class Server{
    private ServerSocket serverSocket;
    public Server(ServerSocket serverSocket)
    {
        this.serverSocket=serverSocket;
    }
    public void startServer()
    {
        try
        {
            while(serverSocket.isClosed()!=true)
            {
                Socket client=serverSocket.accept();
                System.out.println("A new client is connected");
                ClientHandler clientHandler=new ClientHandler(client);
                Thread thread=new Thread(clientHandler);
                thread.start();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void closeServer()
    {
        try
        {
            if(serverSocket!=null)
            {
                serverSocket.close();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void main(String args[]) throws Exception
    {
        ServerSocket serverSocket=new ServerSocket(8080);
        Server server=new Server(serverSocket);
        server.startServer();
    }
}