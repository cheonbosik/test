package gagebu;

public class GagebuVo {
	private int idx;
	private String wdate;
	private String gCode;
	private int price;
	private String content;
	private int balance;
	
	private int imsiPrice;  // 수정처리시에 수입/지출 금액의 변경처리된 금액을 담아놓기위한 필드
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getWdate() {
		return wdate;
	}
	public void setWdate(String wdate) {
		this.wdate = wdate;
	}
	public String getgCode() {
		return gCode;
	}
	public void setgCode(String gCode) {
		this.gCode = gCode;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getImsiPrice() {
		return imsiPrice;
	}
	public void setImsiPrice(int imsiPrice) {
		this.imsiPrice = imsiPrice;
	}
	@Override
	public String toString() {
		return "GagebuVo [idx=" + idx + ", wdate=" + wdate + ", gCode=" + gCode + ", price=" + price + ", content="
				+ content + ", balance=" + balance + ", imsiPrice=" + imsiPrice + "]";
	}
}
