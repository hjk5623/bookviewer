 package Controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import Model.Book;
import Model.BookDB;
import Model.SubBook;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable{
	public Stage mainStage;
	//main.fxml의 fx:id
	@FXML TableView<Book> b1TableView;
	@FXML ImageView b1ImageView;
	@FXML Button b1BtnRegister;
	@FXML Button b1BtnLook;
	@FXML Button b1BtnStory;
	@FXML Button b1BtnDrop;
	@FXML Button b1BtnChart;
	@FXML Button b1BtnEdit;
	@FXML Button b1BtnClose;
	@FXML Button b1BtnSearch;
	@FXML TextField b1TextSearch;
	//--------------------------------------
	private Book selectBook;				// 테이블뷰의 선택된 아이템을 저장할 변수
	private int selectBookIndex;			// 테이블뷰의 선택된 인덱스를 저장할 변수
	private SubBook selectSubBook;	// 보기창 테이블뷰의 선택된 아이템을 저장할 변수
	private File selectFile = null;			// 선택된 이미지파일을 저장할 변수
	private int count=0;						// 뷰어창에서 이미지숫자를 증가시키거나 감소시켜주는 변수
	private Book book=null;				// 메인 테이블뷰와 연결된 모델클래스
	private BookDB bookDB=null;		// 데이터베이스의 테이블과 연결된 모델클래스
	ArrayList<Book> dbArrayList;		// 데이터베이스의 테이블뷰 정보를 가지고  있는 변수
	String[] firstImage = null;				// 이미지 이름을 스플릿으로 나눈 값을 저장해줄 변수
	int countPage=0;							// 데이터베이스에 저장된 북마크정보에서 폴더숫자와 페이지숫자를 나눠서 저장해주는 변수
	int countFolder=0; 
	int folderNum=0;							// 보기창 테이블뷰의 리스트숫자를 저장해주는 변수
	int firstNumber = 0;						// 첫회보기를 눌렀을때 첫번째 폴더를 열기위해 1을 저장해주는 변수
	String fileName="file:///D:/";			// 파일 경로의 앞부분을 저장
	String imagePath="D:/comic/";		// 이미지 파일 경로의 앞부분을 저장
	private File imageDir = new File("C:/comic");											//이미지를 저장할 위치
	ObservableList<Book> b1ListDate= FXCollections.observableArrayList();			//메인 테이블뷰와 연결된 obsList
	ObservableList<String> regCmbGenreList= FXCollections.observableArrayList();	//콤보박스와 연결된 obsList(장르) 
	ObservableList<String> regCmbFinishList= FXCollections.observableArrayList();	//콤보박스와 연결된 obsList(완결여부)
 	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 
		//1. 테이블뷰 값 설정
		setT1TableView();
		//2. 등록창의 콤보박스 내용 저장
		setRegComboBox();
		//3. 등록버튼을 눌렀을때 처리하는 함수
		b1BtnRegister.setOnAction( e -> { handleBtnRegisterAction();  });
		//4. 테이블뷰 클릭했을때 처리하는 함수
		b1TableView.setOnMouseClicked(e -> { handleB1TableViewAction(e); });
		//5. 삭제버튼 눌렀을때 처리하는 함수
		b1BtnDrop.setOnAction(e -> { handleBtnDropAction();  });
		//6. 수정버튼 눌렀을때 처리하는 함수
		b1BtnEdit.setOnAction(e -> { handleBtnEditAction();  });
		//7. 닫기버튼 눌렀을때 처리하는 함수
		b1BtnClose.setOnAction(e -> { handleBtnCloseAction();  });
		//8. 검색버튼 눌렀을때 처리하는 함수
		b1BtnSearch.setOnAction(e -> { handleBtnSearchAction();   });
		//9. 차트버튼 눌렀을때 처리하는 함수
		b1BtnChart.setOnAction(e -> { handleBtnChartAction();  });
		
	}

	//1. 테이블뷰 값 설정
	private void setT1TableView() {
		TableColumn tcNo = b1TableView.getColumns().get(0);
		tcNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		tcNo.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcTitle = b1TableView.getColumns().get(1);
		tcTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		tcTitle.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcWriter = b1TableView.getColumns().get(2);
		tcWriter.setCellValueFactory(new PropertyValueFactory<>("writer"));
		tcWriter.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcScore = b1TableView.getColumns().get(3);
		tcScore.setCellValueFactory(new PropertyValueFactory<>("score"));
		tcScore.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcGenre = b1TableView.getColumns().get(4);
		tcGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
		tcGenre.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcDate = b1TableView.getColumns().get(5);
		tcDate.setCellValueFactory(new PropertyValueFactory<>("mdate"));
		tcDate.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcState = b1TableView.getColumns().get(6);
		tcState.setCellValueFactory(new PropertyValueFactory<>("state"));
		tcState.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcFinish = b1TableView.getColumns().get(7);
		tcFinish.setCellValueFactory(new PropertyValueFactory<>("finish"));
		tcFinish.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcNumber = b1TableView.getColumns().get(8);
		tcNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
		tcNumber.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcImage = b1TableView.getColumns().get(9);
		tcImage.setCellValueFactory(new PropertyValueFactory<>("image"));
		tcImage.setStyle("-fx-alignment : CENTER;");
		
		b1TableView.setItems(b1ListDate);
		//DB에 있는 정보를 테이블뷰에 저장시킨다.
		dbArrayList = BookDAO.getBookTotalData();
		for(Book book  :  dbArrayList  ) {
			b1ListDate.add(book);
		}
	}
	//2. 등록창의 콤보박스 내용 저장, 초기 버튼 셋팅
	private void setRegComboBox() {
		regCmbFinishList.addAll("완결","연재");
		regCmbGenreList.addAll("스릴러","액션","소년","스포츠","판타지","드라마","멜로","코믹");
		b1BtnLook.setDisable(true);
		b1BtnStory.setDisable(true);
		b1BtnDrop.setDisable(true);
		b1BtnEdit.setDisable(true);
	}
	//3. 등록버튼을 눌렀을때 처리하는 함수
	private void handleBtnRegisterAction() {
		try {
			Stage registerStage = new Stage();
			registerStage.initModality(Modality.WINDOW_MODAL);
			registerStage.initOwner(mainStage);
			FXMLLoader loader= new FXMLLoader(getClass().getResource("../View/register.fxml"));		
			Parent root = loader.load();
			
			TextField regTextNo = (TextField)root.lookup("#regTextNo");
			TextField regTextWriter = (TextField)root.lookup("#regTextWriter");
			TextField regTextImage = (TextField)root.lookup("#regTextImage");
			TextField regTextState = (TextField)root.lookup("#regTextState");
			TextField regTextTitle = (TextField)root.lookup("#regTextTitle");
			TextField regTextScore = (TextField)root.lookup("#regTextScore");
			TextField regTextNumber = (TextField)root.lookup("#regTextNumber");
			TextArea regTextStory = (TextArea)root.lookup("#regTextStory");
			ComboBox regCmbGenre = (ComboBox)root.lookup("#regCmbGenre");
			ComboBox regCmbFinish = (ComboBox)root.lookup("#regCmbFinish");
			DatePicker  regDatePickerDate = (DatePicker)root.lookup("#regDatePickerDate");
			ImageView regImageView = (ImageView)root.lookup("#regImageView");
			Button regBtnImage = (Button)root.lookup("#regBtnImage");
			Button regBtnSave = (Button)root.lookup("#regBtnSave");
			Button regBtncancle = (Button)root.lookup("#regBtncancle");
			registerStage.setTitle("Register");
			regImageView.setImage(new Image(getClass().getResource("../images/image.jpg").toString()));
			//이미지 텍스트필들에 값을 직접 입력 못하도록 설정
			regTextImage.setEditable(false);	
			regCmbFinish.setItems(regCmbFinishList);
			regCmbGenre.setItems(regCmbGenreList);
			//등록번호를 숫자 5자리로 제한
			inputDecimalFormat(regTextNo);
			//저장버튼 눌렀을때 테이블뷰의 값을 저장.
			regBtnSave.setOnAction(e -> {
				try {
					//제목, 작가, 나라, 이미지, 줄거리가 입력되지 않으면 알람창을 띄우고 리턴한다.
					if(regTextTitle.getText().equals("") || regTextWriter.getText().equals("") || regTextState.getText().equals("") || regTextImage.getText().equals("") || regTextStory.getText().equals("") ) {
						callAlert("오류 발생 : 모든 항목을 입력하세요."); 
						return;
					}
					//테이블뷰와 연결된 모델클래스
					book=new Book(Integer.parseInt(regTextNo.getText()), regTextTitle.getText().toString(),
							regTextWriter.getText(), Double.parseDouble(regTextScore.getText()),
							regCmbGenre.getValue().toString(), Date.valueOf(regDatePickerDate.getValue().toString()), regTextState.getText(),
							regCmbFinish.getValue().toString(), Integer.parseInt(regTextNumber.getText()), regTextImage.getText());
					registerStage.close();
					//데이터베이스 테이블과 연결된 모델클래스
					bookDB=new BookDB(Integer.parseInt(regTextNo.getText()), regTextTitle.getText(),
							regTextWriter.getText(), Double.parseDouble(regTextScore.getText()),
							regCmbGenre.getValue().toString(), Date.valueOf(regDatePickerDate.getValue().toString()), regTextState.getText(),
							regCmbFinish.getValue().toString(), Integer.parseInt(regTextNumber.getText()), regTextImage.getText(),
							regTextStory.getText(),null);
					registerStage.close();
					//저장버튼을 눌렀을때 테이블뷰와 데이터베이스에 저장시켜준다.
					b1ListDate.add(book);
					int count = BookDAO.insertBookData(bookDB);
					if(count!=0) {
						callAlert("등록 성공 :"+regTextTitle.getText()+ "책이 등록 되었습니다.");
					}					
				}catch (NumberFormatException e1) {
					callAlert("등록 실패 : 등록번호, 평점, 권수를 입력하세요.");
				}catch (NullPointerException e1) {
					callAlert("등록 실패 : 모든 항목을 입력하세요.");
				}catch (Exception e1) { }
			});
			//이미지 저장버튼 눌렀을때 이미지 선택후 저장하고 이미지 이름을 텍스트이미지에 나타낸다.
			regBtnImage.setOnAction(e -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().add(new ExtensionFilter("Image File","*.png","*.jpg","*.gif"));
				selectFile = fileChooser.showOpenDialog(mainStage);
				String localURL=null;
				if(selectFile != null) {
					try {
						localURL = selectFile.toURI().toURL().toString();
					} catch (MalformedURLException e1) { }
				}
				//선택된 이미지 파일을 이미지뷰에 나타내고 이미지텍스트필드에 이미지 이름을 써준다.
				regImageView.setImage(new Image(localURL, false));
				regTextImage.setText(selectFile.getName());
			});
			//닫기 버튼 눌렀을때 처리하는 함수
			regBtncancle.setOnAction(e -> { registerStage.close(); });
	
			Scene scene= new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../application/register.css").toString());
			registerStage.setScene(scene);
			registerStage.show();
		} catch (IOException e) {}
		
	}
	//4. 테이블뷰 클릭했을때 처리하는 함수
	private void handleB1TableViewAction(MouseEvent e) {
		//테이블뷰 선택아이템과 인덱스를 저장
		selectBook = b1TableView.getSelectionModel().getSelectedItem();
		selectBookIndex = b1TableView.getSelectionModel().getSelectedIndex();
		//이미지 이름에서 책 제목만 가져온다.(환타지스타.JPG => 환타지스타)
		firstImage= selectBook.getImage().split("\\.");
		if(e.getClickCount()==1) {
			b1BtnLook.setDisable(false);
			b1BtnStory.setDisable(false);
			b1BtnDrop.setDisable(false);
			b1BtnEdit.setDisable(false);
			b1ImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+selectBook.getImage()));
			// 줄거리 버튼 눌렀을때 처리
			b1BtnStory.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						Stage storyStage = new Stage();
						storyStage.initModality(Modality.WINDOW_MODAL);
						storyStage.initOwner(mainStage);
						FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/story.fxml"));
						Parent root = loader.load();
						
						Label storyLabelTitle = (Label)root.lookup("#storyLabelTitle");
						TextArea storyTextArea = (TextArea)root.lookup("#storyTextArea");
						Button storyBtnReg = (Button)root.lookup("#storyBtnReg");
						ImageView storyImageView = (ImageView)root.lookup("#storyImageView");
						storyImageView.setImage(new Image(getClass().getResource("../images/story.jpg").toString()));
						storyStage.setTitle("Story");
						
						storyTextArea.setEditable(false);
						
						storyLabelTitle.setText(selectBook.getTitle());
						//데이터베이스에 있는 줄거리를 해당 등록번호를 이용해서 가져온다.
						storyTextArea.setText(BookDAO.getBookStoryData(selectBook.getNo()));
						storyBtnReg.setOnAction(e -> storyStage.close() );
						
						Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("../application/story.css").toString());
						storyStage.setScene(scene);
						storyStage.show();
					}catch (IOException e1) { }
				}
			});
			// 읽기 버튼 눌렀을때 처리
			b1BtnLook.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						Stage lookStage = new Stage();
						lookStage.initModality(Modality.WINDOW_MODAL);
						lookStage.initOwner(mainStage);
						FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/look.fxml"));
						Parent root = loader.load();
						
						TableView<SubBook> lookTableView= (TableView)root.lookup("#lookTableView");
						ImageView lookImageView = (ImageView)root.lookup("#lookImageView");
						Button lookBtnFirst = (Button)root.lookup("#lookBtnFirst");
						Button lookBtnAgain = (Button)root.lookup("#lookBtnAgain");
						Button lookBtnClose = (Button)root.lookup("#lookBtnClose");
						lookStage.setTitle(selectBook.getTitle());
						// 보기창의 테이블뷰와 SubBook모델클래스를 연결한다.
						TableColumn tcList= lookTableView.getColumns().get(0);
						tcList.setCellValueFactory(new PropertyValueFactory<>("list"));
						tcList.setStyle("-fx-alignment : CENTER;");
						
						TableColumn tcSubImage= lookTableView.getColumns().get(1);
						tcSubImage.setCellValueFactory(new PropertyValueFactory<>("subImage"));
						tcSubImage.setStyle("-fx-alignment : CENTER;");
						
						// 읽기창의 테이블뷰 값을 저장하는 list
						ObservableList<SubBook> lookList= FXCollections.observableArrayList();
						// 일릭창의 테이블뷰 이름과 이미지를 순서대로 입력하는 로직
						for(int i=0;i<selectBook.getNumber();i++) {
								lookList.add(new SubBook((1+i)+"",firstImage[0]+"_"+(1+i)+"_0.jpg" ));														
						}
						lookTableView.setItems(lookList);
						//읽기창에서 첫회보기 버튼 눌렀을때 처리하는 함수
						lookBtnFirst.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								count=0;
								try {
									Stage viewerStage = new Stage();
									viewerStage.initModality(Modality.WINDOW_MODAL);
									viewerStage.initOwner(lookStage);
									FXMLLoader viewerLoader= new FXMLLoader(getClass().getResource("../View/viewer.fxml"));
									Parent viewerRoot = viewerLoader.load();
									ImageView viewerImageView= (ImageView)viewerRoot.lookup("#viewerImageView");
									ImageView viewerImageBack = (ImageView)viewerRoot.lookup("#viewerImageBack");
									ImageView viewerImageNext = (ImageView)viewerRoot.lookup("#viewerImageNext");
									Label viewerLabelBack = (Label)viewerRoot.lookup("#viewerLabelBack");
									Label viewerLabelNext = (Label)viewerRoot.lookup("#viewerLabelNext");
									TextField viewerTextfield= (TextField)viewerRoot.lookup("#viewerTextfield");
									Button viewerBtnBack= (Button)viewerRoot.lookup("#viewerBtnBack");
									Button viewerBtnNext= (Button)viewerRoot.lookup("#viewerBtnNext");
									Button viewerBtnMark= (Button)viewerRoot.lookup("#viewerBtnMark");
									Button viewerBtnImage= (Button)viewerRoot.lookup("#viewerBtnImage");
									Button viewerBtnClose= (Button)viewerRoot.lookup("#viewerBtnClose");
									viewerImageNext.setImage(new Image(fileName+"오른쪽화살표.jpg"));
									viewerImageBack.setImage(new Image(fileName+"왼쪽화살표.jpg"));
									// 첫회보기를 클릭하면 항상 첫폴더를 보여주기 위한 변수
									firstNumber = 1;
									viewerStage.setTitle(selectBook.getTitle()+" "+firstNumber+"권");
									// 뷰어창에서 현재 페이지 정보와 이전페이지 다음페이지 숫자를 설정해준다.
									viewerTextfield.setText(0+"");											
									viewerLabelBack.setText("");
									viewerLabelNext.setText(1+"");
									// 뷰어창에서 텍스트필드에 숫자 입력후 엔터치면 해당 페이지로 이동 (없는 페이지일경우 알림을 띄우고 전 페이지로 돌아간다.)
									viewerTextfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
										@Override
										public void handle(KeyEvent event) {
											if(event.getCode().equals(KeyCode.ENTER)){
												int textCount=Integer.parseInt(viewerTextfield.getText());	// 텍스트필드 값을 int로 저장한다.													
												try {
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+textCount+".jpg"));												
													viewerImageView.imageProperty().get().impl_getPlatformImage().toString(); // 해당 페이지가 있으면 페이지 정보를 주고 없으면 예외가 발생한다.
													viewerTextfield.setText(textCount+"");
													viewerLabelNext.setText((textCount+1)+"");														
													if(!(textCount==0)) {
														viewerLabelBack.setText((textCount-1)+"");
													}else {
														viewerLabelBack.setText("");
													}
												}catch(Exception e) {													
													callAlert("오류 : 없는 페이지입니다."); // 예외가 발생했을때 전이미지로 돌려주고 리턴한다. 
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));
													viewerTextfield.setText(count+"");													
													viewerLabelNext.setText((count+1)+"");
													if(!viewerTextfield.getText().equals("0")) {
														viewerLabelBack.setText((count-1)+"");												
													}else {
														viewerLabelBack.setText("");											
													}
													return;
												}
												count=textCount; // 해당이미지가 잘 표시되면 해당 페이지수를 카운트에 저장해준다.
											}
										}
									});
									//뷰어창 띄울시 첫번쨰 페이지는 1권 0페이지를 띄운다.
									viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_0.jpg"));
									// 오른쪽 화살표 클릭할때마다 count를 증가시켜 이미지를 하나씩 교체해준다.
									viewerImageNext.setOnMouseClicked(e1 -> {
										count++;
										viewerTextfield.setText(count+"");
										viewerLabelBack.setText((count-1)+"");
										viewerLabelNext.setText((count+1)+"");
										viewerImageNext.setVisible(true);
										try {
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));
											System.out.println(viewerImageView.imageProperty().get().impl_getPlatformImage().toString());										
										}catch(Exception e) {
											callAlert("마지막 페이지 입니다. : 다음권으로  이동하세요");
											count--;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));
											viewerTextfield.setText(count+"");
											viewerLabelNext.setText((count+1)+"");
											viewerLabelBack.setText((count-1)+"");
											return;
										}
									});
									// 왼쪽 화살표 클릭할때마다 count를 감소시켜 이미지를 하나씩 교체해준다.
									viewerImageBack.setOnMouseClicked(e1 -> {
										count--;
										viewerTextfield.setText(count+"");											
										viewerLabelNext.setText((count+1)+"");
										if(!viewerTextfield.getText().equals("0")) {
											viewerLabelBack.setText((count-1)+"");												
										}else {
											viewerLabelBack.setText("");												
										}
										try {
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));	
											System.out.println(viewerImageView.imageProperty().get().impl_getPlatformImage().toString());										
										}catch(Exception e) {
											callAlert("첫페이지 입니다. : 다음권으로  이동하세요");
											count++;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));
											viewerTextfield.setText(count+"");											
											viewerLabelNext.setText((count+1)+"");
											if(!viewerTextfield.getText().equals("0")) {
												viewerLabelBack.setText((count-1)+"");												
											}else {
												viewerLabelBack.setText("");												
											}
											return;
										}									
									});				
										// 책갈피버튼을 누르면 처리하는 함수
									viewerBtnMark.setOnAction(new EventHandler<ActionEvent>() {
										
										@Override
										public void handle(ActionEvent event) {
											bookDB=new BookDB(selectBook.getNo(), selectBook.getTitle(), 
													selectBook.getWriter(), selectBook.getScore(),
													selectBook.getGenre(), selectBook.getMdate(), selectBook.getState(),
													selectBook.getFinish(), selectBook.getNumber(), selectBook.getImage(),
													null,firstNumber+"/"+count+"");
											//책갈비저장버튼 누를시 데이터베이스 markImage테이블에 현재 이미지를 다시 가져올 수 있도록 정보를 저장한다( 환타지스타1권28페이지 => 1/28  )
											int count1 = BookDAO.updateMarkImageData(bookDB);
											if(count1!=0) {			
												callAlert("북마크 저장 : "+selectBook.getTitle()+" "+firstNumber+"권 "+count+"쪽이 북마크 되었습니다.");
											}else {
												return;
											}
										}
									});
									// 이전화 버튼 눌렀을때 처리하는 함수
									viewerBtnBack.setOnAction(new EventHandler<ActionEvent>() {
										// 폴더값을 감소시키고 첫페이지부터 표시하기 위해 count를 0으로 만들어준다.
										@Override
										public void handle(ActionEvent event) {
											firstNumber--;
											if(firstNumber==0) {
												firstNumber++;
												callAlert("첫번째권입니다 : 첫회입니다.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+firstNumber+"권");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											count=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));		
										}
									});
									// 다음화 버튼 눌렀을때 처리하는 함수
									viewerBtnNext.setOnAction(new EventHandler<ActionEvent>() {
										// 폴더값을 증가시키고 첫페이지부터 표시하기 위해 count를 0으로 만들어준다.
										@Override
										public void handle(ActionEvent event) {
											firstNumber++;
											if(firstNumber>selectBook.getNumber()) {
												firstNumber--;
												callAlert("마지막권입니다 : 마지막회입니다.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+firstNumber+"권");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											count=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));		
										}
									});
									//이미지저장버튼 눌렀을때 처리하는 함수
									viewerBtnImage.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
												if(!imageDir.exists()) {
													imageDir.mkdir();	// 디렉토리가 생성이 안되어 있으면 폴더를 만든다.
												}
												FileInputStream fis=null;
												BufferedInputStream bis=null;
												FileOutputStream fos=null;
												BufferedOutputStream bos=null;
												//선택된 이미지를 c:/images/"선택된이미지이름명"으로 저장한다.
												try {
													fis= new FileInputStream(imagePath+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg");												
													bis= new BufferedInputStream(fis);													
													fos= new FileOutputStream(imageDir.getAbsolutePath()+"\\"+"book"+System.currentTimeMillis()+"_"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg");
													bos= new BufferedOutputStream(fos);														
													int data=-1;
													while((data = bis.read()) !=- 1) {
														bos.write(data);
														bos.flush();
													}
													callAlert("이미지저장 성공 : "+selectBook.getTitle()+" "+firstNumber+"권 "+count+"쪽 이미지가 C/comic 위치에 저장 되었습니다.");
												} catch (Exception e) { 
													callAlert("이미지저장 에러 : c/comic/저장파일 에러 점검바람");
												} finally {
													try {
														if(fis != null)  { fis.close(); }
														if(bis != null)  { bis.close(); }
														if(fos != null)  { fos.close(); }
														if(bos != null)  { bos.close(); }
													} catch (IOException e) { }
												}// end of finally
										}
									});
									viewerBtnClose.setOnAction(e1 -> { viewerStage.close(); });
									
								Scene scene = new Scene(viewerRoot);
								scene.getStylesheets().add(getClass().getResource("../application/viewer.css").toString());
								viewerStage.setScene(scene);
								viewerStage.show();
							} catch (IOException e1) { }
							}
						});	
						//보기창에서 이어보기 클릭했을때 처리하는 함수
						lookBtnAgain.setOnAction(new EventHandler<ActionEvent>() {
							
							@Override
							public void handle(ActionEvent event) {
								try {
									Stage viewerStage = new Stage();
									viewerStage.initModality(Modality.WINDOW_MODAL);
									viewerStage.initOwner(lookStage);
									FXMLLoader viewerLoader= new FXMLLoader(getClass().getResource("../View/viewer.fxml"));
									Parent viewerRoot = viewerLoader.load();
									ImageView viewerImageView= (ImageView)viewerRoot.lookup("#viewerImageView");
									ImageView viewerImageBack = (ImageView)viewerRoot.lookup("#viewerImageBack");
									ImageView viewerImageNext = (ImageView)viewerRoot.lookup("#viewerImageNext");
									Label viewerLabelBack = (Label)viewerRoot.lookup("#viewerLabelBack");
									Label viewerLabelNext = (Label)viewerRoot.lookup("#viewerLabelNext");
									TextField viewerTextfield= (TextField)viewerRoot.lookup("#viewerTextfield");
									Button viewerBtnBack= (Button)viewerRoot.lookup("#viewerBtnBack");
									Button viewerBtnNext= (Button)viewerRoot.lookup("#viewerBtnNext");
									Button viewerBtnMark= (Button)viewerRoot.lookup("#viewerBtnMark");
									Button viewerBtnImage= (Button)viewerRoot.lookup("#viewerBtnImage");
									Button viewerBtnClose= (Button)viewerRoot.lookup("#viewerBtnClose");
									viewerImageNext.setImage(new Image(fileName+"오른쪽화살표.jpg"));
									viewerImageBack.setImage(new Image(fileName+"왼쪽화살표.jpg"));
									// 데이터베이스에서 해당 등록번호의 markImage를 가져온다 (4/25) 
									String markString= BookDAO.getBookMarkImageData(selectBook.getNo());
									if(markString==null) {
										callAlert("책갈피 없음 : 저장된 페이지가 없습니다.");
										return;
									}		
									//폴더숫자와 페이지숫자를 나눠준다(4/25 => 4   25)
									String[] markImage=markString.split("/");
									//페이지를 넘길 수 있게 인태져로 바꿔준다.
									countPage = Integer.parseInt(markImage[1]);
									countFolder = Integer.parseInt(markImage[0]);
									viewerStage.setTitle(selectBook.getTitle()+" "+countFolder+"권");
									viewerTextfield.setText(countPage+"");
									viewerLabelBack.setText((countPage-1)+"");
									viewerLabelNext.setText((countPage+1)+"");
									// 뷰어창에서 텍스트필드에 숫자 입력후 엔터치면 해당 페이지로 이동 (없는 페이지일경우 알림을 띄우고 전 페이지로 돌아간다.)
									viewerTextfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
										@Override
										public void handle(KeyEvent event) {
											if(event.getCode().equals(KeyCode.ENTER)){
												int textCount=Integer.parseInt(viewerTextfield.getText());	// 텍스트필드 값을 int로 저장한다.													
												try { 
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+textCount+".jpg"));											
													viewerImageView.imageProperty().get().impl_getPlatformImage().toString();   // 해당 페이지가 있으면 페이지 정보를 주고 없으면 예외가 발생한다.
													viewerTextfield.setText(textCount+"");
													viewerLabelNext.setText((textCount+1)+"");
													if(!(textCount==0)) {
														viewerLabelBack.setText((textCount-1)+"");
													}else {
														viewerLabelBack.setText("");
													}
												}catch(Exception e) {  // 예외가 발생했을때 전이미지로 돌려주고 리턴한다.
													callAlert("페이지오류 : 없는 페이지 입니다.");
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));
													viewerTextfield.setText(countPage+"");													
													viewerLabelNext.setText((countPage+1)+"");
													if(!viewerTextfield.getText().equals("0")) {
														viewerLabelBack.setText((countPage-1)+"");												
													}else {
														viewerLabelBack.setText("");											
													}
													return;
												}
												countPage=textCount; // 페이지가 실제로 있는 경우 카운트에 입력한 값을 저장한다.
											}
										}
									});
									viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));	
									// 오른쪽 화살표 클릭할때마다 countPage를 증가시켜 이미지를 하나씩 교체해준다.
									viewerImageNext.setOnMouseClicked(e1 -> {
										countPage++;
										viewerTextfield.setText(countPage+"");
										viewerLabelBack.setText((countPage-1)+"");
										viewerLabelNext.setText((countPage+1)+"");
										try {
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));												
											viewerImageView.imageProperty().get().impl_getPlatformImage().toString();
										}catch(Exception e){
											callAlert("마지막 페이지 입니다. : 다음권으로  이동하세요");
											countPage--;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));
											viewerTextfield.setText(countPage+"");
											viewerLabelBack.setText((countPage-1)+"");
											viewerLabelNext.setText((countPage+1)+"");
											return;
										}
									});
									// 왼쪽 화살표 클릭할때마다 countPage를 감소시켜 이미지를 하나씩 교체해준다.
									viewerImageBack.setOnMouseClicked(e1 -> {
										countPage--;
										viewerTextfield.setText(countPage+"");										
										viewerLabelNext.setText((countPage+1)+"");
										if(!viewerTextfield.getText().equals("0")) {
											viewerLabelBack.setText((countPage-1)+"");												
										}else {
											viewerLabelBack.setText("");												
										}
										try {
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));																								
											viewerImageView.imageProperty().get().impl_getPlatformImage().toString();
										}catch(Exception e){
											callAlert("첫 페이지 : 첫 페이지 입니다.");
											countPage++;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));	
											viewerTextfield.setText(countPage+"");											
											viewerLabelNext.setText((countPage+1)+"");
											if(!viewerTextfield.getText().equals("0")) {
												viewerLabelBack.setText((countPage-1)+"");												
											}else {
												viewerLabelBack.setText("");												
											}
											return;	
										}
									});
									// 뷰어창에서 책갈피 버튼 클릭했을때 처리하는 함수
									viewerBtnMark.setOnAction(new EventHandler<ActionEvent>() {
										
										@Override
										public void handle(ActionEvent event) {
											bookDB=new BookDB(selectBook.getNo(), selectBook.getTitle(), 
													selectBook.getWriter(), selectBook.getScore(),
													selectBook.getGenre(), selectBook.getMdate(), selectBook.getState(),
													selectBook.getFinish(), selectBook.getNumber(), selectBook.getImage(),
													null,countFolder+"/"+countPage);
											//책갈비저장버튼 누를시 데이터베이스 markImage테이블에 현재 이미지를 다시 가져올 수 있도록 정보를 저장한다( 환타지스타1권28페이지 => 1/28  )
											int count1 = BookDAO.updateMarkImageData(bookDB);
											if(count1!=0) {			
												callAlert("북마크 저장 : "+selectBook.getTitle()+" "+countFolder+"권 "+countPage+"쪽이 북마크 되었습니다.");
											}else {
												return;
											}
										}
									});
									// 이전화 버튼 눌렀을때 처리하는 함수
									viewerBtnBack.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											countFolder--;
											if(countFolder==0) {
												countFolder++;
												callAlert("첫번째권입니다 : 첫회입니다.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+countFolder+"권");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											countPage=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));		
										}
									});
									// 다음화 버튼 눌렀을때 처리하는 함수
									viewerBtnNext.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											countFolder++;
											if(countFolder>selectBook.getNumber()) {
												countFolder--;
												callAlert("마지막권입니다 : 마지막회입니다.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+countFolder+"권");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											countPage=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));		
										}
									});
									//이미지저장버튼 눌렀을때 처리하는 함수
									viewerBtnImage.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
												if(!imageDir.exists()) {
													imageDir.mkdir();	// 디렉토리가 생성이 안되어 있으면 폴더를 만든다.
												}
												FileInputStream fis=null;
												BufferedInputStream bis=null;
												FileOutputStream fos=null;
												BufferedOutputStream bos=null;
												//선택된 이미지를 c:/images/"선택된이미지이름명"으로 저장한다.
												try {
													fis= new FileInputStream(imagePath+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg");
													bis= new BufferedInputStream(fis);
													fos= new FileOutputStream(imageDir.getAbsolutePath()+"\\"+"book"+System.currentTimeMillis()+"_"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg");
													bos= new BufferedOutputStream(fos);
													int data=-1;
													while((data = bis.read()) !=- 1) {
														bos.write(data);
														bos.flush();
													}
													callAlert("이미지저장 성공 : "+selectBook.getTitle()+" "+countFolder+"권 "+countPage+"쪽 이미지가 C/comic 위치에 저장 되었습니다.");
												} catch (Exception e) { 
													callAlert("이미지저장 에러 : c/comic/저장파일 에러 점검바람");
												} finally {
													try {
														if(fis != null)  { fis.close(); }
														if(bis != null)  { bis.close(); }
														if(fos != null)  { fos.close(); }
														if(bos != null)  { bos.close(); }
													} catch (IOException e) { }
												}// end of finally
											}
									});
									viewerBtnClose.setOnAction(e1 -> { viewerStage.close(); });
									
									Scene scene = new Scene(viewerRoot);
									scene.getStylesheets().add(getClass().getResource("../application/viewer.css").toString());
									viewerStage.setScene(scene);
									viewerStage.show();
								} catch (IOException e1) { }
							}
						});		
						
						// 보기창의 테이블뷰 셋팅(1번클릭=이미지가 셋팅, 2번클릭=viewer창을 띄움)
						lookTableView.setOnMouseClicked( e ->{
							//보기창에 테이블뷰 선택아이템을 저장
							selectSubBook= lookTableView.getSelectionModel().getSelectedItem();
							//테이블뷰 몇권인지를 int로 바꾼다.
							folderNum=Integer.parseInt(selectSubBook.getList());
							//클릭하때마다 count를 0으로 만들어서 뷰어창 띄울때마다 0페이지부터 보여준다.
							count=0;
							lookImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+selectSubBook.getSubImage()));
							if(e.getClickCount()==2) {
								try {
									Stage viewerStage = new Stage();
									viewerStage.initModality(Modality.WINDOW_MODAL);
									viewerStage.initOwner(lookStage);
									FXMLLoader viewerLoader= new FXMLLoader(getClass().getResource("../View/viewer.fxml"));
									Parent viewerRoot = viewerLoader.load();
									ImageView viewerImageView= (ImageView)viewerRoot.lookup("#viewerImageView");
									ImageView viewerImageBack = (ImageView)viewerRoot.lookup("#viewerImageBack");
									ImageView viewerImageNext = (ImageView)viewerRoot.lookup("#viewerImageNext");
									Label viewerLabelBack = (Label)viewerRoot.lookup("#viewerLabelBack");
									Label viewerLabelNext = (Label)viewerRoot.lookup("#viewerLabelNext");
									TextField viewerTextfield= (TextField)viewerRoot.lookup("#viewerTextfield");
									Button viewerBtnBack= (Button)viewerRoot.lookup("#viewerBtnBack");
									Button viewerBtnNext= (Button)viewerRoot.lookup("#viewerBtnNext");
									Button viewerBtnMark= (Button)viewerRoot.lookup("#viewerBtnMark");
									Button viewerBtnImage= (Button)viewerRoot.lookup("#viewerBtnImage");
									Button viewerBtnClose= (Button)viewerRoot.lookup("#viewerBtnClose");
									viewerStage.setTitle(selectBook.getTitle()+" "+folderNum+"권");
									viewerImageNext.setImage(new Image(fileName+"오른쪽화살표.jpg"));
									viewerImageBack.setImage(new Image(fileName+"왼쪽화살표.jpg"));
									viewerLabelBack.setText("");
									viewerLabelNext.setText((count+1)+"");
									viewerTextfield.setText(count+"");
									
									// 뷰어창에서 텍스트필드에 숫자 입력후 엔터치면 해당 페이지로 이동 (없는 페이지일경우 알림을 띄우고 전 페이지로 돌아간다.)
									viewerTextfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
										@Override
										public void handle(KeyEvent event) {
											if(event.getCode().equals(KeyCode.ENTER)){
												int textCount=Integer.parseInt(viewerTextfield.getText());	// 텍스트필드 값을 int로 저장한다.													
												try {  
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+textCount+".jpg"));
													viewerImageView.imageProperty().get().impl_getPlatformImage().toString();  // 해당 페이지가 있으면 페이지 정보를 주고 없으면 예외가 발생한다.
													viewerTextfield.setText(textCount+"");
													viewerLabelNext.setText((textCount+1)+"");
													if(!(textCount==0)) {
														viewerLabelBack.setText((textCount-1)+"");
													}else {
														viewerLabelBack.setText("");
													}
												}catch(Exception e){  // 예외가 발생했을때 전이미지로 돌려주고 리턴한다.
													callAlert("페이지오류 : 없는 페이지 입니다.");
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));
													viewerTextfield.setText(count+"");													
													viewerLabelNext.setText((count+1)+"");
													if(!viewerTextfield.getText().equals("0")) {
														viewerLabelBack.setText((count-1)+"");												
													}else {
														viewerLabelBack.setText("");											
													}
													return;
												}
												count=textCount; // 페이지가 실제로 있는 경우 카운트에 입력한 값을 저장한다.
											}
										}
									});
									//뷰어창 띄울시 첫번쨰 페이지는 0페이지를 띄운다.
									viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+selectSubBook.getSubImage()));	
									// 오른쪽 화살표 클릭할때마다 count를 증가시켜 이미지를 하나씩 교체해준다.
									viewerImageNext.setOnMouseClicked(e1 ->  {  
										count++;
										viewerTextfield.setText(count+"");
										viewerLabelBack.setText((count-1)+"");
										viewerLabelNext.setText((count+1)+"");
										try{
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));
											viewerImageView.imageProperty().get().impl_getPlatformImage().toString();																								
										}catch(Exception e2) {
											callAlert("마지막 페이지 입니다. : 다음권으로  이동하세요");
											count--;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));
											viewerTextfield.setText(count+"");
											viewerLabelBack.setText((count-1)+"");
											viewerLabelNext.setText((count+1)+"");
											return;											
										}										
									});
									// 왼쪽 화살표 클릭할때마다 count를 감소시켜 이미지를 하나씩 교체해준다.
									viewerImageBack.setOnMouseClicked(e1 -> {
										count--;
										viewerTextfield.setText(count+"");
										viewerLabelNext.setText((count+1)+"");										
										if(!viewerTextfield.getText().equals("0")) {
											viewerLabelBack.setText((count-1)+"");												
										}else {
											viewerLabelBack.setText("");											
										}
										try {
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));
											viewerImageView.imageProperty().get().impl_getPlatformImage().toString();																								
										}catch(Exception e2) {
											callAlert("첫 페이지 : 첫 페이지 입니다.");
											count++;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));
											viewerTextfield.setText(count+"");											
											viewerLabelNext.setText((count+1)+"");
											if(!viewerTextfield.getText().equals("0")) {
												viewerLabelBack.setText((count-1)+"");												
											}else {
												viewerLabelBack.setText("");												
											}
											return;											
										}									
									});
									// 이전화 버튼 눌렀을때 처리하는 함수
									viewerBtnBack.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											folderNum--;
											if(folderNum==0) {
												folderNum++;
												callAlert("첫번째권입니다 : 첫회입니다.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+folderNum+"권");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											count=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));		
										}
									});
									// 다음화 버튼 눌렀을때 처리하는 함수
									viewerBtnNext.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											folderNum++;
											if(folderNum>selectBook.getNumber()) {
												folderNum--;
												callAlert("마지막권입니다 : 마지막회입니다.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+folderNum+"권");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											count=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));		
										}
									});
									
									// 책갈피저장버튼을 누르면 처리하는 함수
									viewerBtnMark.setOnAction(new EventHandler<ActionEvent>() {
										
										@Override
										public void handle(ActionEvent event) {
											bookDB=new BookDB(selectBook.getNo(), selectBook.getTitle(), 
													selectBook.getWriter(), selectBook.getScore(),
													selectBook.getGenre(), selectBook.getMdate(), selectBook.getState(),
													selectBook.getFinish(), selectBook.getNumber(), selectBook.getImage(),
													null,folderNum+"/"+count);
											//책갈비저장버튼 누를시 데이터베이스 markImage테이블에 현재 이미지를 다시 가져올 수 있도록 정보를 저장한다( 환타지스타1권28페이지 => 1/28  )
											int count1 = BookDAO.updateMarkImageData(bookDB);
											if(count1!=0) {			
												callAlert("북마크 저장 : "+selectBook.getTitle()+" "+folderNum+"권 "+count+"쪽이 북마크 되었습니다.");
											}else {
												return;
											}
										}
									});				
									//이미지저장버튼 눌렀을때 처리하는 함수
									viewerBtnImage.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
												if(!imageDir.exists()) {
													imageDir.mkdir();	// 디렉토리가 생성이 안되어 있으면 폴더를 만든다.
												}
												FileInputStream fis=null;
												BufferedInputStream bis=null;
												FileOutputStream fos=null;
												BufferedOutputStream bos=null;
												//선택된 이미지를 c:/images/"선택된이미지이름명"으로 저장한다.
												try {
													fis= new FileInputStream(imagePath+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg");
													bis= new BufferedInputStream(fis);
													fos= new FileOutputStream(imageDir.getAbsolutePath()+"\\"+"book"+System.currentTimeMillis()+"_"+firstImage[0]+"_"+folderNum+"_"+count+".jpg");
													bos= new BufferedOutputStream(fos);
													int data=-1;
													while((data = bis.read()) !=- 1) {
														bos.write(data);
														bos.flush();
													}
													callAlert("이미지저장 성공 : "+selectBook.getTitle()+" "+folderNum+"권 "+count+"쪽 이미지가 C/comic 위치에 저장 되었습니다.");
												} catch (Exception e) { 
													callAlert("이미지저장 에러 : c/comic/저장파일 에러 점검바람");
												} finally {
													try {
														if(fis != null)  { fis.close(); }
														if(bis != null)  { bis.close(); }
														if(fos != null)  { fos.close(); }
														if(bos != null)  { bos.close(); }
													} catch (IOException e) { }
												}// end of finally
										}
									});
									viewerBtnClose.setOnAction(e1 -> { viewerStage.close(); });
									
									Scene scene = new Scene(viewerRoot);
									scene.getStylesheets().add(getClass().getResource("../application/viewer.css").toString());
									viewerStage.setScene(scene);
									viewerStage.show();
								} catch (IOException e1) { }
							}
						});
						lookBtnClose.setOnAction(e -> { lookStage.close(); });
						
						Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("../application/look.css").toString());
						lookStage.setScene(scene);
						lookStage.show();
					}catch (IOException e1) { }
				}
			});
		}	
	}
	//5. 삭제버튼 눌렀을때 처리하는 함수
	private void handleBtnDropAction() {
		//해당 등록번호를 이용해 db와 테이블뷰에서 해당 값을 제거
		int count = BookDAO.deleteBookData(selectBook.getNo());
		if(count!=0) {
			b1ListDate.remove(selectBookIndex);
			dbArrayList.remove(selectBook);
			callAlert("삭제완료 : "+selectBook.getTitle()+"책이 삭제되었습니다.");			
		}else {
			return;
		}	
	}
	//6. 수정버튼 눌렀을때 처리하는 함수
	private void handleBtnEditAction() {
		try {
			Stage editStage = new Stage();
			editStage.initModality(Modality.WINDOW_MODAL);
			editStage.initOwner(mainStage);
			FXMLLoader loader= new FXMLLoader(getClass().getResource("../View/register.fxml"));		
			Parent root = loader.load();
			
			TextField regTextNo = (TextField)root.lookup("#regTextNo");
			TextField regTextWriter = (TextField)root.lookup("#regTextWriter");
			TextField regTextImage = (TextField)root.lookup("#regTextImage");
			TextField regTextState = (TextField)root.lookup("#regTextState");
			TextField regTextTitle = (TextField)root.lookup("#regTextTitle");
			TextField regTextScore = (TextField)root.lookup("#regTextScore");
			TextField regTextNumber = (TextField)root.lookup("#regTextNumber");
			TextArea regTextStory = (TextArea)root.lookup("#regTextStory");
			ComboBox regCmbGenre = (ComboBox)root.lookup("#regCmbGenre");
			ComboBox regCmbFinish = (ComboBox)root.lookup("#regCmbFinish");
			DatePicker  regDatePickerDate = (DatePicker)root.lookup("#regDatePickerDate");
			ImageView regImageView = (ImageView)root.lookup("#regImageView");
			Button regBtnImage = (Button)root.lookup("#regBtnImage");
			Button regBtnSave = (Button)root.lookup("#regBtnSave");
			Button regBtncancle = (Button)root.lookup("#regBtncancle");
			editStage.setTitle("Edit");
			//수정창에서 권수, 완결여부, 평점만 입력가능하고 나머지는 수정 못하도록 설정
			regTextNo.setEditable(false);
			regTextWriter.setEditable(false);
			regTextImage.setEditable(false);
			regTextState.setEditable(false);
			regTextTitle.setEditable(false);
			regTextStory.setEditable(false);
			regDatePickerDate.setEditable(true);
			regCmbGenre.setEditable(false);
			regBtnImage.setVisible(false);
			//콤보박스 값을 셋팅
			regCmbFinish.setItems(regCmbFinishList);
			
			regTextNo.setText(selectBook.getNo()+"");
			regTextWriter.setText(selectBook.getWriter());
			regTextImage.setText(selectBook.getImage());
			regTextState.setText(selectBook.getState());
			regTextTitle.setText(selectBook.getTitle());
			regTextScore.setText(selectBook.getScore()+"");
			regTextNumber.setText(selectBook.getNumber()+"");
			regCmbGenre.setValue(selectBook.getGenre());
			regCmbFinish.setValue(selectBook.getFinish());
			regDatePickerDate.setValue(selectBook.getMdate().toLocalDate());
			regImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+selectBook.getImage()));
			//줄거리는 데이터베이스 테이블에서 가져온다.
			regTextStory.setText(BookDAO.getBookStoryData(selectBook.getNo())); 
			//저장버튼 눌렀을때 테이블뷰의 값을 저장.
			regBtnSave.setOnAction(e -> {
				try {
					// 수정된 값을 테이블뷰와 데이터베이스 테이블에도 적용시킨다.
					book=new Book(Integer.parseInt(regTextNo.getText()), regTextTitle.getText(), 
							regTextWriter.getText(), Double.parseDouble(regTextScore.getText()),
							regCmbGenre.getValue().toString(), Date.valueOf(regDatePickerDate.getValue().toString()), regTextState.getText(),
							regCmbFinish.getValue().toString(), Integer.parseInt(regTextNumber.getText()), regTextImage.getText());
					
					bookDB=new BookDB(Integer.parseInt(regTextNo.getText()), regTextTitle.getText(), 
							regTextWriter.getText(), Double.parseDouble(regTextScore.getText()),
							regCmbGenre.getValue().toString(), Date.valueOf(regDatePickerDate.getValue().toString()), regTextState.getText(),
							regCmbFinish.getValue().toString(), Integer.parseInt(regTextNumber.getText()), regTextImage.getText(),
							regTextStory.getText(),null);
					callAlert("수정완료 : "+selectBook.getTitle()+" 책이 수정되었습니다.");
					editStage.close();
					//수정된 값을 데이터베이스에 저장시킨다. => 쿼리문에서 변경가능하게 해놓은 값을 권수, 평점, 완결여부이다.
					int count = BookDAO.updateBookData(bookDB);  
					if(count!=0) {
						b1ListDate.set(selectBookIndex, book);					// list에서 현재 테이블뷰 선택된 객체를 수정해준다.
						int arrayIndex= dbArrayList.indexOf(selectBook);		// dbArrayList에서 수정할 객체를 현재 선택된 테이블뷰를 이용해서 인덱스를 가져온다
						dbArrayList.set(arrayIndex, book);							// 인덱스를 이용해서 dbArrayList를 수정해준다.
					}else {
						return;
					}					
				}catch (NumberFormatException e1) {
					callAlert("수정 실패 : 평점, 권수를 입력하세요.");
				} catch(Exception e1) { }
			});
			//닫기 버튼 눌렀을때 처리하는 함수
			regBtncancle.setOnAction(e -> { editStage.close(); });
			Scene scene= new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../application/edit.css").toString());
			editStage.setScene(scene);
			editStage.show();
		} catch(Exception e) { }
	}
	//7. 닫기버튼 눌렀을때 처리하는 함수
	private void handleBtnCloseAction() {
		Platform.exit();
	}
	//8. 검색버튼 눌렀을때 처리하는 함수
	private void handleBtnSearchAction() {
		// 검색기능 등록번호와 제목을가지고 검색한다.
		for(Book book : b1ListDate ) {
			if(b1TextSearch.getText().trim().equals(book.getNo()+"") || b1TextSearch.getText().trim().equals(book.getTitle())) {
				b1TableView.getSelectionModel().select(book);
				b1TableView.scrollTo(book);
				b1ImageView.setImage(new Image(fileName+"comic/"+book.getTitle()+"/"+book.getImage()));
			}
		}
	}
 	//9. 차트버튼 눌렀을때 처리하는 함수
	private void handleBtnChartAction() {
		try {
			Stage chartStage = new Stage();
			chartStage.initModality(Modality.WINDOW_MODAL);
			chartStage.initOwner(mainStage);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/chart.fxml"));
			Parent root = loader.load();
			
			BarChart t1BarChart = (BarChart)root.lookup("#t1BarChart");
			PieChart t2PieChart = (PieChart)root.lookup("#t2PieChart");
			Button t1BtnReg = (Button)root.lookup("#t1BtnReg");
			Button t2BtnReg = (Button)root.lookup("#t2BtnReg");
			chartStage.setTitle("Chart");
			//첫번째 tab => 평점별 barChart를 보여준다.
			XYChart.Series seriesScore = new  XYChart.Series<>();
			seriesScore.setName("평점");
			ObservableList scoreList= FXCollections.observableArrayList();
			for(int i=0; i<b1ListDate.size(); i++) { 
				scoreList.add(new XYChart.Data<>(b1ListDate.get(i).getTitle(),b1ListDate.get(i).getScore()));
				
			}
			seriesScore.setData(scoreList);
			t1BarChart.getData().add(seriesScore);
			//두번째 tab=> 장르별 count수를 파이차트로 보여준다. => 쿼리문을 이용해서 데이터베이스에서 장르별 count수를 가져온다.
			ObservableList GenreList= FXCollections.observableArrayList();
			GenreList.add(new PieChart.Data("스릴러",  BookDAO.getBookGenreData("스릴러")));
			GenreList.add(new PieChart.Data("액션",  BookDAO.getBookGenreData("액션")));
			GenreList.add(new PieChart.Data("소년",  BookDAO.getBookGenreData("소년")));
			GenreList.add(new PieChart.Data("스포츠",  BookDAO.getBookGenreData("스포츠")));
			GenreList.add(new PieChart.Data("판타지",  BookDAO.getBookGenreData("판타지")));
			GenreList.add(new PieChart.Data("드라마",  BookDAO.getBookGenreData("드라마")));
			GenreList.add(new PieChart.Data("멜로",  BookDAO.getBookGenreData("멜로")));
			GenreList.add(new PieChart.Data("코믹",  BookDAO.getBookGenreData("코믹")));
			
			t2PieChart.setData(GenreList);
			//닫기 버튼 눌렀을때 처리하는 함수
			t1BtnReg.setOnAction( e -> { chartStage.close(); });
			t2BtnReg.setOnAction( e -> { chartStage.close(); });
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../application/chart.css").toString());
			chartStage.setScene(scene);
			chartStage.show();
		} catch (IOException e) { }
	}
	//기타 : 알림창
	public static void callAlert(String contentText) {
		Alert alert= new Alert(AlertType.INFORMATION);
		alert.setTitle("알림창");
		alert.setHeaderText(contentText.substring(0, contentText.lastIndexOf(":")));
		alert.setContentText(contentText.substring(contentText.lastIndexOf(":")+1));
		alert.showAndWait();
	}
	//기타 : 해당 텍스트필드에 숫자 자릿수를 정해주는 함수
	private void inputDecimalFormat(TextField textField) {
		// 숫자만 입력(정수만 입력받음)
		DecimalFormat format = new DecimalFormat("#####");
		// 점수 입력시 길이 제한 이벤트 처리
		textField.setTextFormatter(new TextFormatter<>(event -> {  
			//입력받은 내용이 없으면 이벤트를 리턴함.  
			if (event.getControlNewText().isEmpty()) { return event; }
			//구문을 분석할 시작 위치를 지정함. 
			ParsePosition parsePosition = new ParsePosition(0);
			//입력받은 내용과 분석위치를 지정한지점부터 format 내용과 일치한지 분석함.
			Object object = format.parse(event.getControlNewText(), parsePosition); 
			//리턴값이 null 이거나, 입력한길이와 구문분석위치값이 적어버리면(다 분석하지못했음을 뜻함) 거나, 입력한길이가 6이면(5자리를 넘었음을 뜻함.) 이면 null 리턴함. 
			if (object == null || parsePosition.getIndex()<event.getControlNewText().length() || event.getControlNewText().length() == 6) {
				return null;    
			}else {
				return event;    
			}   
		}));
	}
}