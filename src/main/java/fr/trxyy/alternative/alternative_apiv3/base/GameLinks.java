package fr.trxyy.alternative.alternative_apiv3.base;

/**
 * @author Trxyy
 */
public class GameLinks {

	/**
	 * The base url, ex: http://mywebsite.com/
	 */
	public String BASE_URL;
	/**
	 * The json url, ex: http://mywebsite.com/mc_version.json
	 */
	public String JSON_URL;
	/**
	 * The json name, 1.7.10.json for 1.7.10 version
	 */
	public String JSON_NAME;
	/**
	 * The ignore list, ex: http://mywebsite.com/ignore.cfg
	 */
	public String IGNORE_LIST;
	/**
	 * The delete list, ex: http://mywebsite.com/delete.cfg
	 */
	public String DELETE_LIST;
	/**
	 * The custom files url, ex: http://mywebsite.com/files/
	 */
	public String CUSTOM_FILES_URL;

	/**
	 * The Constructor
	 * 
	 * @param baseUrl  The base url
	 * @param jsonName The json name (1.7.10.json for 1.7.10 version)
	 */
	public GameLinks(String baseUrl, String jsonName) {
	    // 1. Chuẩn hóa Base URL trước
	    if (baseUrl.endsWith("/")) {
	        this.BASE_URL = baseUrl;
	    } else {
	        this.BASE_URL = baseUrl + "/";
	    }

	    // 2. Sử dụng this.BASE_URL (đã có dấu /) cho tất cả các link khác
	    this.JSON_NAME = jsonName;
	    this.JSON_URL = this.BASE_URL + jsonName;
	    this.IGNORE_LIST = this.BASE_URL + "ignore.cfg";
	    this.DELETE_LIST = this.BASE_URL + "delete.cfg";
	    this.CUSTOM_FILES_URL = this.BASE_URL + "files/";
	}

	/**
	 * @return The url
	 */
	public String getBaseUrl() {
		return this.BASE_URL;
	}

	/**
	 * @return The json name
	 */
	public String getJsonName() {
		return this.JSON_NAME;
	}

	/**
	 * @return The json url
	 */
	public String getJsonUrl() {
		return this.JSON_URL;
	}

	/**
	 * @return The ignore list url
	 */
	public String getIgnoreListUrl() {
		return this.IGNORE_LIST;
	}

	/**
	 * @return The delete list url
	 */
	public String getDeleteListUrl() {
		return this.DELETE_LIST;
	}

	/**
	 * @return The custom files folder url
	 */
	public String getCustomFilesUrl() {
		return this.CUSTOM_FILES_URL;
	}

}
