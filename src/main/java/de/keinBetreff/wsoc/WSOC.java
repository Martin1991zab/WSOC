package de.keinBetreff.wsoc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "WSOC", targetNamespace = "wsoc.kein-betreff.de")
public class WSOC {

	private static Random rand = new Random();

	@WebMethod
	public byte[] convert(@WebParam(name = "file") byte[] fileBytes, @WebParam(name = "fromFormat") String fromFormat,
			@WebParam(name = "toFormat") String toFormat) {
		try {
			int r = Math.abs(rand.nextInt());
			File inFile = new File(r + "." + fromFormat);
			inFile.deleteOnExit();
			File outFile = new File(r + "." + toFormat);
			outFile.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(inFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(fileBytes);
			bos.flush();
			fos.flush();
			bos.close();
			fos.close();

			Process p = new ProcessBuilder("soffice", "--headless", "--convert-to", toFormat, inFile.getAbsolutePath())
					.redirectError(new File("err.txt")).redirectOutput(new File("out.txt")).start();

			p.waitFor();

			if (!outFile.exists())
				throw new Exception("Ausgabe Datei wurde nicht angelegt: " + inFile.getAbsolutePath() + " zu "
						+ outFile.getAbsolutePath());

			FileInputStream fis = new FileInputStream(outFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			byte[] buffer = new byte[fis.available()];
			bis.read(buffer);
			bis.close();
			fis.close();

			return buffer;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
