import ass.Card;
import ass.Deck;
import ass.Stock;
import ass.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jakub
 * Date: 11/26/13
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Game implements Runnable {
    static boolean allowedToStart;
    public static boolean endGame;
    public static boolean startBool;
    static List<GameService> Players;
    public Game(){
        allowedToStart=false;
        endGame=false;
        startBool=false;
        Players=new ArrayList<GameService>();

    }
    public boolean getAllowedToStart(){
        return allowedToStart;
    }
    public void setAllowedToStart(boolean newAllowedToStart){
        allowedToStart=newAllowedToStart;
    }
    public void setStartBool(boolean startbool){
        startBool=startbool;
    }
    public void addPlayer(GameService newPlayer){
        Players.add(newPlayer);
    }
    public List<GameService> returnPlayers(){
        return Players;
    }
    ///Main Game loop
    public void startGame(){
        for (GameService gs : Players)  {
            gs.setCash();
        }
        Deck cardDeck=new Deck();
        cardDeck.Shuffle();
        for (GameService gs : Players)  {
            Card drawnCard=null;
            drawnCard = cardDeck.drawCard(Type.PLUS20);
            gs.WriteToClient("DRAWCARD "+drawnCard.type+" "+drawnCard.stock1);
            drawnCard = cardDeck.drawCard(Type.MINUS20);
            gs.WriteToClient("DRAWCARD "+drawnCard.type+" "+drawnCard.stock1);
            drawnCard = cardDeck.drawCard(Type.DOUBLE);
            gs.WriteToClient("DRAWCARD "+drawnCard.type+" "+drawnCard.stock1);
            drawnCard = cardDeck.drawCard(Type.HALF);
            gs.WriteToClient("DRAWCARD "+drawnCard.type+" "+drawnCard.stock1);
            drawnCard = cardDeck.drawCard(Type.NOEFFECT);
            gs.WriteToClient("DRAWCARD "+drawnCard.type);
            for(int i=0;i<3;i++){
                drawnCard = cardDeck.drawCard(Type.PLUSMINUS);
                gs.WriteToClient("DRAWCARD "+drawnCard.type+" "+drawnCard.stock1+" "+drawnCard.stock2);

            }
            gs.WriteToClient("PRINTHAND");


        }
        for(int round=1; round<8;round++){
            boolean roundFinished=false;

                for (GameService gs : Players)  {
                    gs.WriteToClient("NEWROUND "+round);
                    gs.WriteToClient("PLAY");
                    gs.startRound();
                    for (Stock x: Stock.values()){
                        gs.WriteToClient("STOCK "+x+" "+x.price);
                        System.out.println(x + " " + x.price);
                    }
                }
                while(!roundFinished){
                    roundFinished=true;
                    for (GameService gs : Players)  {
                        if(!gs.getActionPerformed()){
                            roundFinished=false;
                        }
                    }

            }
        }
        while(true){

        }



    }

    @Override
    ///starts a new thread
    public void run() {
        if (!allowedToStart){
            for (GameService gs : Players)  {
                gs.WriteToClient("Wait "+Players.size());

            }
        }
        while(!endGame){
            int a=0;///for  some reason the if statement dont work with out a line in the front
            if (allowedToStart){
                for (GameService gs : Players)  {
                    gs.WriteToClient("We have more than one player press 1 to start the game");
                }
                while(!endGame){
                    int nood=0;
                    if(startBool){



                System.out.println("allowed");
                for (GameService gs : Players)  {
                    gs.WriteToClient("StartGame");
                }
                startGame();
                }                 }


            }
        }
    }
}
