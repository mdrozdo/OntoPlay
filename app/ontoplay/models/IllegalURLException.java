package ontoplay.models;

public class IllegalURLException extends IllegalArgumentException {
	private String illegalUrl;

	public IllegalURLException(String url) {
		super();
		illegalUrl = url;
	}

	public IllegalURLException(String message, String url) {
		super(message);
		illegalUrl = url;
	}

	public IllegalURLException(Throwable cause, String url) {
		super(cause);
		illegalUrl = url;
	}

	public IllegalURLException(String message, Throwable cause, String url) {
		super(message, cause);
		illegalUrl = url;
	}

	public String getIllegalUrl() {
		return illegalUrl;
	}
}
