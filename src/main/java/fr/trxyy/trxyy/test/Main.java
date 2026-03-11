package fr.trxyy.test;
	
import java.awt.FontFormatException;
import java.io.IOException;

import fr.trxyy.alternative.alternative_apiv3.base.GameConnect;
import fr.trxyy.alternative.alternative_apiv3.base.GameEngine;
import fr.trxyy.alternative.alternative_apiv3.base.GameFolder;
import fr.trxyy.alternative.alternative_apiv3.base.GameLinks;
import fr.trxyy.alternative.alternative_apiv3.base.LauncherBackground;
import fr.trxyy.alternative.alternative_apiv3.base.LauncherBase;
import fr.trxyy.alternative.alternative_apiv3.base.LauncherPane;
import fr.trxyy.alternative.alternative_apiv3.base.LauncherPreferences;
import fr.trxyy.alternative.alternative_apiv3.base.WindowStyle;
import fr.trxyy.alternative.alternative_apiv3.utils.Mover;
import fr.trxyy.alternative.alternative_authv3.base.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	public static GameFolder GAME_FOLDER = new GameFolder("minecraft2022-ar");
	private GameLinks GAME_LINKS = new GameLinks(
		    "https://piston-meta.mojang.com/v1/packages/da76e0a25ffccf2765f9e86ce61c063e44b2183b/", 
		    "1.12.json"
		);
	private LauncherPreferences LAUNCHER_PREFS = new LauncherPreferences("Launcher Template AlternativeAPI v3.0", 880, 520, Mover.MOVE);
	public GameEngine GAME_ENGINE = new GameEngine(GAME_FOLDER, GAME_LINKS, LAUNCHER_PREFS);
	private GameConnect GAME_CONNECT = new GameConnect("mc.hypixel.net", "25565");
	public String titleIcon = "favicon.png";
	
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(this.createContent());
		LauncherBase launcher = new LauncherBase(primaryStage, scene, StageStyle.TRANSPARENT, this.GAME_ENGINE);
		launcher.setIconImage(primaryStage, titleIcon);
	}

	private Parent createContent() throws FontFormatException, IOException {
		LauncherPane contentPane = new LauncherPane(this.GAME_ENGINE, 5, WindowStyle.TRANSPARENT);
		/** Direct Connect **/
		//this.GAME_ENGINE.reg(GAME_CONNECT);
		new LauncherBackground(this.GAME_ENGINE, "testgif.gif", contentPane);
		new Panel(contentPane, this.GAME_ENGINE);
		return contentPane;
	}
	
	public static void main(String[] args) {
		Logger.log("Fabric, Forge & Optifine works perfectly ! (Launcher)");
		Application.launch(args);
	}
}
