package ass;

public enum Stock {
	IBM(100), MIC(100), ORA(100), SAP(100);

	Stock(int price) {
		this.price = price;
	}

	public int price;

	public static String pricesString() {
		StringBuffer sb = new StringBuffer();
		for (Stock s : Stock.values())
			sb.append(s + "(" + s.price + ") ");
		return sb.toString();
	}
}
