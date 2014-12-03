package ass;

import java.util.Arrays;

public final class Card {

	public final Type type;
	public final Stock stock1;
	public final Stock stock2;

	public Card(Type type, Stock stock1, Stock stock2) {
		this.type = type;
		this.stock1 = stock1;
		this.stock2 = stock2;
	}

	public String toString() {
		switch (type) {
		case PLUS20:
			return stock1.toString() + "+20";
		case MINUS20:
			return stock1.toString() + "-20";
		case PLUSMINUS:
			return stock1.toString() + "+10/" + stock2.toString() + "-10";
		case DOUBLE:
			return stock1.toString() + "*2";
		case HALF:
			return stock1.toString() + "/2";
		case NOEFFECT:
			return "NOEFFECT";
		default:
			return null;
		}
	}

	public void action() {
		switch (type) {
		case PLUS20:
			stock1.price += 20;
			break;
		case MINUS20:
			stock1.price -= 20;
			break;
		case DOUBLE:
			stock1.price *= 2;
			break;
		case HALF:
			stock1.price /= 2;
			break;
		case PLUSMINUS:
			stock1.price += 10;
			stock2.price -= 10;
			break;
		case NOEFFECT:
			break;
		default:
			break;
		}
	}

	public static final Card[] plus20Cards = new Card[4];
	public static final Card[] minus20Cards = new Card[4];
	public static final Card[] plusMinusCards = new Card[12];
	public static final Card[] doubleCards = new Card[4];
	public static final Card[] halfCards = new Card[4];
	public static final Card[] noEffectCards = new Card[4];

	public static void initCards() {
		for (Stock s : Stock.values()) {
			plus20Cards[s.ordinal()] = new Card(Type.PLUS20, s, null);
			minus20Cards[s.ordinal()] = new Card(Type.MINUS20, s, null);
			doubleCards[s.ordinal()] = new Card(Type.DOUBLE, s, null);
			halfCards[s.ordinal()] = new Card(Type.HALF, s, null);
			noEffectCards[s.ordinal()] = new Card(Type.NOEFFECT, null, null);
		}

		int i = 0;
		for (Stock s1 : Stock.values())
			for (Stock s2 : Stock.values())
				if (s1 != s2)
					plusMinusCards[i++] = new Card(Type.PLUSMINUS, s1, s2);
	}

	public static void displayCards() {
		System.out.println(Arrays.deepToString(plus20Cards));
		System.out.println(Arrays.deepToString(minus20Cards));
		System.out.println(Arrays.deepToString(plusMinusCards));
		System.out.println(Arrays.deepToString(doubleCards));
		System.out.println(Arrays.deepToString(halfCards));
		System.out.println(Arrays.deepToString(noEffectCards));
	}

	public static void testActions() {

		System.out.println(Stock.pricesString());
		for (Card[] cards : new Card[][] { plus20Cards, minus20Cards,
				plusMinusCards, doubleCards, halfCards, noEffectCards })
			for (Card c : cards) {
				c.action();
				System.out.println("Card " + c + ": " + Stock.pricesString());
			}
	}

	public static void main(String[] args) {
		initCards();
		displayCards();
		testActions();
	}

}
