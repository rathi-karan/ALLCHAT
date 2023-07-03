// ClientHandler.java
import java.util.ArrayList;
import java.io.*;
import java.net.*;
public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clienthandlers=new ArrayList<>();
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String username;
    public ClientHandler(Socket socket)
    {
        try
        {
            this.socket=socket;
            this.bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username=br.readLine();
            clienthandlers.add(this);
            broadCast("SERVER: "+username+" has joined the chat");
        }
        catch(IOException e)
        {
            closeEverything(socket,br,bw);
        }
    }
    public void run()
    {
        String recv;
        while(socket.isClosed()!=true)
        {
            try
            {
                recv=br.readLine();
                broadCast(recv);
            }
            catch(IOException e)
            {
                closeEverything(socket,br,bw);
                break;
            }
        }
    }
    public void broadCast(String msg)
    {
        for(ClientHandler i:clienthandlers)
        {
            try
            {
                if(!i.username.equals(username))
                {
                    i.bw.write(msg);
                    i.bw.newLine();
                    i.bw.flush();
                }
            }
            catch(IOException e)
            {
                closeEverything(socket,br,bw);
            }
        }
    }
    public void removeClientHandler()
    {
        clienthandlers.remove(this);
        broadCast("SERVER: "+username+" has left the chat");
    }
    public void closeEverything(Socket socket ,BufferedReader br, BufferedWriter bw)
    {
        removeClientHandler();
        try
        {
            if(br!=null)
            {
                br.close();
            }
            if(bw!=null)
            {
                bw.close();
            }
            if(socket!=null)
            {
                socket.close();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
