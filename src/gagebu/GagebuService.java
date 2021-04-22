package gagebu;

import java.util.ArrayList;
import java.util.Scanner;

public class GagebuService {
	Scanner sc = new Scanner(System.in);
  GagebuVo vo = new GagebuVo();
  GagebuDao dao = new GagebuDao();

	// 가계부 입력처리
	public void gInput() {
		while(true) {
			System.out.print("작업선택 : 1.수입    2.지출    0.종료  ==> ");
			String sel = sc.next();
			
			if(sel.equals("0")) break;
			
			if(sel.equals("1")) vo.setgCode("+");
			else vo.setgCode("-");
			
			System.out.print("금액 : "); vo.setPrice(sc.nextInt());
			System.out.print("내역 : "); vo.setContent(sc.next());
			dao.gInput(vo);
		}
	}

	// 전체조회 처리
	public void gList(int su) {
		ArrayList<GagebuVo> vos = new ArrayList<GagebuVo>();
		String wdate = "";
		
		if(su == 3) {  // 전체 조회
			//vos = dao.gList();
			vos = dao.gSearch("list");
		}
		else if(su == 2) {  // 날짜별 조회
			System.out.print("검색할 날짜를 입력하세요?(예:20210420) ==>");
			wdate = sc.next();
			vos = dao.gSearch(wdate);
		}
		
		String gCode = "";
		int balance = 0, sw = 0, suip = 0, jichul = 0;
		
		System.out.println("============================================================");
		System.out.println("  거래날짜\t비고\t  금액\t\t 적요");
		System.out.println("------------------------------------------------------------");
		
		for(GagebuVo vo : vos) {
			if(sw == 0) {
				balance = vo.getBalance();
				sw = 1;
			}
			
			if(vo.getgCode().equals("+")) {
				gCode = "수입";
				suip += vo.getPrice();
			}
			else {
				gCode = "지출";
				jichul += vo.getPrice();
			}
			System.out.println(vo.getWdate().substring(0, 10)+"\t"+gCode+"\t"+String.format("%,7d", vo.getPrice()) +"\t\t"+vo.getContent());
		}
		System.out.println("============================================================");
		if(su == 2) System.out.println("검색일자 : " + wdate + " , 수입 : " + suip + " , 지출 : " + jichul);
		System.out.println("현재 총 잔액 : " + String.format("%,7d", balance));
		System.out.println("============================================================");
	}

	// 삭제프로그램 or 수정프로그램
	//public void gDelete() {
	public void gDeleteUpdate(String choice) {
		ArrayList<GagebuVo> vos = new ArrayList<GagebuVo>();
		String wdate = "";
		
		System.out.print("검색할 날짜를 입력하세요?(예:20210420) ==>");
		wdate = sc.next();
		vos = dao.gSearch(wdate);
		
		String gCode = "";
		
		System.out.println("============================================================");
		System.out.println("고유번호\t거래날짜\t\t비고\t  금액\t\t 적요");
		System.out.println("------------------------------------------------------------");
		
		for(GagebuVo vo : vos) {
			if(vo.getgCode().equals("+")) {
				gCode = "수입";
			}
			else {
				gCode = "지출";
			}
			System.out.println(vo.getIdx()+"\t"+vo.getWdate().substring(0, 10)+"\t"+gCode+"\t"+String.format("%,7d", vo.getPrice()) +"\t\t"+vo.getContent());
		}
		System.out.println("============================================================");
		int idx = 0;
		System.out.print("삭제 또는 수정할 고유번호를 선택하세요?(종료:0) ");
		idx = sc.nextInt();
		if(idx == 0) return;
		if(choice.equals("D")) {  // 삭제처리
			dao.gDelete(idx);
		}
		else {  // 수정처리
			vo = dao.gUpdateSearch(idx);
			if(vo == null) {
				System.out.println("검색자료가 없습니다.");
				return;
			}
			
			int imsiPrice = 0;
			if(vo.getgCode().equals("+")) {
				gCode = "수입";
				imsiPrice = vo.getPrice();
			}
			else {
				gCode = "지출";
				imsiPrice = vo.getPrice() * (-1);
			}
			int price = 0;
			System.out.println("-----------------------------------------------------------------------------------------------------------");
			System.out.println("수정항목 선택? 1.날짜:"+vo.getWdate().substring(0, 10)+"     2.비고(수입/지출)금액:"+gCode+"("+vo.getPrice()+")     3.내용:"+vo.getContent()+"     0.종료");
			System.out.println("-----------------------------------------------------------------------------------------------------------");
			System.out.print(">> ");
			int no = sc.nextInt();
			switch (no) {
				case 1:
					System.out.print("변경하실 날짜는?(예:20210420) ==> ");
					vo.setWdate(sc.next());
					break;
				case 2:
					System.out.print("변경하실 비고는? 1.수입   2.지출 ==> ");
					gCode = sc.next();
					System.out.print("변경하실 금액은? ==> ");
					vo.setPrice(sc.nextInt());
					if(vo.getPrice() == 0) {
						System.out.println("수정처리되지 않았습니다.");
						return;
					}
					if(gCode.equals("1")) {
						vo.setgCode("+");
						price = vo.getPrice();
					}
					else {
						vo.setgCode("-");
						price = -vo.getPrice();
					}
					break;
				case 3:
					System.out.print("변경하실 내용은?(모두 공백없이 붙여서 입력하세요) ==> ");
					vo.setContent(sc.next());
					break;
				default:
					System.out.println("수정처리되지 않았습니다.");
					return;
			}
			if(price != 0) vo.setImsiPrice(price - imsiPrice);  // 금액의 변화가 있으면 imsiPrice필드의 값이 0이 아니다.
			else vo.setImsiPrice(0);
			dao.setUpdate(vo);  // 수정처리메소드 호출
		}
	}

	
}
