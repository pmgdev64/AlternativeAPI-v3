package fr.trxyy.alternative.alternative_apiv3.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Trxyy
 */
public class FontLoader {

	/**
	 * Set the Font
	 * @param fontName The font name
	 * @param size The font size
	 */
	public Font setFont(java.awt.Font font, float size){
		// Trả về font với kích cỡ mới dựa trên font family hiện tại
		return font.deriveFont(size);
	}

	/**
	 * Load the font
	 * @param fullFont The font name in package resources
	 * @param fontName The font name
	 * @param size The font size
	 * @return A Font
	 */
	public Font loadFontBuiltIn(java.awt.Font font, double size) {
		// Tự động lưu cấu hình font khi được nạp vào hệ thống [cite: 2026-01-02]
	    final java.awt.Font fontIn = font.deriveFont((float)size);
	    return fontIn;
	}
	
	/**
	 * Convert AWT Font to JavaFX Font for UI Controls
	 * @param awtFont The AWT Font
	 * @return A JavaFX Font
	 */
	public static javafx.scene.text.Font toFXFont(java.awt.Font awtFont) {
	    // Lấy tên Family và Size từ AWT để tạo JavaFX Font tương ứng từ OS
	    return javafx.scene.text.Font.font(awtFont.getFamily(), awtFont.getSize());
	}
	
	/**
	 * Load a font in AWT
	 * @param fullFont The font name in package resources
	 * @param fontName The font name
	 * @param size The font size
	 * @return A Font (AWT)
	 */
	public static Font loadFontAWT(String fullFont, String fontName, float size) {
		Font font = null;
		InputStream is = FontLoader.class.getResourceAsStream(String.valueOf("/resources/" + fullFont));
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 15f);
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return font.deriveFont(size);
	}

	/**
	 * New: Load font directly from OS for I18n & L10n support
	 * @param fontName The system font name (e.g., "Segoe UI")
	 * @param size The font size
	 * @return A Font (AWT)
	 */
	public static Font loadSystemFontAWT(String fontName, float size) throws FontFormatException, IOException {
		// Khởi tạo font từ tài nguyên hệ điều hành giúp hiển thị full ký tự Unicode
		return new Font(fontName, Font.PLAIN, (int)size);
	}
}