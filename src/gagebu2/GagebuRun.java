package gagebu2;

import java.util.Scanner;

public class GagebuRun {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		GagebuDao dao = new GagebuDao();
		GagebuService service = new GagebuService();
		
		boolean run = true;
		
		System.out.println("========== 가 계 부 프 로 그 램(v1.1) ==========");
		while(run) {
			System.out.println("------------------------------------------------------------");
			System.out.println("작업선택: 1.입력   2.날자조회   3.전체조회   4.수정   5.삭제   0.종료");
			System.out.println("------------------------------------------------------------");
			System.out.print(">> ");
			int no = sc.nextInt();
			
			switch(no) {
				case 1:  // 자료 입력
					service.gInput();
					break;
				case 2:  // 날짜 조회
					service.gList(2);
					break;
				case 3:  // 전체 조회
					service.gList(3);
					break;
				case 4:  // 수정
					//service.gUpdate();
					service.gDeleteUpdate("U");
					break;
				case 5:  // 삭제
					//service.gDelete();
					service.gDeleteUpdate("D");
					break;
				default:  // 종료
					run = false;
					break;
			}
		}
		System.out.println("================================");
		System.out.println("\t 작업끝...");
		System.out.println("================================");
		dao.dbClose();
		sc.close();
	}
}
