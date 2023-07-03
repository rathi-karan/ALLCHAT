//Client.java
import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Client
{
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String username;
    public Client(Socket socket,String username)
    {
        try
        {
            this.socket=socket;
            this.br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username=username;
        }
        catch(IOException e)
        {
            closeEverything(socket,br,bw);
        }
    }
    public void sendMsg()
    {
        try
        {
            bw.write(username);
            bw.newLine();
            bw.flush();
            Scanner sc=new Scanner(System.in);
            while(socket.isConnected())
            {
                String msg=sc.nextLine();
                bw.write(username+" : "+msg);
                bw.newLine();
                bw.flush();
            }
        }
        catch(IOException e)
        {
            closeEverything(socket,br,bw); 
        }
    }
    public void listenMsg()
    {
        new Thread(new Runnable(){
            public void run()
            {
                String msgrecv;
                while(socket.isConnected())
                {
                    try
                    {
                        msgrecv=br.readLine();
                        System.out.println(msgrecv);
                    }
                    catch(IOException e)
                    {
                        closeEverything(socket,br,bw);
                    }
                }
            }
        }).start();
    }
    public void closeEverything(Socket socket,BufferedReader br,BufferedWriter bw)
    {
        try{
            if(socket!=null)
            {
                socket.close();
            }
            if(br!=null)
            {
                br.close();
            }
            if(bw!=null)
            {
                bw.close();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void main(String args[])throws IOException
    {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter your name to enter group chat:");
        String name=sc.nextLine();
        String ip=sc.nextLine();
        Socket socket=new Socket(ip,8080);
        Client client =new Client(socket,name);
        client.listenMsg();
        client.sendMsg();
    }
}
