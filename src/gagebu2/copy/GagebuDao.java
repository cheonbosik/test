package gagebu2.copy;

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
		try {
			sql = "insert into gagebu2 values (default,default,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getgCode());
			pstmt.setInt(2, vo.getPrice());
			pstmt.setString(3, vo.getContent());
			pstmt.executeUpdate();
			System.out.println("자료가 입력되었습니다.");
		} catch (SQLException e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		
	}

	// 앞의 전체 처리와 내용이 유사하기에 앞쪽의 gList()메소드와 통합처리하고자 한다. 이때 gList()메소드는 매개변수를 'list'라는 문자로 넘겼을대를 전체조회로 처리한다.
	public ArrayList<GagebuVo> gSearch(String wdate) {
		ArrayList<GagebuVo> vos = new ArrayList<GagebuVo>();
		
		try {
			if(wdate.equals("list")) {  // 전체조회일경우에는 wdate매개변수에 'list'값을 담아서 넘어온다.
				sql = "select * from gagebu2 order by wdate desc, idx desc";  // 날짜 내림차순으로 정렬(수정때 날짜를 변경할 수 있기에..) 또 동순위면 idx내림차순.
				pstmt = conn.prepareStatement(sql);
			}
			else {  // 날짜별 조회일때는 wdate에 검색할 날짜가 넘어온다.
				sql = "select * from gagebu2 where replace(substr(wdate,1,10),'-','')=? order by wdate desc, idx desc"; // sql의 substr(변수,시작위치,꺼낼갯수)
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
	public void gDelete(int idx) {
		try {
			sql = "delete from gagebu2 where idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
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
			sql = "select * from gagebu2 where idx = ?";
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
			// 앞에서 수정처리하여 넘겨준 내역을 직접 수정처리해준다.
			sql = "update gagebu2 set wdate=?,gCode=?,price=?,content=? where idx=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getWdate());
			pstmt.setString(2, vo.getgCode());
			pstmt.setInt(3, vo.getPrice());
			pstmt.setString(4, vo.getContent());
			pstmt.setInt(5, vo.getIdx());
			pstmt.executeUpdate();
			if(pstmt != null) pstmt.close();
			
			System.out.println("자료가 수정처리 되었습니다.");
		} catch (SQLException e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
	}
	
}
