import ass.Card;
import ass.Stock;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import ass.Type;
/**
 * Created with IntelliJ IDEA.
 * User: Jakub
 * Date: 11/26/13
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameClient implements Runnable {
    private List<Card> hand;
    private HashMap<Stock,Integer> shares = new HashMap<Stock,Integer>();
    private HashMap<Stock,Integer> stock = new HashMap<Stock,Integer>();
    boolean startGame;
    boolean cardPlayed;
    Thread listeningThread;
    private Socket socket;
    Scanner instream;
    PrintWriter outstream;
    GameClientThread  client;
    Scanner keyboard;
    int cash;
    int movesdone=0;
    public static final String serverName = "localhost";
    public GameClient(String serverName,int serverPort){
        cash=0;
        startGame=false;
        cardPlayed=false;

        try{
            socket = new Socket(serverName,serverPort);
            keyboard = new Scanner(System.in);
            hand = new ArrayList<Card>();
            startStreams();
        }
        catch(UnknownHostException ehe){
            System.out.println("host exception");
        } catch (IOException e) {
            System.out.println("IOE exception");
        }
    }


    /// This method set up the input and output stream and starts the listening thread
    public void startStreams() throws IOException
    {
        instream = new Scanner(socket.getInputStream());
        outstream =new PrintWriter( socket.getOutputStream());
        if (listeningThread == null)
        {  client = new GameClientThread(this, socket);
            listeningThread = new Thread(this);
            listeningThread.start();
        }

    }
    ///stoping the listening thread
    public void stop(){
        listeningThread.interrupt();
        listeningThread=null;
    }
    ///triggered bythe Listender, handles the input
    public void handle(String[] args){
        if (args[0].equals("SETCASH")){
            setCash(Integer.parseInt(args[1]));
            System.out.println(cash);
        }
        else if (args[0].equals("DRAWCARD")){
            addCardToHand(args);

        }
        else if (args[0].equals("NEWROUND")){
            cardPlayed=false;
            movesdone=0;

        }
        else if (args[0].equals("STARTGAME")){
            startGame=true;

        }
        else if (args[0].equals("PRINTHAND")){
            printHand();
        }

        else if (args[0].equals("PRINTSTOCK")){
            printStock();
        }
        else if (args[0].equals("ADDSTOCK")){
            shares.put(Stock.valueOf(args[1]), Integer.parseInt(args[2]));
        }
        else if (args[0].equals("DELETESHARE")){
            if(shares.get(Stock.valueOf(args[1]))==Integer.parseInt(args[2])){
                shares.remove(Stock.valueOf(args[1]));

            }
            else{
                shares.put(Stock.valueOf(args[1]), shares.get(Stock.valueOf(args[1])) - Integer.parseInt(args[2]));
            }
        }
        else if (args[0].equals("CASH")){
            WriteToServer("CASHBALANCE " + getCash());
        }
        else if (args[0].equals("STOCK")){
            stock.put(Stock.valueOf(args[1]), Integer.parseInt(args[2]));
        }
    }
    ///send a message ot the server
    public void WriteToServer(String message){
        outstream.println(message);
        outstream.flush();
    }
    ///prints the Stock companies names and their prices
    public void printStock(){
        Iterator<Stock> keySetIterator = shares.keySet().iterator();

        while(keySetIterator.hasNext()){
            Stock key = keySetIterator.next();
            System.out.println("key: " + key + " value: " + shares.get(key));
        }
    }
    ///set the players cash
    public void setCash(int newCash){
        cash=newCash;
    }
    ///returns cash
    public int getCash(){
        return cash;
    }
    ///Adds a card to the player hand, this is performed once for every card dealed to the player at the beginning of the game
    public void addCardToHand(String[] args){
        System.out.println(args);
        Type ty= Type.valueOf(args[1]);
        Stock s1=null;
        if (args.length>2){
        s1=Stock.valueOf(args[2]);}
        Stock s2=null;
        if(args.length==4){
            s2=Stock.valueOf(args[3]);
        }
        Card drawnCard;
        System.out.println("adding new card" + ty);
        switch (ty) {
            case PLUS20:{
                drawnCard=new Card(ty,s1,null);
                System.out.println("adding new card PLUS20");
                hand.add(drawnCard);
                break;}
            case MINUS20:                  {
                drawnCard=new Card(ty,s1,null);
                System.out.println("adding new card MINUS20");
                hand.add(drawnCard);
                break;}
            case PLUSMINUS:      {
                drawnCard=new Card(ty,s1,s2);
                System.out.println("adding new card PLUSMINUS");
                hand.add(drawnCard);
                break;}
            case DOUBLE:      {
                drawnCard=new Card(ty,s1,null);
                System.out.println("adding new card DOUBLE");
                hand.add(drawnCard);
                break;}
            case HALF: {
                drawnCard=new Card(ty,s1,null);
                System.out.println("adding new card HALF");
                hand.add(drawnCard);
                break;}
            case NOEFFECT:  {
                drawnCard=new Card(ty,null,null);
                System.out.println("adding new card NOEFFECT");
                hand.add(drawnCard);
                break;}
        }


}
    ///displays cards that user has on his hand
    public void printHand(){
        System.out.println("hand size "+hand.size());
        for(Card c:hand){
            System.out.println(c.type+" "+c.stock1+" "+c.stock2);
        }
    }
    public static void main(String[] args) throws IOException {
        GameClient bc=new GameClient("localhost",8765);
    }

    @Override
    ///starts the thread
    public void run() {
          while (listeningThread != null)
        {   if (!startGame){
            try{
                if(keyboard.hasNext()){
                    Integer input=Integer.parseInt(keyboard.next());

                    if (input==1){
                        WriteToServer("START");

                    }


                }    }
            catch (Exception e){

            }


        }
            else{
            try{
                System.out.println("1 to buy 2 to sell or 3 to play a card");
            if(keyboard.hasNext()){
                Integer input=Integer.parseInt(keyboard.next());

                if (input==1){
                    if (movesdone<2&&cardPlayed==false){
                    buyMenu();       }
                    else {
                        System.out.println("You have traded enough shares this turn, play a card");
                    }

                }
                else if(input==2){
                    if (movesdone<2&&cardPlayed==false){
                        sellMenu();       }
                    else {
                        System.out.println("You have traded enough shares this turn, play a card");
                    }

                }
                else if(input==3){
                    if (cardPlayed==false){
                        playMenu();       }
                    else {
                        System.out.println("You have played a card this turn");
                    }

                }

                }    }
                catch (Exception e){

                }  }

            ///sendCommand(input);



        }
    }
    ///Displays the menu and awaits for input
    public void buyMenu(){
        boolean quit=false;
        while(!quit){
        try{
            System.out.println("Buy menu possible input; IBM MIC ORA SAP QUIT ;");
            System.out.println("Cash = "+cash);
            Iterator<Stock> it = stock.keySet().iterator();
            while(it.hasNext()){
                Stock key = it.next();
                System.out.println("Stock "+key+" price "+key.price);
            }
            if(keyboard.hasNext()){
                String input=(keyboard.nextLine());
                int n=0;
                if (input.equals("IBM")){
                    n=getNo();
                    if(n!=0){
                        summarizePurchase(Stock.IBM, n);
                        quit=true;
                    }

                }
                else if(input.equals("MIC")){
                    n=getNo();
                    if(n!=0){
                        summarizePurchase(Stock.MIC, n);
                        quit=true;
                    }

                }
                else if(input.equals("ORA")){
                    n=getNo();
                    if(n!=0){
                        summarizePurchase(Stock.ORA, n);
                        quit=true;
                    }

                }
                else if(input.equals("SAP")){
                    n=getNo();
                    if(n!=0){
                        summarizePurchase(Stock.SAP, n);
                        quit=true;
                    }

                }
                else if(input.equals("QUIT")){
                    quit=true;

                }

            }    }
        catch (Exception e){

        }


    } }
    ///Displays the menu and awaits for input
    public int getNo(){
        boolean quit=false;
        while(!quit){
            try{
                System.out.println("ENTER quantity");

                if(keyboard.hasNext()){
                    Integer input=(keyboard.nextInt());
                    return input;

                }    }
            catch (Exception e){
                return 0;
            }

            }
        return 0;

    }
    ///perform checks if the transaction is valid
    public void summarizePurchase(Stock s,int no){
        System.out.println("cash "+cash);
        System.out.println("here");
        int total=((stock.get(s)*no)+(no*5));
        System.out.println("Stock "+s+" cost "+s.price);
        System.out.println(total);
        System.out.println("cash "+cash);
        if(cash>=total){
            WriteToServer("CASHBALANCE "+cash);
            WriteToServer("BUY "+s+" "+no);
            movesdone++;
        }

    }
    ///perform checks if the transaction is valid
    public void summarizeSell(Stock s,int no){
        System.out.println("cash "+cash);
        System.out.println("here");
        int total=((stock.get(s)*no)+(no*5));
        System.out.println("Stock "+s+" cost "+s.price);
        System.out.println(total);
        System.out.println("cash "+cash);
        if(no<=shares.get(s)){
            WriteToServer("CASHBALANCE "+cash);
            WriteToServer("SELL "+s+" "+no);
            movesdone++;
        }

    }
    ///Displays the menu and awaits for input
    public void sellMenu(){
        boolean quit=false;
        while(!quit){
            try{
                System.out.println("Sell menu possible input; ;");
                System.out.println(shares.size());
                Iterator<Stock> it = shares.keySet().iterator();
                while(it.hasNext()){
                    Stock key = it.next();
                    System.out.println("Stock "+key+" price "+key.price+" no "+shares.get(key));
                }
                if(keyboard.hasNext()){
                    String input=(keyboard.nextLine());
                    int n=0;
                    if (input.equals("IBM")&&shares.containsKey(Stock.valueOf(input))){
                        n=getNo();
                        if(n!=0){
                            summarizeSell(Stock.IBM,n);
                            quit=true;
                        }

                    }
                    else if(input.equals("MIC")){
                        n=getNo();
                        if(n!=0){
                            summarizeSell(Stock.MIC,n);
                            quit=true;
                        }

                    }
                    else if(input.equals("ORA")){
                        n=getNo();
                        if(n!=0){
                            summarizeSell(Stock.ORA,n);
                            quit=true;
                        }

                    }
                    else if(input.equals("SAP")){
                        n=getNo();
                        if(n!=0){
                            summarizeSell(Stock.SAP,n);
                            quit=true;
                        }

                    }
                    else if(input.equals("QUIT")){
                        quit=true;

                    }

                }    }
            catch (Exception e){

            }


        }

    }
    ///Displays the menu and awaits for input
    public void playMenu(){

            try{
                System.out.println("Play menu possible input; ;");
                System.out.println(hand.size());
                int count=0;
                for (Card c : hand){

                    System.out.println(count+" "+c);
                    count++;
                }
                System.out.println("enter "+hand.size()+" to go back");
                if(keyboard.hasNext()){
                    int input=(keyboard.nextInt());
                    if(0<=input&&input<=hand.size()){
                        WriteToServer("PLAY "+hand.get(input).type+" "+hand.get(input).stock1+" "+hand.get(input).stock2);
                        hand.remove(input);
                        cardPlayed=true;

                    }
                    else if(input==hand.size()){
                        return;

                    }
                    else {
                        System.out.println("unsupported command");
                    }




                }    }
            catch (Exception e){

            }




    }
}
