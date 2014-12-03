package ass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Jakub
 * Date: 11/27/13
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Deck {
    public List<Card> plus20Cards = new ArrayList<Card>(4);
    public List<Card> minus20Cards = new ArrayList<Card>(4);
    public List<Card> plusMinusCards = new ArrayList<Card>(4);
    public List<Card> doubleCards = new ArrayList<Card>(4);
    public List<Card> halfCards = new ArrayList<Card>(4);
    public List<Card> noEffectCards = new ArrayList<Card>(4);
    public Deck(){
        for (Stock s : Stock.values()) {
            plus20Cards.add(new Card(Type.PLUS20, s, null));
            minus20Cards.add(new Card(Type.MINUS20, s, null));
            doubleCards.add(new Card(Type.DOUBLE, s, null));
            halfCards.add(new Card(Type.HALF, s, null));
            noEffectCards.add(new Card(Type.NOEFFECT, null, null));
        }

        int i = 0;
        for (Stock s1 : Stock.values())
            for (Stock s2 : Stock.values())
                if (s1 != s2)     {
                    System.out.println("added");
                    plusMinusCards.add( new Card(Type.PLUSMINUS, s1, s2));     }
    }
    public void Shuffle(){
        final Random random = new Random(4454776669L);

        Collections.shuffle ( plus20Cards, random);
        Collections.shuffle ( minus20Cards, random);
        Collections.shuffle ( doubleCards, random);
        Collections.shuffle ( halfCards, random);
        Collections.shuffle ( noEffectCards, random);
        Collections.shuffle ( plusMinusCards, random);

    }
    public Card drawCard(Type type){
        switch (type) {
            case PLUS20:
                return plus20Cards.remove(0);
            case MINUS20:
                return minus20Cards.remove(0);
            case PLUSMINUS:
                return plusMinusCards.remove(0);
            case DOUBLE:
                return doubleCards.remove(0);
            case HALF:
                return halfCards.remove(0);
            case NOEFFECT:
                return noEffectCards.remove(0);
            default:
                return null;
        }

    }


    }

