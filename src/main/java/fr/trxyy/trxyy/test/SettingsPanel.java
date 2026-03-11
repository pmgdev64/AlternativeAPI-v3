package fr.trxyy.test;

import fr.trxyy.alternative.alternative_apiv3.base.GameEngine;
import fr.trxyy.alternative.alternative_apiv3.settings.GameInfos;
import fr.trxyy.alternative.alternative_apiv3.settings.GameSaver;
import fr.trxyy.alternative.alternative_apiv3.utils.FontLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SettingsPanel extends Pane {

    private GameEngine engine;
    private Font customFont;
    private FontLoader fontLoader = new FontLoader();

    public SettingsPanel(GameEngine engine) {
        this.engine = engine;
        // Thiết lập kích thước và background (giống với Swing cũ của bạn)
        this.setPrefSize(630, 210);
        this.setStyle("-fx-background-color: rgba(84, 89, 87, 0.8);");

        // Load Font
        try {
            // Chuyển font từ AWT sang JavaFX Font
            this.customFont = fontLoader.toFXFont(fontLoader.loadSystemFontAWT("Comic Relief", 12));
        } catch (Exception e) {
            this.customFont = Font.font("Arial", 12);
        }

        // Đọc cấu hình cũ
        GameSaver saver = new GameSaver(engine);
        GameInfos savedInfos = saver.readConfig();

        // 1. Tiêu đề
        Label titleLabel = new Label("Paramètres du launcher");
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setFont(new Font(customFont.getName(), 35));
        titleLabel.setLayoutX(17);
        titleLabel.setLayoutY(5);

        // 2. Resolution Label & Field
        Label resolutionLabel = new Label("Resolution (WxH)");
        resolutionLabel.setTextFill(Color.BLACK);
        resolutionLabel.setFont(new Font(customFont.getName(), 20));
        resolutionLabel.setLayoutX(17);
        resolutionLabel.setLayoutY(40);

        TextField resolutionField = new TextField(savedInfos.getResolution());
        resolutionField.setPrefSize(130, 20);
        resolutionField.setLayoutX(20);
        resolutionField.setLayoutY(80);
        resolutionField.setAlignment(Pos.CENTER);
        resolutionField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 0;");

        // 3. VM Arguments Label & Field
        Label vmLabel = new Label("Arguments VM");
        vmLabel.setTextFill(Color.BLACK);
        vmLabel.setFont(new Font(customFont.getName(), 20));
        vmLabel.setLayoutX(20);
        vmLabel.setLayoutY(100);

        TextField vmField = new TextField(savedInfos.getVmArguments());
        vmField.setPrefSize(575, 20);
        vmField.setLayoutX(20);
        vmField.setLayoutY(140);
        vmField.setAlignment(Pos.CENTER);
        vmField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 0;");

        // 4. Save Button
        Button saveSettingsButton = new Button("Sauvegarder");
        saveSettingsButton.setPrefSize(150, 30);
        saveSettingsButton.setLayoutX(440);
        saveSettingsButton.setLayoutY(100);
        saveSettingsButton.setFont(new Font(customFont.getName(), 18));
        saveSettingsButton.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: black; -fx-background-radius: 0;");

        // Logic lưu cài đặt
        saveSettingsButton.setOnAction(e -> {
            GameInfos gameInfo = new GameInfos();
            gameInfo.setResolution(resolutionField.getText());
            gameInfo.setVmArguments(vmField.getText());
            GameSaver gameSaver = new GameSaver(gameInfo, this.engine);
            gameSaver.saveSettings();
            
            // Đóng cửa sổ Stage
            Stage stage = (Stage) saveSettingsButton.getScene().getWindow();
            stage.close();
        });

        // Thêm tất cả vào Pane
        this.getChildren().addAll(titleLabel, resolutionLabel, resolutionField, vmLabel, vmField, saveSettingsButton);
    }
}