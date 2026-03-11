package fr.trxyy.alternative.alternative_api_uiv3.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * @author Trxyy
 */
public class LauncherImage extends ImageView {
	
	/**
	 * The Constructor
	 * @param root The Pane to add the image
	 */
	public LauncherImage(Pane root) {
		root.getChildren().add(this);
	}
	
	/**
	 * The Constructor
	 * @param root The Pane to add the image
	 * @param image The image
	 */
	// Giả sử đây là file LauncherImage.java của bạn
	public LauncherImage(Pane root, Image image) {
	    super(image); // Kế thừa từ javafx.scene.image.ImageView
	    root.getChildren().add(this);

	    // [CẢI TIẾN QUAN TRỌNG]: Luôn giữ tỷ lệ ảnh gốc
	    this.setPreserveRatio(true);
	}
	
	/**
	 * Set the size
	 * @param x The position X
	 * @param y The position Y
	 */
	public void setSize(int w, int h) {
		this.setFitWidth(w);
		this.setFitHeight(h);
	}

	/**
	 * Set the bounds
	 * @param x The position X
	 * @param y The position Y
	 */
	public void setBounds(int x, int y, int w, int h) {
		this.setLayoutX(x);
		this.setLayoutY(y);
		this.setFitWidth(w);
		this.setFitHeight(h);
	}

}
