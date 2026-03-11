package fr.trxyy.alternative.alternative_api_uiv3.components;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * @author Trxyy & PmgDev64
 */
public class LauncherButton extends Button {

	/**
	 * The Constructor
	 * @param root The Pane to add the button
	 */
	public LauncherButton(Pane root) {
		this.setupDefaultEvents();
		root.getChildren().add(this);
	}
	
	/**
	 * The Constructor
	 * @param text The text for the button
	 * @param root The Pane to add the button
	 */
	public LauncherButton(String text, Pane root) {
		this.setText(text);
		this.setupDefaultEvents();
		root.getChildren().add(this);
	}

	/**
	 * Tách logic sự kiện hover mặc định để tránh lặp code trong Constructor
	 */
	private void setupDefaultEvents() {
		this.setUnHover(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				setOpacity(1.0);
			}
		});
		this.setHover(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				setOpacity(0.80);
			}
		});
	}

	/**
	 * Thiết lập toàn bộ Style cho nút (Xóa style cũ, ghi đè cái mới)
	 * @param value Chuỗi CSS JavaFX
	 */
	public void setButtonStyle(String value) {
		this.setStyle(value);
	}

	/**
	 * Set the size of the button
	 * @param width_ The width
	 * @param height_ The height
	 */
	public void setSize(int width_, int height_) {
		this.setPrefSize(width_, height_);
	}

	/**
	 * Set the button invisible
	 */
	public void setInvisible() {
		this.setBackground(null);
	}

	/**
	 * Set the position
	 * @param posX The position X
	 * @param posY The position Y
	 */
	public void setPosition(int posX, int posY) {
		this.setLayoutX(posX);
		this.setLayoutY(posY);
	}
	
	/**
	 * Set the position and size
	 * @param posX The position X
	 * @param posY The position Y
	 * @param width_ The width
	 * @param height_ The height
	 */
	public void setBounds(int posX, int posY, int width_, int height_) {
		this.setLayoutX(posX);
		this.setLayoutY(posY);
		this.setPrefSize(width_, height_);
	}

	/**
	 * Set the Action when clicked
	 * @param value The value
	 */
	public void setAction(EventHandler<? super MouseEvent> value) {
		this.onMouseClickedProperty().set(value);
	}
	
	/**
	 * Set the Action when hover
	 * @param value The value
	 */
	public final void setHover(EventHandler<? super MouseEvent> value) {
		this.onMouseEnteredProperty().set(value);
	}
    
	/**
	 * Set the Action when unhover
	 * @param value The value
	 */
	public final void setUnHover(EventHandler<? super MouseEvent> value) {
		this.onMouseExitedProperty().set(value);
	}
    
	/**
	 * Thêm Style vào Style hiện tại (Cộng dồn)
	 * @param value The CSS value to add
	 */
	public final void addStyle(String value) {
		String currentStyle = this.getStyle();
		// Kiểm tra nếu style hiện tại không trống và thiếu dấu ";" thì tự thêm vào
		if (!currentStyle.isEmpty() && !currentStyle.endsWith(";")) {
			currentStyle += ";";
		}
		this.setStyle(currentStyle + value);
	}
}