package fr.trxyy.alternative.alternative_api_uiv3.components;

import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.trxyy.alternative.alternative_apiv3.base.GameEngine;
import fr.trxyy.alternative.alternative_apiv3.base.ResourceLocation;
import fr.trxyy.alternative.alternative_apiv3.utils.FontLoader;
import fr.trxyy.test.Main;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * @author Trxyy
 */
public class LauncherAlert {
	
	private static double xOffset = 0;
	private static double yOffset = 0;
	public static Interpolator easeCapCut = Interpolator.SPLINE(0.30, 1, 0.5, 1);
	private FontLoader fontLoader = new FontLoader();
	public GameEngine gameEngine = new Main().GAME_ENGINE;

	/**
	 * The Constructor
	 * @param text The message to display
	 * @param type The Type of the Alert
	 */
	public LauncherAlert(String text, AlertType type) {
		try {
			this.displayCustomAlert("Error", text);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The Constructor
	 * @param title The title of the Alert
	 * @param text The message to display
	 */
	public LauncherAlert(String title, String text) {
		try {
			this.displayCustomAlert(title, text);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void displayCustomAlert(String title, String message) throws FontFormatException, IOException {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setTitle(title);

		VBox root = new VBox(15);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(20));
		root.setStyle(
			"-fx-background-color: rgba(50,50,50,0.9);" +
			"-fx-background-radius: 12;" +
			"-fx-border-radius: 12;" +
			"-fx-border-color: #ff5555;" +
			"-fx-border-width: 2;"
		);

		Label msg = new Label(message);
		msg.setTextFill(Color.WHITE);
		/** Dùng FontLoader (AWT Bridge) để hỗ trợ I18n tốt nhất */
		msg.setFont(fontLoader.toFXFont(fontLoader.loadSystemFontAWT("Segoe UI", 14f)));
		msg.setWrapText(true);

		Button ok = new Button("OK");
		ok.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white; -fx-background-radius: 8;");
		ok.setOnAction(e -> {
			// Fade out khi đóng
            FadeTransition ftOut = new FadeTransition(Duration.millis(200), root);
            Timeline zoomOut = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(root.opacityProperty(), 1),
                            new KeyValue(root.scaleXProperty(), 1),
                            new KeyValue(root.scaleYProperty(), 1)
                    ),
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(root.opacityProperty(), 0, easeCapCut),
                            new KeyValue(root.scaleXProperty(), 0.2, easeCapCut),
                            new KeyValue(root.scaleYProperty(), 0.2, easeCapCut)
                    )
            );
            ftOut.setFromValue(1.0);
            ftOut.setToValue(0.0);
            ftOut.setOnFinished(ev -> stage.close());
            ftOut.play();
            zoomOut.play();
		});

		root.getChildren().addAll(msg, ok);

		root.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});
		root.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - xOffset);
			stage.setY(event.getScreenY() - yOffset);
		});

		Main mainIn = new Main();
		
		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		if (mainIn.titleIcon != null) this.setIconImage(stage, mainIn.titleIcon);

		root.setOpacity(0.0);
		stage.show();
		
		/** Hiệu ứng Zoom-in đặc trưng của PMG Team */
		Timeline zoomIn = new Timeline(
				new KeyFrame(Duration.ZERO,
                        new KeyValue(root.opacityProperty(), 0),
                        new KeyValue(root.scaleXProperty(), 0.2),
                        new KeyValue(root.scaleYProperty(), 0.2)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(root.opacityProperty(), 1, easeCapCut),
                        new KeyValue(root.scaleXProperty(), 1, easeCapCut),
                        new KeyValue(root.scaleYProperty(), 1, easeCapCut)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(root.opacityProperty(), 1),
                        new KeyValue(root.scaleXProperty(), 0.94, Interpolator.EASE_IN),
                        new KeyValue(root.scaleYProperty(), 0.94, Interpolator.EASE_IN)
                )
        );
		zoomIn.play();
		
		/** Tự động lưu trạng thái nếu có module nào được kích hoạt từ alert này [cite: 2026-01-02] */
	}
	
	private Image loadImage(String imagePath) {
	    try {
	        // Thay vì dùng engine.getLauncherPreferences(), hãy load trực tiếp từ resources
	        return new Image(getClass().getResourceAsStream("/resources/" + imagePath));
	    } catch (Exception e) {
	        // Nếu không có ảnh, trả về null thay vì crash
	        return null; 
	    }
	}
	
	public void setIconImage(Stage primaryStage, String imgName) {
		Image img = loadImage(imgName);
		primaryStage.getIcons().add(img);
	}
}