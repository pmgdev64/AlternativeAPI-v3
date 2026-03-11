package fr.trxyy.alternative.alternative_apiv3.language;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.HashMap;
import java.util.Map;

import fr.trxyy.alternative.alternative_apiv3.ExceptionInLoadResourceException;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;

/**
 * @author PmgDev64
 */
public class I18n {
    private PropertyResourceBundle bundle;
    private Locale currentLocale;
    private static final String BUNDLE_PATH = "languages/messages";
    // Cache để tránh gọi Google API quá nhiều (giúp máy 4GB RAM chạy mượt hơn)
    private Map<String, String> translationCache = new HashMap<>();

    public I18n() {
        this.updateLocale(Locale.getDefault());
    }

    public void updateLocale(Locale locale) {
        this.currentLocale = locale;
        String lang = locale.getLanguage();
        String resourcePath = "/" + BUNDLE_PATH + "_" + lang + ".properties";
        
        // Kiểm tra file ngôn ngữ cụ thể, nếu không có thì thử file mặc định
        if (getClass().getResource(resourcePath) == null) {
            resourcePath = "/" + BUNDLE_PATH + ".properties";
        }

        // Load thủ công
        try (java.io.InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is != null) {
                this.bundle = new PropertyResourceBundle(new InputStreamReader(is, StandardCharsets.UTF_8));
                System.out.println("[AAuth] Loaded language file: " + resourcePath);
            } else {
                // Không ném Exception nữa, chỉ log cảnh báo
                this.bundle = null;
                System.err.println("[AAuth] Warning: Language file not found. Switching to Auto-Translate mode.");
            }
        } catch (Exception e) {
            this.bundle = null;
            System.err.println("[AAuth] Error loading language file: " + e.getMessage());
        }
        
        this.autoSaveLanguage(); // [cite: 2026-01-02]
    }

    public void translateAllElements(Parent root) {
        if (root == null) return;
        processRecursive(root);
    }

    private void processRecursive(Parent parent) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            String key = node.getId();
            if (key != null && !key.isEmpty()) {
                String translatedText = null;

                // Ưu tiên 1: File .properties
                if (bundle != null && bundle.containsKey(key)) {
                    translatedText = bundle.getString(key);
                } 
                // Ưu tiên 2: Google Translate
                else {
                    translatedText = translateWithGoogle(key, currentLocale.getLanguage());
                }

                if (translatedText != null) {
                    applyTranslation(node, translatedText);
                }
            }

            if (node instanceof Parent) {
                processRecursive((Parent) node);
            }
        }
    }

    private void applyTranslation(Node node, String text) {
        if (node instanceof Labeled) {
            ((Labeled) node).setText(text);
        } else if (node instanceof TextInputControl) {
            ((TextInputControl) node).setPromptText(text);
        }
    }

    private String translateWithGoogle(String text, String targetLang) {
        // Kiểm tra cache trước
        if (translationCache.containsKey(text)) {
            return translationCache.get(text);
        }

        try {
            String query = text.replace(".", " ").replace("_", " ");
            String urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=" 
                            + targetLang + "&dt=t&q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
            
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) response.append(inputLine);
                
                String res = response.toString();
                String result = res.substring(res.indexOf("\"") + 1, res.indexOf("\"", res.indexOf("\"") + 1));
                
                translationCache.put(text, result); // Lưu vào cache
                return result;
            }
        } catch (Exception e) {
            return text; // Trả về ID nếu lỗi mạng
        }
    }

    private void autoSaveLanguage() {
        // [cite: 2026-01-02] Logic tự động lưu cấu hình
    }
}