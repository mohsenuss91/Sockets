

import ass.Card;
import ass.Stock;
import ass.Type;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;



public class GameService implements Runnable {

    private Socket s;
    private Scanner in;
    private PrintWriter out;
    private Game game;
    private int cashAtHand;
    private boolean actionPerformed;
    String logMessage;

    public GameService(Socket aSocket, Game agame) throws IOException {
        s = aSocket;
        game = agame;
        out = new PrintWriter(s.getOutputStream());
        actionPerformed=false;
        logMessage="";
        cashAtHand=1000;
    }

    public void run() {
        try {
            in = new Scanner(s.getInputStream());
            WriteToClient("Welcome to the Game.");
            doService();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.println("Thanks for playing the Game.");
            out.flush();
        }
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client disconnected.");
    }
    public void WriteToClient(String message){
        out.println(message);
        out.flush();
    }
    public void setCash(){
        WriteToClient("SETCASH "+cashAtHand);
    }

    public void doService() throws IOException {
        while (true) {
            if (!in.hasNextLine())
                return;
            String command = in.nextLine().toUpperCase();

            System.out.println("command:"+ command);
            System.out.println(command);
            if (command.equals("QUIT"))  {
                System.out.println("QUIT");
                return;                   }
            else if(command.equals("START")){
                game.setStartBool(true);
            }

            else{
                System.out.println("else");
                String[] args = command.trim().split("\\s+");
                System.out.println("else"+args[0]);

            if(args[0].equals("SELL")){
                cashAtHand=cashAtHand+(Stock.valueOf(args[1]).price*Integer.parseInt(args[2]))-(5*Integer.parseInt(args[2]));
                WriteToClient("DELETESHARE "+args[1]+" "+args[2]);
                WriteToClient("SETCASH "+cashAtHand);


            }
            else if(args[0].equals("CASHBALANCE")){
                cashAtHand=Integer.parseInt(args[1]);
            }
            else if(args[0].equals("BUY")){
                System.out.println("here");
                Stock stockk=Stock.valueOf(args[1]);
                System.out.println("here");
                int no=Integer.parseInt(args[2]);
                System.out.println("here");
                int cost=(stockk.price*no)+(5*no);
                System.out.println("here");
                System.out.println("cashATHand "+cashAtHand);
                cashAtHand=cashAtHand-cost;
                System.out.println("cashATHand "+cashAtHand);

                WriteToClient("ADDSTOCK " + stockk + " " + no);
                WriteToClient("SETCASH "+cashAtHand );


            }
            else if(args[0].equals("PLAY")){
                Card playcard=null;
                System.out.println(args);
                if (args[2].equals("NULL")){
                     playcard=new Card(Type.valueOf(args[1]),null,null);
                }
                else if(args[3].equals("NULL"))
                {
                     playcard=new Card(Type.valueOf(args[1]),Stock.valueOf(args[2]),null);

                }
                else{
                 playcard=new Card(Type.valueOf(args[1]),Stock.valueOf(args[2]),Stock.valueOf(args[3]));
                }
                playcard.action();
                setActionPerformed(true);



            }

            else {
                executeCommand(command, args);
            }  }
        }
    }
    public void startRound(){
        for(GameService gs:game.returnPlayers()){
            WriteToClient("player "+gs.cashAtHand);
            WriteToClient("SETCASH "+cashAtHand);
            WriteToClient("NEWROUND");

        }

    }

    public void executeCommand(String command, String[] args) {
        logMessage.concat( "BankService.executeCommand(" + command
                + Arrays.toString(args) + ")\n");
        return;
    }
    public void setActionPerformed(boolean ap){
        actionPerformed=ap;
    }
    public boolean getActionPerformed(){
        return actionPerformed;
    }

}
