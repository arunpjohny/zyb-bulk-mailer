package in.co.zybotech.mailer.bulk;

import java.util.HashMap;
import java.util.Map;

public class Record {
	private String email;

	private Map<String, String> model = new HashMap<String, String>();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, String> getModel() {
		return new HashMap<String, String>(model);
	}

	public void put(String key, String value) {
		model.put(key, value);
	}
}
