package fr.trxyy.alternative.alternative_apiv3.base;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import fr.trxyy.alternative.alternative_apiv3.minecraft.utils.OperatingSystem;
import fr.trxyy.alternative.alternative_apiv3.utils.Mover;
import fr.trxyy.alternative.alternative_authv3.base.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * @author Trxyy & PmgDev64
 */
public class LauncherBase {

	private static LauncherBase launcherBase;
	final Point dragDelta = new Point();
	public GameEngine gameEngine;

	public LauncherBase(final Stage stage, Scene scene, StageStyle style, GameEngine engine) {
		launcherBase = this;
		this.gameEngine = engine;

		/**
		 * QUAN TRỌNG: Ngăn chặn JavaFX tự động thoát khi ẩn Stage (Implicit Exit).
		 * Điều này cho phép Launcher hiện lại sau khi Minecraft tắt.
		 */
		Platform.setImplicitExit(false);

		if (OperatingSystem.getCurrentPlatform() == OperatingSystem.OSX || OperatingSystem.getCurrentPlatform() == OperatingSystem.LINUX || OperatingSystem.getCurrentPlatform() == OperatingSystem.SOLARIS) {
			Logger.log("Hệ điều hành: Mac/Linux/Solaris.");
		}
		
		engine.reg(stage);

		stage.initStyle(style);
		if (style.equals(StageStyle.TRANSPARENT)) {
			scene.setFill(Color.TRANSPARENT);
		}
		stage.setResizable(false);
		stage.setTitle(engine.getLauncherPreferences().getName());
		stage.setWidth(engine.getLauncherPreferences().getWidth());
		stage.setHeight(engine.getLauncherPreferences().getHeight());
		
		/** 1. KeyFrame MỞ (Open) */
		KeyFrame openFrame = new KeyFrame(Duration.seconds(1.0),
	            new KeyValue(scene.getRoot().opacityProperty(), 1.0),
	            new KeyValue(scene.getRoot().scaleXProperty(), 1.0),
	            new KeyValue(scene.getRoot().scaleYProperty(), 1.0)
	        );

		/** 2. Sự kiện đóng app (Nút X hệ thống) - Chỉ exit khi người dùng thực sự muốn đóng Launcher */
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                windowEvent.consume(); 
                KeyFrame closeFrame = new KeyFrame(Duration.seconds(0.5),
                    event -> { 
                    	Logger.log("[AAuth] Thoát hoàn toàn Launcher...");
                    	Platform.exit(); 
                    	System.exit(0); 
                    },
                    new KeyValue(scene.getRoot().opacityProperty(), 0.0),
                    new KeyValue(scene.getRoot().scaleXProperty(), 0.95),
                    new KeyValue(scene.getRoot().scaleYProperty(), 0.95)
                );
                setOnCloseAnimation(stage, scene, closeFrame);
            }
        });

		if (engine.getLauncherPreferences().isMovable().equals(Mover.MOVE)) {
			scene.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent mouseEvent) {
					dragDelta.x = (int) (stage.getX() - mouseEvent.getScreenX());
					dragDelta.y = (int) (stage.getY() - mouseEvent.getScreenY());
				}
			});
			scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent mouseEvent) {
					stage.setX(mouseEvent.getScreenX() + dragDelta.x);
					stage.setY(mouseEvent.getScreenY() + dragDelta.y);
				}
			});
		}
		
		stage.setScene(scene);
		this.displayCopyrights();
		
		/** 3. Hiển thị và chạy animation mở */
		stage.show();
		this.setOnOpenAnimation(stage, scene, openFrame);
	}
	
	public static LauncherBase getLauncherBase() {
		return launcherBase;
	}

	public void setOnOpenAnimation(Stage stage, Scene scene, KeyFrame frame) {
	    scene.getRoot().setOpacity(0.0);
	    scene.getRoot().setScaleX(0.98); // Bo góc và scale nhẹ mượt hơn trên G5400
	    scene.getRoot().setScaleY(0.98);

	    Timeline timeline = new Timeline();
	    timeline.getKeyFrames().add(frame);
	    timeline.play();
	}

	public void setOnCloseAnimation(Stage stage, Scene scene, KeyFrame frame) {
	    Timeline timeline = new Timeline();
	    timeline.getKeyFrames().add(frame);
	    timeline.play();
	}
	
	/** Đóng app từ các nút bấm tùy chỉnh trên UI */
	public static void handleCloseRequest(Stage stage, Scene scene) {
		KeyFrame defaultClose = new KeyFrame(Duration.seconds(0.5),
                event -> { 
                	Logger.log("[AAuth] Đang đóng Launcher...");
                	Platform.exit(); 
                	System.exit(0); 
                },
                new KeyValue(scene.getRoot().opacityProperty(), 0.0),
                new KeyValue(scene.getRoot().scaleXProperty(), 0.95),
                new KeyValue(scene.getRoot().scaleYProperty(), 0.95)
            );
		getLauncherBase().setOnCloseAnimation(stage, scene, defaultClose);
	}

	public void setIconImage(Stage primaryStage, String imgName) {
		Image img = loadImage(this.gameEngine, imgName);
		primaryStage.getIcons().add(img);
	}
	
	public Image loadImage(GameEngine engine, String image) {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(ResourceLocation.class.getResource(String.valueOf(engine.getLauncherPreferences().getResourceLocation()) + image));
		} catch (IOException e) {
			Logger.err("Không thể tải hình ảnh: " + image);
		}
		return SwingFXUtils.toFXImage(bufferedImage, null);
	}

	private void displayCopyrights() {
		Logger.log("You Are Running AlternativeAPI on Java " + System.getProperty("java.version"));
		Logger.log("========================================");
		Logger.log("|          AlternativeUpdater          |");
		Logger.log("|       Author: " + ApiConstants.getAuthor() + "       |");
		Logger.log("|            Version: " + ApiConstants.getVersion() + "            |");
		Logger.log("========================================");
	}
}