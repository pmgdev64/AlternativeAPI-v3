package fr.trxyy.test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import javax.swing.JDialog;
import javax.swing.JFrame;

import fr.trxyy.alternative.alternative_api_uiv3.components.LauncherAlert;
import fr.trxyy.alternative.alternative_api_uiv3.components.LauncherButton;
import fr.trxyy.alternative.alternative_api_uiv3.components.LauncherImage;
import fr.trxyy.alternative.alternative_api_uiv3.components.LauncherLabel;
import fr.trxyy.alternative.alternative_api_uiv3.components.LauncherPasswordField;
import fr.trxyy.alternative.alternative_api_uiv3.components.LauncherProgressBar;
import fr.trxyy.alternative.alternative_api_uiv3.components.LauncherTextField;
import fr.trxyy.alternative.alternative_apiv3.base.GameEngine;
import fr.trxyy.alternative.alternative_apiv3.base.IScreen;
import fr.trxyy.alternative.alternative_apiv3.base.LauncherBase;
import fr.trxyy.alternative.alternative_apiv3.language.I18n;
import fr.trxyy.alternative.alternative_apiv3.settings.UsernameSaver;
import fr.trxyy.alternative.alternative_apiv3.updater.GameUpdater;
import fr.trxyy.alternative.alternative_apiv3.utils.FontLoader;
import fr.trxyy.alternative.alternative_apiv3.utils.Mover;
import fr.trxyy.alternative.alternative_authv3.base.GameAuth;
import fr.trxyy.alternative.alternative_authv3.base.Session;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.awt.Font;
import java.awt.FontFormatException;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Panel extends IScreen {
	/** TOP */
	private LauncherButton closeButton;
	private LauncherButton reduceButton;
	/** LOGIN */
	private LauncherTextField usernameField;
	private LauncherPasswordField passwordField;
	private LauncherButton loginButton, microsoftButton, settingsButton;
	/** VERSION SELECT */
	private ComboBox<String> versionSelect;
	/** USERNAME SAVER */
	public UsernameSaver usernameSaver;
	/** GAMEENGINE, UPDATE */
	private GameEngine gameEngine;
	private LauncherProgressBar progressBar;
	private LauncherLabel updateLabel;
	private Rectangle updateRectangle;
	private Thread updateThread;
	private GameUpdater updater;
	/** LOGGED IN **/
	private Rectangle loggedRectangle;
	private LauncherImage headImage;
	private LauncherLabel accountLabel;
	/** SETTINGS **/
	private GameAuth gameAuth;
	private Session gameSession;
	private FontLoader fontLoader = new FontLoader();
	private Font customFont = fontLoader.setFont(new Font("Comic Relief", Font.PLAIN, 18), 18.0f);
	private I18n i18n;
	
	public Panel(Pane root, GameEngine engine) throws FontFormatException, IOException {
		this.gameEngine = engine;
		this.i18n = new I18n();
		this.usernameSaver = new UsernameSaver(engine);
		
		this.drawRect(root, 0, 0, gameEngine.getWidth(), gameEngine.getHeight(), Color.rgb(255, 255, 255, 0.10));
		/** ===================== RECTANGLE NOIR EN BAS ===================== */
		this.drawRect(root, 0, engine.getHeight() - 110, engine.getWidth(), 300, Color.rgb(0, 0, 0, 0.4));
		/** ===================== AFFICHER UN LOGO ===================== */
		this.drawImage(gameEngine, loadImage(gameEngine, "alternativeapi_logo.png"), 0, this.gameEngine.getHeight() - 100, 400, 100, root, Mover.DONT_MOVE);
		/** ===================== BOUTON FERMER ===================== */
		this.closeButton = new LauncherButton(root);
		this.closeButton.setInvisible();
		this.closeButton.setBounds(gameEngine.getWidth() - 50, -3, 40, 20);
		LauncherImage closeImage = new LauncherImage(root, loadImage(gameEngine, "close.png"));
		closeImage.setSize(40, 20);
		this.closeButton.setGraphic(closeImage);
		this.closeButton.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent event) {
		        Stage stage = (Stage) closeButton.getScene().getWindow();
		        Scene scene = closeButton.getScene();
		        
		        // Tự tạo một KeyFrame đóng "độc lạ" cho nút bấm
		        KeyFrame buttonCloseFrame = new KeyFrame(Duration.seconds(0.6),
		            e -> { System.exit(0); }, // Thoát khi chạy xong
		            new KeyValue(scene.getRoot().opacityProperty(), 0.0),
		            new KeyValue(scene.getRoot().translateYProperty(), 100), // Trượt xuống 100px
		            new KeyValue(scene.getRoot().rotateProperty(), 5)        // Xoay nhẹ 5 độ
		        );
		        
		        LauncherBase.getLauncherBase().setOnCloseAnimation(stage, scene, buttonCloseFrame);
		    }
		});
		/** ===================== BOUTON REDUIRE ===================== */
		this.reduceButton = new LauncherButton(root);
		this.reduceButton.setInvisible();
		this.reduceButton.setBounds(gameEngine.getWidth() - 91, -3, 40, 20);
		LauncherImage reduceImage = new LauncherImage(root, loadImage(gameEngine, "reduce.png"));
		reduceImage.setSize(40, 20);
		this.reduceButton.setGraphic(reduceImage);
		this.reduceButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Stage stage = (Stage) ((LauncherButton) event.getSource()).getScene().getWindow();
				stage.setIconified(true);
			}
		});
		/** ===================== Ô CHỌN PHIÊN BẢN ===================== */
		this.versionSelect = new ComboBox<String>();
		this.versionSelect.setPromptText("Chọn phiên bản");
		this.versionSelect.setItems(FXCollections.observableArrayList("1.20.1", "1.19.4", "1.16.5", "1.12.2", "1.8.9"));
		this.versionSelect.setLayoutX(this.gameEngine.getWidth() - 360);
		this.versionSelect.setLayoutY(this.gameEngine.getHeight() - 130);
		this.versionSelect.setPrefSize(220, 22);
		this.versionSelect.setStyle("-fx-background-color: rgb(230, 230, 230); -fx-text-fill: black; -fx-background-radius: 0;");
		root.getChildren().add(this.versionSelect);

		/** ===================== CASE PSEUDONYME ===================== */
		this.usernameField = new LauncherTextField(usernameSaver.getUsername(), root);
		this.usernameField.setBounds(this.gameEngine.getWidth() - 360, this.gameEngine.getHeight() - 100, 220, 20);
		this.setFontSize(14.0F);
		this.usernameField.setFont(fontLoader.toFXFont(this.customFont));
		this.usernameField.addStyle("-fx-background-color: rgb(230, 230, 230);");
		this.usernameField.addStyle("-fx-text-fill: black;");
		this.usernameField.addStyle("-fx-border-radius: 0 0 0 0;");
		this.usernameField.addStyle("-fx-background-radius: 0 0 0 0;");
		this.usernameField.setVoidText("Nom de compte");
		/** ===================== CASE MOT DE PASSE ===================== */
		this.passwordField = new LauncherPasswordField(root);
		this.passwordField.setBounds(this.gameEngine.getWidth() - 360, this.gameEngine.getHeight() - 65, 220, 20);
		this.setFontSize(14.0F);
		this.passwordField.setFont(fontLoader.toFXFont(this.customFont));
		this.passwordField.addStyle("-fx-background-color: rgb(230, 230, 230);");
		this.passwordField.addStyle("-fx-text-fill: black;");
		this.passwordField.addStyle("-fx-border-radius: 0 0 0 0;");
		this.passwordField.addStyle("-fx-background-radius: 0 0 0 0;");
		this.passwordField.setVoidText("Mot de passe (vide = crack)");
		/** ===================== BOUTON DE CONNEXION ===================== */
		this.loginButton = new LauncherButton("Se connecter", root);
		this.setFontSize(12.5F);
		this.loginButton.setFont(fontLoader.toFXFont(this.customFont));
		this.loginButton.setBounds(this.gameEngine.getWidth() - 130, this.gameEngine.getHeight() - 64, 105, 20);
		this.loginButton.addStyle("-fx-background-color: rgb(230, 230, 230);");
		this.loginButton.addStyle("-fx-text-fill: black;");
		this.loginButton.addStyle("-fx-border-radius: 0 0 0 0;");
		this.loginButton.addStyle("-fx-background-radius: 0 0 0 0;");
		this.loginButton.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent event) {
		        /** 1. Vô hiệu hóa nút để tránh người dùng click spam gây lặp log Wrote */
		        loginButton.setDisable(true);

		        /** 2. Kiểm tra phiên bản */
		        if (versionSelect.getValue() == null) {
		            new LauncherAlert("Erreur", "Vui lòng chọn một phiên bản Minecraft!");
		            loginButton.setDisable(false);
		            return;
		        }

		        String username = usernameField.getText();

		        /** 3. Kiểm tra Username */
		        if (username.length() < 3) {
		            new LauncherAlert("Lỗi", "Tên tài khoản phải có ít nhất 3 ký tự.");
		            loginButton.setDisable(false);
		            return;
		        }

		        /** 4. Lưu cấu hình - Chỉ gọi 1 lần duy nhất [cite: 2026-01-02] */
		        usernameSaver.saveSettings(username);

		        /** 5. Chạy logic Launch trong một luồng riêng (Thread) để không làm lag UI */
		        new Thread(() -> {
		            try {
		                // Lấy URL JSON
		                String jsonUrl = engine.getGameLinks().getJsonUrl();
		                System.out.println("DEBUG - JSON URL: " + jsonUrl);

		                if (jsonUrl == null || jsonUrl.isEmpty()) {
		                    javafx.application.Platform.runLater(() -> {
		                        new LauncherAlert("Lỗi khởi động", "Không tìm thấy URL cấu hình phiên bản.");
		                        loginButton.setDisable(false);
		                    });
		                    return;
		                }

		                // Thực hiện tải file JSON (IO Operation)
		                File jsonFile = downloadVersion(jsonUrl, engine);

		                if (jsonFile != null && jsonFile.exists()) {
		                    gameSession = new Session(username, UUID.randomUUID().toString(), UUID.randomUUID().toString());
		                    
		                    // Cập nhật UI và bắt đầu Update/Launch trên JavaFX Thread
		                    javafx.application.Platform.runLater(() -> {
		                        updateGame(gameSession, jsonFile);
		                    });
		                } else {
		                    javafx.application.Platform.runLater(() -> {
		                        new LauncherAlert("Lỗi khởi động", "Tải JSON thất bại hoặc file không tồn tại.");
		                        loginButton.setDisable(false);
		                    });
		                }
		            } catch (Exception e) {
		                e.printStackTrace();
		                javafx.application.Platform.runLater(() -> {
		                    new LauncherAlert("Lỗi nghiêm trọng", e.getMessage());
		                    loginButton.setDisable(false);
		                });
		            }
		        }).start();
		    }
		});
		/** ===================== BOUTON DES OPTIONS ===================== */
		this.settingsButton = new LauncherButton("Options", root);
		this.setFontSize(12.5F);
		this.settingsButton.setFont(fontLoader.toFXFont(this.customFont));
		this.settingsButton.setBounds(this.gameEngine.getWidth() - 130, this.gameEngine.getHeight() - 99, 105, 20);
		this.settingsButton.addStyle("-fx-background-color: rgb(230, 230, 230);");
		this.settingsButton.addStyle("-fx-text-fill: black;");
		this.settingsButton.addStyle("-fx-border-radius: 0 0 0 0;");
		this.settingsButton.addStyle("-fx-background-radius: 0 0 0 0;");
		this.settingsButton.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent event) {
		        // Tạo một Stage mới (Cửa sổ mới)
		        Stage settingsStage = new Stage();
		        settingsStage.setTitle("Modification des parametres");
		        settingsStage.initModality(Modality.APPLICATION_MODAL); // Thay thế cho setModal(true)
		        settingsStage.initOwner(gameEngine.getStage()); // Gắn chủ sở hữu là cửa sổ chính
		        settingsStage.setResizable(false);

		        // Chuyển SettingsPanel sang dạng Pane của JavaFX
		        // Pane settingsLayout = new SettingsPanelFX(gameEngine); 
		        // Scene settingsScene = new Scene(settingsLayout, 630, 210);
		        // settingsStage.setScene(settingsScene);
		        
		        settingsStage.show();
		    }
		});
		/** ===================== BOUTON DE CONNEXION MICROSOFT ===================== */
		this.microsoftButton = new LauncherButton("Connexion với Microsoft", root);
		this.setFontSize(12.5F);
		this.microsoftButton.setFont(fontLoader.toFXFont(this.customFont));
		this.microsoftButton.setBounds(this.gameEngine.getWidth() - 345, this.gameEngine.getHeight() - 33, 190, 20);
		this.microsoftButton.addStyle("-fx-background-color: rgb(230, 230, 230);");
		this.microsoftButton.addStyle("-fx-text-fill: black;");
		this.microsoftButton.addStyle("-fx-border-radius: 0 0 0 0;");
		this.microsoftButton.addStyle("-fx-background-radius: 0 0 0 0;");
		this.microsoftButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				gameAuth = new GameAuth();
				showMicrosoftAuth(gameEngine, gameAuth);
				if (gameAuth.isLogged()) {
					gameSession = gameAuth.getSession();
					File jsonFile = downloadVersion(engine.getGameLinks().getJsonUrl(), engine);
					updateGame(gameSession, jsonFile);
				}
			}
		});
		
		/** ======================================================== **/
		this.updateLabel = new LauncherLabel(root);
		this.updateLabel.setText("Mise a jour...");
		this.setFontSize(30.0F);
		this.updateLabel.setFont(fontLoader.toFXFont(this.customFont));
		this.updateLabel.setBounds(this.gameEngine.getWidth() - 250, this.gameEngine.getHeight() - 70, 230, 20);
		this.updateLabel.addStyle("-fx-text-fill: white;");
		this.updateLabel.setOpacity(0.0D);
		this.updateLabel.setVisible(false);
		
		this.loggedRectangle = this.drawRect(root, this.gameEngine.getWidth() / 2 - 115, 50, 230, 200, Color.rgb(0, 0, 0, 0.4));
		this.loggedRectangle.setOpacity(0.0D);
		this.loggedRectangle.setVisible(false);
		
		this.headImage = new LauncherImage(root);
		this.headImage.setFitWidth(120);
		this.headImage.setFitHeight(120);
		this.headImage.setLayoutX(this.gameEngine.getWidth() / 2 - 60);
		this.headImage.setLayoutY(70);
		this.headImage.setOpacity(0.0D);
		this.headImage.setVisible(false);
		
		this.accountLabel = new LauncherLabel(root);
		this.accountLabel.setAlignment(Pos.CENTER);
		this.setFontSize(20.0F);
		this.accountLabel.setFont(fontLoader.toFXFont(this.customFont));
		this.accountLabel.setBounds(this.gameEngine.getWidth() / 2 - 110, this.gameEngine.getHeight() / 2 - 50, 220, 20);
		this.accountLabel.addStyle("-fx-text-fill: white;");
		this.accountLabel.setOpacity(0.0D);
		this.accountLabel.setVisible(false);
		
		this.updateRectangle = this.drawRect(root, this.gameEngine.getWidth() / 2 - 250, this.gameEngine.getHeight() / 2 + 70, 500, 30, Color.rgb(0, 0, 0, 0.4));
		this.updateRectangle.setOpacity(0.0D);
		this.updateRectangle.setVisible(false);
		
		this.progressBar = new LauncherProgressBar(root);
		this.progressBar.setBounds(this.gameEngine.getWidth() / 2 - 245, this.gameEngine.getHeight() / 2 + 75, 490, 21);
		this.progressBar.setOpacity(0.0D);
		this.progressBar.setVisible(false);
	}

	private void updateGame(Session auth, File jsonFile) {        
	    this.accountLabel.setText(auth.getUsername());
	    
	    // 1. Chạy hiệu ứng chuyển cảnh UI
	    javafx.application.Platform.runLater(() -> {
	        // Gom nhóm các lệnh ẩn/hiện vào một chỗ để tránh lag UI Thread
	        loginButton.setVisible(false);
	        usernameField.setVisible(false);
	        passwordField.setVisible(false);
	        settingsButton.setVisible(false);
	        microsoftButton.setVisible(false);
	        versionSelect.setVisible(false);

	        updateLabel.setVisible(true);
	        progressBar.setVisible(true);
	        loggedRectangle.setVisible(true);
	        headImage.setImage(new Image("https://minotar.net/helm/" + auth.getUsername() + "/120.png"));
	        headImage.setVisible(true);
	        updateRectangle.setVisible(true);
	        accountLabel.setVisible(true);
	        
	        // Thêm hiệu ứng fadeIn nhẹ nhàng
	        fadeIn(updateLabel, 500);
	        fadeIn(progressBar, 500);
	    });

	    // 2. Chạy luồng cập nhật và khởi động game
	    this.updateThread = new Thread() {
	        public void run() {
	            try {
	                System.out.println("Đang chuẩn bị cập nhật game với JSON: " + jsonFile.getName());
	                
	                // Khởi tạo Updater
	                updater = new GameUpdater(prepareGameUpdate(updater, gameEngine, auth, jsonFile), gameEngine);
	                gameEngine.reg(updater);

	                // Timeline cập nhật Progress Bar (JavaFX Thread)
	                Timeline t = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
	                    if (gameEngine.getGameUpdater() != null) {
	                        double total = gameEngine.getGameUpdater().filesToDownload;
	                        double current = gameEngine.getGameUpdater().downloadedFiles;
	                        double percent = (total > 0) ? (current / total) : 0;
	                        
	                        progressBar.setProgress(percent);
	                        updateLabel.setText(gameEngine.getGameUpdater().getUpdateText());
	                    }
	                }));
	                t.setCycleCount(Animation.INDEFINITE);
	                t.play();

	                // BẮT ĐẦU CHẠY GAME
	                downloadGameAndRun(updater, auth);
	                
	            } catch (Exception e) {
	                e.printStackTrace();
	                javafx.application.Platform.runLater(() -> {
	                    new LauncherAlert("Lỗi Khởi Động", "Không thể khởi chạy Minecraft: " + e.getMessage());
	                    loginButton.setDisable(false);
	                    loginButton.setVisible(true); // Hiện lại nút để thử lại
	                });
	            }
	        }
	    };
	    this.updateThread.setName("Update-Thread");
	    this.updateThread.start();
	}
	
	public void setFontSize(float size) throws FontFormatException, IOException{
		this.customFont = fontLoader.loadSystemFontAWT("Comic Relief", size);
	}
}