package com.mysite.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysite.common.JDBCUtil;

public class BoardDAO {
	
	//DAO : Repository (JPA) , DataBase를 직접 접근 하는 객체 
		// Insert / Update / Delete / Select  <== 쿼리가 저장되어 직접 DB접근 자바 객체 
	
	//사용할 변수 선언 : private 
		// Connection, PreparedStatement, ResultSet  <== java.sql.* 
	private Connection conn = null ; 
	private PreparedStatement pstmt = null; 
	private ResultSet rs = null; 
	
	//SQL 쿼리를 상수로 정의후에 각각 필요한 메소드에서 사용  
	private final String BOARD_INSERT = 
			"insert into board (seq,title,write,content) values ((select nvl(max(seq),0) + 1 from board), ?,?,?)"; 
	private final String BOARD_UPDATE = ""; 
	private final String BOARD_DELETE = ""; 
	private final String BOARD_GET = ""; 
	private final String BOARD_LIST = "select * from board order by seq desc"; 
	
	
	//1. board 테이블에 값을 넣는 메소드 : insert  
	//  BOARD_INSERT = "insert into board (seq,title,write,content) 
		// values ((select nvl(max(seq),0) + 1 from board), ?,?,?)";
	public void insertBoard (BoardDTO dto) {
		System.out.println("======= insertBoard 기능 처리 ======");
		
		try {
			conn = JDBCUtil.getConnection();    // conn 객체가 활성화 ( Oracle / XE / HR2 / 1234 )
			
			// PreparedStatement 객체를 활성화 
			pstmt = conn.prepareStatement(BOARD_INSERT);   // pstmt 객체 활성화 
			// ? 에 들어갈 변수의 값을 dto에 getter를 사용해서 값을  할당 
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getWrite());
			pstmt.setString(3, dto.getContent());
			
			//PreparedStatement 객체를 실행 : DB에 값이 Insert 됨 
			pstmt.executeUpdate() ;    // insert / update / delete 
			
			System.out.println("BOARD 테이블에 값이 잘 Insert 되었습니다. ");
			
		}catch (Exception e) {
			 e.printStackTrace();
			System.out.println("BOARD 테이블에 값이 잘 Insert 실패 했습니다.");
		}finally {
			//사용한 객체를 제거함. 
			JDBCUtil.close(pstmt, conn);
			
		}
		
		
	}
	
	//2. UPDATE 
	
	//3. DELETE 
	
	
	//4. 상세 페이지 (GET) : 레코드 1개 
	
	//5. 리스트 페이지 (BOARD_LIST) : 레코드 여러개 
			// BOARD_LIST = "select * from board order by seq desc"
	public List<BoardDTO> getBoardList(BoardDTO dto){
		System.out.println("getBoardList 메소드 호출 - 게시판 리스트 페이지");
		
		//주의 : 블락 밖에서 선언 해야 한다. 
		List<BoardDTO> boardList = new ArrayList<BoardDTO>();     // try 블락 밖에서 선언되어야 한다. 
		
		try {
			conn = JDBCUtil.getConnection(); 
			pstmt = conn.prepareStatement(BOARD_LIST); 
			rs = pstmt.executeQuery(); 				// rs 에는 select 한 결과 레코드셋 을 담고 있다.
			
			//rs의 값을 끄집어 내어서 DTO에 저장 
			while ( rs.next()) {
				
				// 주의 : While 블락 내에서 DTO 객체를 생성 해야 board 의 힙주소가 새로 생성됨. 
				BoardDTO board = new BoardDTO(); 	// DTO 객체를 while 루프 내에서 생성 
				
				board.setSeq(rs.getInt("SEQ"));
				board.setTitle(rs.getNString("TITLE"));
				board.setWrite(rs.getString("WRITE"));
				board.setContent(rs.getString("CONTENT"));
				board.setRegdate(rs.getDate("REGDATE"));
				board.setCnt(rs.getInt("CNT"));
				
				//boardList에 DTO를 추가 
				boardList.add(board); 			
			}
			
			System.out.println("boardList 에 모든 레코드 추가 성공 ");
				
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("boardList 에 모든 레코드 추가 실패 ");
			
		}finally {
			// 사용 한 객체 close 
			JDBCUtil.close(rs, pstmt, conn);
		}
		
	
		return boardList; 	  // boardList : board 테이블의 각각의 레코드를 dto에 담아서 boardList 에 저장됨 
	}
	
	
	
	
	
	
	

}