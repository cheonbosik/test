package gagebu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GagebuDao {
	public Connection conn = null;
	public PreparedStatement pstmt = null;
	public ResultSet rs = null;
	
	String sql = "";
	
	GagebuVo vo = null;
	
	// 생성자를 통한 데이터베이스 연결
	public GagebuDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/works";
			String user = "green";
			String password = "1234";
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 검색 실패!!!");
		} catch (Exception e) {
			System.out.println("데이터베이스 연동실패!!");
		}
	}
	
	// 데이터베이스 Close(Connection객체 Close)
	public void dbClose() {
		if(conn != null)
			try {
				conn.close();
			} catch (Exception e) {}
	}
	
	// PreparedStatement객체 Close
	public void pstmtClose() {
		if(pstmt != null)
			try {
				pstmt.close();
			} catch (Exception e) {}
	}
	
	// ResultSet객체 Close
	public void rsClose() {
		if(rs != null)
			try {
				rs.close();
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
	}

	// 가계부 입력처리
	public void gInput(GagebuVo vo) {
		int balance;
		try {
			// 기존의 잔고를 읽어온다.
			sql = "select balance from gagebu order by idx desc limit 1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) balance = rs.getInt("balance");
			else balance = 0;
			if(pstmt != null) pstmt.close();
			
			// '수입/지출'인지를 판별하여 잔액을 계산한다.
			if(vo.getgCode().equals("+")) balance += vo.getPrice();
			else balance -= vo.getPrice();
			
			// 입력된 자료를 가계부테이블에 등록한다.
			sql = "insert into gagebu values (default,default,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getgCode());
			pstmt.setInt(2, vo.getPrice());
			pstmt.setString(3, vo.getContent());
			pstmt.setInt(4, balance);
			pstmt.executeUpdate();
			System.out.println("자료가 입력되었습니다.");
		} catch (SQLException e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		
	}

	/*  전체조회와 개별 날짜조회는 루틴이 비슷하기에 하나로 통합처리한다.
	public ArrayList<GagebuVo> gList() {
		ArrayList<GagebuVo> vos = new ArrayList<GagebuVo>();
		
		try {
			sql = "select * from gagebu order by idx desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				vo = new GagebuVo();
				
				vo.setIdx(rs.getInt("idx"));
				vo.setWdate(rs.getString("wdate"));
				vo.setgCode(rs.getString("gCode"));
				vo.setPrice(rs.getInt("price"));
				vo.setContent(rs.getString("content"));
				vo.setBalance(rs.getInt("balance"));
				
				vos.add(vo);
			}
		} catch (Exception e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		
		return vos;
	}
	*/

	// 앞의 전체 처리와 내용이 유사하기에 앞쪽의 gList()메소드와 통합처리하고자 한다. 이때 gList()메소드는 매개변수를 'list'라는 문자로 넘겼을대를 전체조회로 처리한다.
	public ArrayList<GagebuVo> gSearch(String wdate) {
		ArrayList<GagebuVo> vos = new ArrayList<GagebuVo>();
		
		try {
			// 먼저 총 잔액을 구해온다.
			int balance = 0;
			sql = "select balance from gagebu order by idx desc limit 1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) balance = rs.getInt(1);
			
			if(wdate.equals("list")) {  // 전체조회일경우에는 wdate매개변수에 'list'값을 담아서 넘어온다.
				sql = "select * from gagebu order by wdate desc, idx desc";  // 날짜 내림차순으로 정렬(수정때 날짜를 변경할 수 있기에..) 또 동순위면 idx내림차순.
				pstmt = conn.prepareStatement(sql);
			}
			else {  // 날짜별 조회일때는 wdate에 검색할 날짜가 넘어온다.
				sql = "select * from gagebu where replace(substr(wdate,1,10),'-','')=? order by wdate desc, idx desc"; // sql의 substr(변수,시작위치,꺼낼갯수)
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, wdate);
			}
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				vo = new GagebuVo();
				
				vo.setIdx(rs.getInt("idx"));
				vo.setWdate(rs.getString("wdate"));
				vo.setgCode(rs.getString("gCode"));
				vo.setPrice(rs.getInt("price"));
				vo.setContent(rs.getString("content"));
				vo.setBalance(balance);  // 모든 레코드의 balance에는 총잔액이 들어간다.
				
				vos.add(vo);
			}
		} catch (Exception e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		
		return vos;
	}

	// DB에서 실제 레코드 삭제처리
	// 레코드를 삭제하게되면 삭제되는 레코드의 '수입/지출(prict)'에 대한 값을 최종 잔액변수(balance)에 누적시켜줘야한다.  
	public void gDelete(int idx) {
		try {
			// 1.삭제를 요청한 고유번호(idx)에 해당하는 gCode와 price를 구해온다.
			String gCode = "";
			int price = 0;
			sql = "select gCode,price from gagebu where idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				gCode = rs.getString("gCode");
				price = rs.getInt("price");
			}
			if(pstmt != null) pstmt.close();
			
  		// 2.가장 마지막 레코드의 잔고를 읽어온다.(왜냐하면 가장마지막 레코드의 잔고가 최종 잔고이기 때문이다)
			int balance = 0;
			sql = "select balance from gagebu order by idx desc limit 1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				balance = rs.getInt("balance");
			}
			if(pstmt != null) pstmt.close();
			
			// 3.삭제할 레코드에 입력되어 있는 price를 최종잔고에 (+/-)시켜준다. 수입은 -로, 지출은 +로..
			if(gCode.equals("+")) balance -= price;
			else balance += price;
			
			// 4. 삭제를 원하는 레코드를 삭제처리한다.
			sql = "delete from gagebu where idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
			pstmt.executeUpdate();
			pstmtClose();
			
			// 5. 다시, 가장 나중에 입력된 레코드의 고유번호(idx)를 읽어온다.(입력된 고유번호의 최대값을 읽어와도 된다)
			sql = "select idx from gagebu order by idx desc limit 1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int imsiIdx = 0;
			if(rs.next()) {
				imsiIdx = rs.getInt("idx");
			}
			if(pstmt != null) pstmt.close();
			
			// 6. 앞에서 기억시켜두었던 현재 잔액을, 가장 마지막레코드 잔액에 업데이트시켜준다.
			sql = "update gagebu set balance = ? where idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, balance);
			pstmt.setInt(2, imsiIdx);
			pstmt.executeUpdate();
			pstmtClose();
			
			System.out.println("자료가 삭제처리 되었습니다.");
		} catch (SQLException e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			pstmtClose();
		}
		
	}

	// 수정처리를 위한 개별자료 검색후 돌려보내기
	public GagebuVo gUpdateSearch(int idx) {
		try {
			vo = new GagebuVo();
			sql = "select * from gagebu where idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				vo.setIdx(idx);
				vo.setWdate(rs.getString("wdate"));
				vo.setgCode(rs.getString("gCode"));
				vo.setPrice(rs.getInt("price"));
				vo.setContent(rs.getString("content"));
			}
		} catch (SQLException e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			pstmtClose();
		}
		return vo;
	}

	// 실제로 수정처리가 일어나는곳
	public void setUpdate(GagebuVo vo) {
		try {
			// 1.금액이 수정처리되었다면 잔액이 변경될수 있기에 먼저 고유번호(idx)와 최종잔액(balance)을 가져온다. 즉, 가장 마지막 레코드의 잔고를 읽어온다.(왜냐하면 가장마지막 레코드의 잔고가 최종 잔고이기 때문)
			int balance = 0, idx = 0;
			if(vo.getImsiPrice() != 0) {
				sql = "select idx,balance from gagebu order by idx desc limit 1";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					idx = rs.getInt("idx");
					balance = rs.getInt("balance") + vo.getImsiPrice();  // imsiPrice의 값이 0이 아니면 금액에 변화가 있었기에 총잔액을 다시 계산한다.
				}
				if(pstmt != null) pstmt.close();
			}
			
			// 2.앞에서 수정처리하여 넘겨준 내역을 직접 수정처리해준다.
			sql = "update gagebu set wdate=?,gCode=?,price=?,content=? where idx=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getWdate());
			pstmt.setString(2, vo.getgCode());
			pstmt.setInt(3, vo.getPrice());
			pstmt.setString(4, vo.getContent());
			pstmt.setInt(5, vo.getIdx());
			pstmt.executeUpdate();
			if(pstmt != null) pstmt.close();
			
			// 3. 최종적으로 금액(vo.getImsiPrice())의 수정이 있었다면, 총잔액(balance)을 마지막 레코드(idx)에 update처리한다.
			if(vo.getImsiPrice() != 0) {
				sql = "update gagebu set balance=? where idx=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, balance);
				pstmt.setInt(2, idx);
				pstmt.executeUpdate();
			}
			System.out.println("자료가 수정처리 되었습니다.");
		} catch (SQLException e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
	}
	
}
