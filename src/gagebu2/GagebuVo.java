package gagebu2;

public class GagebuVo {
	private int idx;
	private String wdate;
	private String gCode;
	private int price;
	private String content;
	
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
	@Override
	public String toString() {
		return "GagebuVo [idx=" + idx + ", wdate=" + wdate + ", gCode=" + gCode + ", price=" + price + ", content="
				+ content + "]";
	}
}
