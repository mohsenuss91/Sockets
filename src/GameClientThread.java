import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
This program listens to the input and if its valid it sends it to the handler method in GameClient
 */
public class GameClientThread extends Thread
{  private Socket socket   = null;
    private GameClient       client   = null;
    private Scanner streamIn = null;

    public GameClientThread(GameClient _client, Socket _socket)
    {  client   = _client;
        socket   = _socket;
        open();
        start();
    }
    public void open()
    {  try
    {  streamIn  = new Scanner(socket.getInputStream());
    }
    catch(IOException ioe)
    {  System.out.println("Error getting input stream: " + ioe);
        client.stop();
    }
    }
    public void close()
    {  try
    {  if (streamIn != null) streamIn.close();
    }
    catch(Exception e)
    {  System.out.println("Error closing input stream: " + e);
    }
    }
    public void run()
    {  while (true)
    {  try
    {   String command = streamIn.nextLine().toUpperCase();
        String[] args = command.trim().split("\\s+");
        System.out.println(args.length);
        System.out.println(command);
        client.handle(args);
    }
    catch(Exception e)
    {  System.out.println("Listening error: " + e.getMessage());
        client.stop();
    }
    }
    }
}