package in.co.zybotech.mailer.bulk.test;

import in.co.zybotech.mailer.bulk.Main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class MainTest {

	public static void main(String[] args) throws UnsupportedEncodingException {
		File dir = new File(
				"D:/Arun P Johny/zybotech/repository/in.co.zybotech.mailer.bulk/src/test/in/co/zybotech/mailer/bulk/test");

		Main.main(new String[] { "v2",
				getFilePath(dir, "properties.properties"),
				getFilePath(dir, "people.txt"), "email,name",
				"Subject : " + new Date(), getFilePath(dir, "mail.html"),
				getFilePath(dir, "mail.txt"), getFilePath(dir, "attachments") });

	}

	private static String getFilePath(File dir, String string) {
		return new File(dir, string).getAbsolutePath();
	}

}
