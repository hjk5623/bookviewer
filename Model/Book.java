package Model;

import java.sql.Date;

public class Book {
	private int no;
	private String title;
	private String writer;
	private double score;
	private String genre;
	private Date mdate;
	private String state;
	private String finish;
	private int number;
	private String image;
	public Book(int no, String title, String writer, double score, String genre, Date mdate, String state, String finish,
			int number, String image) {
		super();
		this.no = no;
		this.title = title;
		this.writer = writer;
		this.score = score;
		this.genre = genre;
		this.mdate = mdate;
		this.state = state;
		this.finish = finish;
		this.number = number;
		this.image = image;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public Date getMdate() {
		return mdate;
	}
	public void setMdate(Date mdate) {
		this.mdate = mdate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getFinish() {
		return finish;
	}
	public void setFinish(String finish) {
		this.finish = finish;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	

}
