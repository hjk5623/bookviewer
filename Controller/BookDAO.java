package Controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Book;
import Model.BookDB;
public class BookDAO {
	public static ArrayList<Book> dbArrayList = new ArrayList<>();//????
	 
	//1. 학생등록하는 함수
	public static int insertBookData(BookDB bookDB) {
		//1.1 데이타베이스에 학생테이블에 입력하는 쿼리문.
		StringBuffer insertBook = new StringBuffer();
		insertBook.append("insert into booktbl ");
		insertBook.append("(no,title,writer,score,genre,mdate,state,finish,number,image,story,markimage) ");
		insertBook.append("values ");
		insertBook.append("(?,?,?,?,?,?,?,?,?,?,?,?) ");
		//1.2 데이타베이스 Connection을 가져와야 한다.
		Connection con = null;
		//1.3 쿼리문을 실행해야할 Statement를 만들어야한다.
		PreparedStatement psmt=null;
		int count=0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(insertBook.toString());
			//1.4 쿼리문에 실제 데이타를 연결한다.
			psmt.setInt(1, bookDB.getNo());
			psmt.setString(2, bookDB.getTitle());
			psmt.setString(3, bookDB.getWriter());
			psmt.setDouble(4, bookDB.getScore());
			psmt.setString(5, bookDB.getGenre());
			psmt.setDate(6, bookDB.getMdate());
			psmt.setString(7, bookDB.getState());
			psmt.setString(8, bookDB.getFinish());
			psmt.setInt(9, bookDB.getNumber());
			psmt.setString(10, bookDB.getImage());
			psmt.setString(11, bookDB.getStroy());
			psmt.setString(12, bookDB.getMarkImage());
			//1.5 실제데이타를 연결한 쿼리문을 실행한다.
			count = psmt.executeUpdate();
			if(count==0) {
				MainController.callAlert("삽입 쿼리 실패 : 삽입 쿼리문이 실패했습니다.");
				return count;
			}
		} catch (SQLException e) {
			MainController.callAlert("삽입 실패 : 데이타베이스 삽입이 실패했습니다.");
		} finally {
			//1.6 자원객체를 닫아준다.
			try {
				if(psmt != null) { psmt.close(); }
				if(con != null) { con.close(); }
			} catch (SQLException e) { 
				MainController.callAlert("자원 닫기 실패 : psmt, con 닫기 실패.");
			}
		}
		return count;
	}	
	//2. 테이블에서 전체내용을 모두 가져오는 함수
	public static ArrayList<Book> getBookTotalData(){
		//2.1 데이타베이스에 학생테이블에 있는 레코드를 모두 가져오는 쿼리문
				String selectBook = "select * from booktbl";
				//2.2 데이타베이스 Connection을 가져와야 한다.
				Connection con = null;
				//2.3 쿼리문을 실행해야할 Statement를 만들어야한다.
				PreparedStatement psmt=null;
				//2.4 쿼리문을 실행하고나서 가져와야할 레코드를 담고있는 보자기 객체
				ResultSet rs = null;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(selectBook);
					rs = psmt.executeQuery();
					if(rs==null) {
						MainController.callAlert("select 실패 : select 쿼리문이 실패했습니다.");
						return null;
					}
					while(rs.next()) {
						Book book = new Book(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getString(5),rs.getDate(6),
								rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10));
						dbArrayList.add(book);
					}
				} catch (SQLException e) {
					MainController.callAlert("삽입 실패 : 데이타베이스 삽입이 실패했습니다.");
				} finally {
					//2.6 자원객체를 닫아준다.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("자원 닫기 실패 : psmt, con 닫기 실패.");
					}
				}
		
		return dbArrayList;
	}	
	//3. 테이블뷰에서 선택한 레코드를 데이타베이스에서 삭제하는 함수
	public static int deleteBookData(int no) {
		//3.1 데이타베이스에 학생테이블에 있는 레코드를 삭제하는 쿼리문
		String deleteBook = "delete from booktbl where no = ? ";
		//3.2 데이타베이스 Connection을 가져와야 한다.
		Connection con = null;
		//3.3 쿼리문을 실행해야할 Statement를 만들어야한다.
		PreparedStatement psmt=null;
		//3.4 쿼리문을 실행하고나서 가져와야할 레코드를 담고있는 보자기 객체
		int count=0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(deleteBook);
			psmt.setInt(1, no);
			//3.5 실제데이타를 연결한 쿼리문을 실행한다.(번개를 치는 것)
			//executeQuery() 쿼리문을 실행해서 결과를 가져올때 사용하는 번개문
			//executeUpdate() 쿼리문을  실행해서 테이블에 저장을 할때 사용하는 번개문
			count = psmt.executeUpdate();
			if(count==0) {
				MainController.callAlert("delete 실패 : delete 쿼리문이 실패했습니다.");
				return count;
			}
		} catch (SQLException e) {
			MainController.callAlert("삭제 실패 : 데이타베이스 삭제가 실패했습니다.");
		} finally {
			//3.6 자원객체를 닫아준다.
			try {
				if(psmt != null) { psmt.close(); }
				if(con != null) { con.close(); }
			} catch (SQLException e) { 
				MainController.callAlert("자원 닫기 실패 : psmt, con 닫기 실패.");
			}
		}
		return count;
	}
	//4. 테이블뷰에서 수정한 레코드를 데이타베이스 테이블에 수정하는함수.
	public static int updateBookData(BookDB bookDB) {
		//4.1 데이타베이스에 학생테이블에 수정하는 쿼리문
				StringBuffer updateStudent = new StringBuffer();
				updateStudent.append("update booktbl set ");
				updateStudent.append("score=?,finish=?,number=? where no=? ");
			
				//4.2 데이타베이스 Connection을 가져와야 한다.
				Connection con = null;
				//4.3 쿼리문을 실행해야할 Statement를 만들어야한다.
				PreparedStatement psmt=null;
				int count=0;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(updateStudent.toString());
					//4.4 쿼리문에 실제 데이타를 연결한다.
					psmt.setDouble(1, bookDB.getScore());
					psmt.setString(2, bookDB.getFinish());
					psmt.setInt(3, bookDB.getNumber());
					psmt.setInt(4, bookDB.getNo());
					
					//4.5 실제데이타를 연결한 쿼리문을 실행한다.
					count = psmt.executeUpdate();
					if(count==0) {
						MainController.callAlert("수정 쿼리 실패 : 수정 쿼리문이 실패했습니다.");
						return count;
					}
				} catch (SQLException e) {
					MainController.callAlert("수정 실패 : 데이타베이스 수정이 실패했습니다.");
				} finally {
					//4.6 자원객체를 닫아준다.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("자원 닫기 실패 : psmt, con 닫기 실패.");
					}
				}
		return count;
	}
	//5 데이터베이스에서 story 내용을 가지고 오는 함수
	public static String getBookStoryData(int no){
		String string=null;
				String selectBook = "select story from booktbl where no =" + no ;
				//5.2 데이타베이스 Connection을 가져와야 한다.
				Connection con = null;
				//5.3 쿼리문을 실행해야할 Statement를 만들어야한다.
				PreparedStatement psmt=null;
				//5.4 쿼리문을 실행하고나서 가져와야할 레코드를 담고있는 보자기 객체
				ResultSet rs = null;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(selectBook);
					
					//5.5 실제데이타를 연결한 쿼리문을 실행한다.(번개를 치는 것)
					rs = psmt.executeQuery();
					
					if(rs==null) {
						MainController.callAlert("select 실패 : select 쿼리문이 실패했습니다.");
						return null;
					}
					while(rs.next()) {
						string = rs.getString("story");
					}
				} catch (SQLException e) {
					MainController.callAlert("삽입 실패 : 데이타베이스 삽입이 실패했습니다.");
				} finally {
					//5.6 자원객체를 닫아준다.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("자원 닫기 실패 : psmt, con 닫기 실패.");
					}
				}
		
		return string;
	}
	//6 데이터베이스에서 장르별 카운트수를 가지고 오는 함수
	public static int getBookGenreData(String genre){
		int count =0;
				String selectBook = "select count(genre) from booktbl where genre =" +  "'"+genre+"'";
				//6.2 데이타베이스 Connection을 가져와야 한다.
				Connection con = null;
				//6.3 쿼리문을 실행해야할 Statement를 만들어야한다.
				PreparedStatement psmt=null;
				//6.4 쿼리문을 실행하고나서 가져와야할 레코드를 담고있는 보자기 객체
				ResultSet rs = null;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(selectBook);
					
					//6.5 실제데이타를 연결한 쿼리문을 실행한다.(번개를 치는 것)
					rs = psmt.executeQuery();
					
					if(rs==null) {
						MainController.callAlert("select 실패 : select 쿼리문이 실패했습니다.");
						return 0;
					}
					while(rs.next()) {
						count = rs.getInt("count(genre)");
					}
				} catch (SQLException e) {
					MainController.callAlert("삽입 실패 : 데이타베이스 삽입이 실패했습니다.");
				} finally {
					//6.6 자원객체를 닫아준다.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("자원 닫기 실패 : psmt, con 닫기 실패.");
					}
				}
		
		return count;
	}
	//7. 북마크 저장시 해당 등록번호의 markImage 테이블에 값을 저장하는 함수
	public static int updateMarkImageData(BookDB bookDB) {
		//7.1 데이타베이스에 북테이블에 수정하는 쿼리문
				StringBuffer updateStudent = new StringBuffer();
				updateStudent.append("update booktbl set ");
				updateStudent.append("markimage=? where no=? ");
			
				//7.2 데이타베이스 Connection을 가져와야 한다.
				Connection con = null;
				//7.3 쿼리문을 실행해야할 Statement를 만들어야한다.
				PreparedStatement psmt=null;
				int count=0;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(updateStudent.toString());
					//7.4 쿼리문에 실제 데이타를 연결한다.
					psmt.setString(1, bookDB.getMarkImage());
					psmt.setInt(2, bookDB.getNo());
					
					//7.5 실제데이타를 연결한 쿼리문을 실행한다.
					count = psmt.executeUpdate();
					if(count==0) {
						MainController.callAlert("수정 쿼리 실패 : 수정 쿼리문이 실패했습니다.");
						return count;
					}
				} catch (SQLException e) {
					MainController.callAlert("수정 실패 : 데이타베이스 수정이 실패했습니다.");
				} finally {
					//7.6 자원객체를 닫아준다.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("자원 닫기 실패 : psmt, con 닫기 실패.");
					}
				}
		return count;
	}
	//8 데이터베이스에서 markImage를 가져오는 함수
	public static String getBookMarkImageData(int no){
		String mark =null;
				String selectBook = "select markimage from booktbl where no = "+no;
				//8.2 데이타베이스 Connection을 가져와야 한다.
				Connection con = null;
				//8.3 쿼리문을 실행해야할 Statement를 만들어야한다.
				PreparedStatement psmt=null;
				//8.4 쿼리문을 실행하고나서 가져와야할 레코드를 담고있는 보자기 객체
				ResultSet rs = null;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(selectBook);
					//8.5 실제데이타를 연결한 쿼리문을 실행한다.(번개를 치는 것)
					rs = psmt.executeQuery();
					if(rs==null) {
						MainController.callAlert("select 실패 : select 쿼리문이 실패했습니다.");
						return null;
					}
					while(rs.next()) {
						mark = rs.getString("markImage");
					}
				} catch (SQLException e) {
					MainController.callAlert("삽입 실패 : 데이타베이스 삽입이 실패했습니다.");
				} finally {
					//8.6 자원객체를 닫아준다.
					try {
						if(psmt != null) { psmt.close(); }
						if(con != null) { con.close(); }
					} catch (SQLException e) { 
						MainController.callAlert("자원 닫기 실패 : psmt, con 닫기 실패.");
					}
				}
		
		return mark;
	}
}
