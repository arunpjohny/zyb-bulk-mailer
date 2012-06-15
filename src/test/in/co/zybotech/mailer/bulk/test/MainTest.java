package in.co.zybotech.mailer.bulk.test;

import in.co.zybotech.mailer.bulk.Main;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class MainTest {

	public static void main(String[] args) throws UnsupportedEncodingException {
		File dir = new File(
				"D:/Arun P Johny/zybotech/repository/in.co.zybotech.mailer.bulk/src/test/in/co/zybotech/mailer/bulk/test");

		Main.main(new String[] { getFilePath(dir, "properties.properties"),
				getFilePath(dir, "people.txt"), "", "Subject",
				getFilePath(dir, "mail.html"), getFilePath(dir, "mail.txt") });

	}

	private static String getFilePath(File dir, String string) {
		return new File(dir, string).getAbsolutePath();
	}

}
