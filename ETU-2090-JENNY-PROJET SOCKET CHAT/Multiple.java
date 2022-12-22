//classe plusieurs client
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Multiple implements Runnable{   
  public static ArrayList<Multiple> client=new ArrayList<>();
  private Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private String clientUsername;

  public Multiple(Socket socket)
  {
        try {
            this.socket=socket;
         this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientUsername=bufferedReader.readLine();
            client.add(this);
            broadcastMessage("server"   +clientUsername  +"   has entered the chat");


        } catch (Exception e) {
            closeEverything(socket,bufferedReader,bufferedWriter);
            // TODO: handle exception
        }
  }

  public void closeEverything(Socket socket2, BufferedReader bufferedReader2, BufferedWriter bufferedWriter2) {
    removeclientMultiple();   ///suppression des clients
    try {
        if (bufferedReader2!=null) {
            bufferedReader2.close();
        }
        if (bufferedWriter2!=null) {
            bufferedWriter2.close();

        }
        if (socket2!=null) {
            socket2.close();           
        }
    } catch (Exception e) {
        System.out.println(e.getMessage());
        // TODO: handle exception
    }

}

public void broadcastMessage(String msgTosend)  ///fonction qui envoie la message par le reseau socket
{
    for(Multiple clientMultiple:client)
    {
        try {
            if (!clientMultiple.clientUsername.equals(clientUsername)) {
                clientMultiple.bufferedWriter.write(msgTosend);
                clientMultiple.bufferedWriter.newLine();
                clientMultiple.bufferedWriter.flush();

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            closeEverything(socket, bufferedReader, bufferedWriter);
            // TODO: handle exception
        }
    }
}

public void removeclientMultiple() {
    client.remove(this);
    broadcastMessage("server"+    clientUsername   +"   a quitter le groupe");    
}

@Override
    public void run() {
        String msgClient;

        while (socket.isConnected()) {
            try {
                msgClient=bufferedReader.readLine();
                broadcastMessage(msgClient);
            } catch (Exception e) {
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
                // TODO: handle exception
            }            
        }
    }
}