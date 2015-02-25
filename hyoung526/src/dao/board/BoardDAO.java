package dao.board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.board.BoardModel;

public class BoardDAO implements BoardDAOImpl{
	
	//데이터베이스 관련 클래스들의 초기화, 모든 메소드에서 사용할 수 있게 멤버변수 선언
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	//데이터베이스 접속 정보로, 변경될 수 없게 상수와 멤버변수로 선언
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/test";
	private final String DB_ID = "root";
	private final String DB_PWD = "1234";
	
	//게시판 목록 조회
	public List<BoardModel> selectList(BoardModel boardModel){
				
		List<BoardModel> boardList = null;
		try {
			Class.forName(this.JDBC_DRIVER);
			this.conn = DriverManager.getConnection(this.DB_URL, this.DB_ID, this.DB_PWD);
			
			int pageNum = Integer.parseInt(boardModel.getPageNum());
			int listCount = boardModel.getListCount();
			String searchType = boardModel.getSearchType();
			String searchText = boardModel.getSearchText();
						
			StringBuffer sb = new StringBuffer();
			if(searchType != null){
				if("ALL".equals(searchType)){
					sb.append("WHERE SUBJECT LIKE '%").append(searchText).append("%' OR WRITER LIKE '%").append(searchText).append("%' OR CONTENTS LIKE '%").append(searchText).append("%'");
				}else if("SUBJECT".equals(searchType)){
					sb.append("WHERE SUBJECT LIKE '%").append(searchText).append("%'");
				}else if("WRITER".equals(searchType)){
					sb.append("WHERE WRITER LIKE '%").append(searchText).append("%'");
				}else if("CONTENTS".equals(searchType)){
					sb.append("WHERE CONTENTS LIKE '%").append(searchText).append("%'");
				}
			}
			
			this.pstmt = this.conn.prepareStatement("SELECT NUM, SUBJECT, CONTENTS, WRITER, HIT, REG_DATE FROM" +
					" BOARD "+ sb.toString() +"  LIMIT ?, ?");
			this.pstmt.setInt(1, listCount * (pageNum-1));
			this.pstmt.setInt(2, listCount);
			this.rs = this.pstmt.executeQuery();
			
			boardList = new ArrayList<BoardModel>();
			
			while(this.rs.next()){
				boardModel = new BoardModel();
				boardModel.setNum(this.rs.getInt("NUM"));
				boardModel.setSubject(this.rs.getString("SUBJECT"));
				boardModel.setContents(this.rs.getString("CONTENTS"));
				boardModel.setWriter(this.rs.getString("WRITER"));
				boardModel.setHit(this.rs.getInt("HIT"));
				boardModel.setRegDate(this.rs.getString("REG_DATE"));
				
				boardList.add(boardModel);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			close(this.rs, this.pstmt, this.conn);
		}
		return boardList;
	}
	
	//게시판 수 조회
	public int selectCount(BoardModel boardModel){
		
		int totalCount = 0;
		
		try {
			//데이터베이스 드라이버를 로딩하고 커넥션 정보 얻음
			Class.forName(this.JDBC_DRIVER);
			this.conn = DriverManager.getConnection(this.DB_URL, this.DB_ID, this.DB_PWD);
			
			String searchType = boardModel.getSearchType();
			String searchText = boardModel.getSearchText();
			
			//조건문 생성
			StringBuffer sb = new StringBuffer();
			if(searchType != null){
				if("ALL".equals(searchType)){
					sb.append("WHERE SUBJECT LIKE '%").append(searchText).append("%' OR WRITER LIKE '%").append(searchText).append("%' OR CONTENTS LIKE '%").append(searchText).append("%'");
				}else if("SUBJECT".equals(searchType)){
					sb.append("WHERE SUBJECT LIKE '%").append(searchText).append("%'");
				}else if("WRITER".equals(searchType)){
					sb.append("WHERE WRITER LIKE '%").append(searchText).append("%'");
				}else if("CONTENTS".equals(searchType)){
					sb.append("WHERE CONTENTS LIKE '%").append(searchText).append("%'");
				}
			}
			
			//질의문 생성
			this.pstmt = this.conn.prepareStatement("SELECT COUNT(*) CNT FROM BOARD "+ sb.toString() +"");
			//질의문 실행
			this.rs = this.pstmt.executeQuery();
			
			if(this.rs.next()){
				totalCount = Integer.parseInt(this.rs.getString("CNT"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return totalCount;
	}
	
	//게시판 상세 조회
	public BoardModel select(BoardModel boardModel){
		
		try {
			//데이터베이스 드라이버를 로딩하고, 커넥션 정보를 얻음.
			Class.forName(this.JDBC_DRIVER);
			this.conn = DriverManager.getConnection(this.DB_URL, this.DB_ID, this.DB_PWD);
			//질의문 설정
			this.pstmt = this.conn.prepareStatement("SELECT NUM, SUBJECT, CONTENTS, WRITER, HIT, REG_DATE FROM" +
					" BOARD WHERE NUM=?");
			this.pstmt.setInt(1, boardModel.getNum());
			this.rs = this.pstmt.executeQuery();
			
			//질의문에 해당하는 레코드가 있을 경우
			if(this.rs.next()){
				//BoardModel set 메소드에 결과값들을 설정
				boardModel.setNum(this.rs.getInt("NUM"));
				boardModel.setSubject(this.rs.getString("SUBJECT"));
				boardModel.setContents(this.rs.getString("CONTENTS"));
				boardModel.setWriter(this.rs.getString("WRITER"));
				boardModel.setHit(this.rs.getInt("HIT"));
				boardModel.setRegDate(this.rs.getString("REG_DATE"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			close(this.rs, this.pstmt, this.conn);
		}
		
		return boardModel;
	}
	
	//게시판 등록 처리
	public void insert(BoardModel boardModel){
		
		try {
			//데이터베이스 드라이버를 로딩하고, 커넥션을 얻음
			Class.forName(this.JDBC_DRIVER);
			this.conn = DriverManager.getConnection(this.DB_URL, this.DB_ID, this.DB_PWD);
			//질의문 생성
			this.pstmt = this.conn.prepareStatement("INSERT INTO BOARD( SUBJECT, WRITER, CONTENTS, IP, HIT, REG_DATE, MOD_DATE ) " +
													"VALUES( ?, ?, ?, ?, 0, NOW(), NOW() )");
			//바인딩 변수 셋팅
			this.pstmt.setString(1, boardModel.getSubject());
			this.pstmt.setString(2, boardModel.getWriter());
			this.pstmt.setString(3, boardModel.getContents());
			this.pstmt.setString(4, boardModel.getIp());
			//질의문 실행
			this.pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			close(this.rs, this.pstmt, this.conn);
		}
		
	}
	
	//게시판 수정 처리
	public void update(BoardModel boardModel) {
		try {
			//데이터베이스 드라이버 생성 후 커넥션 객체 얻음
			Class.forName(this.JDBC_DRIVER);
			this.conn = DriverManager.getConnection(this.DB_URL, this.DB_ID, this.DB_PWD);
			//쿼리문 생성
			this.pstmt = this.conn.prepareStatement("UPDATE BOARD SET \n" +
												    "       SUBJECT = ? \n" +
												    "       ,WRITER = ? \n" +
												    "       ,CONTENTS = ? \n" +
												    "       ,IP = ? \n" +
												    "       ,MOD_DATE = NOW() \n" +
													" WHERE NUM = ?");
			//바인딩 변수 셋팅
			this.pstmt.setString(1, boardModel.getSubject());
			this.pstmt.setString(2, boardModel.getWriter());
			this.pstmt.setString(3, boardModel.getContents());
			this.pstmt.setString(4, boardModel.getIp());
			this.pstmt.setInt(5, boardModel.getNum());
			//쿼리문 실행
			this.pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			close(this.rs, this.pstmt, this.conn);
		}
		
	}
	
	//게시판 조회수 증가 수정 처리
	public void updateHit(BoardModel boardModel){
		try {
			//데이터베이스 드라이버 로딩 후 커넥션 객체 얻음
			Class.forName(this.JDBC_DRIVER);
			this.conn = DriverManager.getConnection(this.DB_URL, this.DB_ID, this.DB_PWD);
			//쿼리문 생성
			this.pstmt = this.conn.prepareStatement("UPDATE BOARD SET HIT = HIT + 1 WHERE NUM = ?");
			//바인팅 변수 셋팅
			this.pstmt.setInt(1, boardModel.getNum());
			//쿼리문 실행
			this.pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			close(this.rs, this.pstmt, this.conn);
		}
		
	}
	
	//게시판 삭제 처리
	public void delete(BoardModel boardModel){
		try {
			//데이터베이스 드라이버 로딩 후 커넥션 객체 얻음
			Class.forName(this.JDBC_DRIVER);
			this.conn = DriverManager.getConnection(this.DB_URL, this.DB_ID, this.DB_PWD);
			//쿼리문 생성
			this.pstmt = this.conn.prepareStatement("DELETE FROM BOARD WHERE NUM = ?");
			//바인딩 변수 셋팅
			this.pstmt.setInt(1, boardModel.getNum());
			//쿼리문 실행
			this.pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			close(this.rs, this.pstmt, this.conn);
		}
		
	}
	
	//사용되었던 데이터베이스 관련 객체들을 종료함
	public void close(ResultSet rs, PreparedStatement pstmt, Connection conn){
		if(rs != null){
			try{
				rs.close();
			}catch(SQLException e){e.printStackTrace();}
		}
		if(pstmt != null){
			try{
				pstmt.close();
			}catch(SQLException e){e.printStackTrace();}
		}
		if(conn != null){
			try{
				conn.close();
			}catch(SQLException e){e.printStackTrace();}
		}
	}
}
