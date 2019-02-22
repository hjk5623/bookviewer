fileName="file:///D:/"
imagePath="D:/comic/"
//이미지 이름은 책제목_1_0 의 형식으로 시작하여야 한다.(2권 => 책제목_2_0)
//폴더 형식 -> 책제목(폴더) => 책제목1~책제목20(폴더) => 책제목_0_0(이미지파일)
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