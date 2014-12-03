import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
Sets up the socket and awaits client connecting, on connect it will start GameService for each client
 */
public class GameServer {

    public static void main(String[] args) throws IOException {
        List<Thread> threadSet=new ArrayList<Thread>();
        final int portNo=8765;
        Game cardGame=new Game();
        Thread gameThread;

        gameThread=new Thread(cardGame);
        Game game=new Game();
        Thread t;
        ServerSocket gameServer= new ServerSocket(portNo);
        System.out.println("waiting for client to connect");
        gameThread.start();

        while(true){
            Socket s = gameServer.accept();
            t=null;
            GameService gameService= new GameService(s,game);
            t = new Thread(gameService);
            t.start();
            if (t!=null){
                threadSet.add(t);
                game.addPlayer(gameService);
                if (!game.getAllowedToStart()&&threadSet.size()>1){
                    System.out.println("get going");
                    game.setAllowedToStart(true);


                }
            }

        }

    }
}
