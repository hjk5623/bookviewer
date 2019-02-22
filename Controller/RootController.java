package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RootController implements Initializable{
	public Stage primaryStage;
	@FXML private ImageView imageView;
	@FXML private TextField textId;
	@FXML private PasswordField textPassword;
	@FXML private Button btnLogin;
	@FXML private Button btnClose;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//id,pw 제대로 입력하고 엔터키나 로그인버튼 클릭시 새로운 창을 띄운다.
		imageView.setImage(new Image(getClass().getResource("../images/loginbook.jpg").toString()));
		textPassword.setOnKeyPressed(e -> { if(e.getCode().equals(KeyCode.ENTER)) {handlerBtnLoginAction();} });
		btnLogin.setOnAction(e ->  handlerBtnLoginAction() );
		//취소버튼 클릭시 창을 닫는다.
		btnClose.setOnAction(e -> Platform.exit() );
		//이미지 클릭시 아이디와 비밀번호를 자동으로 써준다.
		imageView.setOnMouseClicked( e -> { handleImageViewAction(e); });
	}
	//id,pw 제대로 입력하고 엔터키나 로그인버튼 클릭시 새로운 창을 띄운다.
	private void handlerBtnLoginAction() {
		if(!(textId.getText().equals("root") && textPassword.getText().equals("123456"))) {
			textId.clear();
			textPassword.clear();
			MainController.callAlert("로그인 실패 : 아이디와 패스워드를 확인해주세요.");
			return;
		}
		try {
			Stage mainStage= new Stage();
			FXMLLoader loader= new FXMLLoader(getClass().getResource("../View/main.fxml"));
			Parent root= loader.load();
			MainController mainController= loader.getController();
			mainController.mainStage=mainStage;
			MainController.callAlert("로그인 성공 : 로그인 성공하셨습니다.");
			mainStage.setTitle("BookList");
			Scene scene= new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../application/main.css").toString());
			mainStage.setScene(scene);
			primaryStage.close();
			mainStage.show();
			
		} catch (Exception e) {
			
		}
	}
	//이미지 클릭시 아이디와 비밀번호를 자동으로 써준다.
	private void handleImageViewAction(MouseEvent e) {
		textId.setText("root");
		textPassword.setText("123456");
	}
		
}