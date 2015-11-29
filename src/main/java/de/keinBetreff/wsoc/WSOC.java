package de.keinBetreff.wsoc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "WSOC", targetNamespace = "wsoc.kein-betreff.de")
public class WSOC {

	private static Random rand = new Random();

	private synchronized int getRand() {
		return Math.abs(rand.nextInt());
	}

	@WebMethod
	public byte[] convert(@WebParam(name = "file") byte[] fileBytes, @WebParam(name = "fromFormat") String fromFormat,
			@WebParam(name = "toFormat") String toFormat) {
		File inFile = null;
		File outFile = null;
		try {
			int r = getRand();
			inFile = writeFile(fileBytes, r + "." + fromFormat);
			outFile = new File(r + "." + toFormat);

			Process p = new ProcessBuilder("soffice", "--headless", "--convert-to", toFormat, inFile.getAbsolutePath())
					.start();
			p.waitFor();

			if (!outFile.exists())
				throw new Exception("Ausgabedatei wurde nicht erstellt: " + inFile.getAbsolutePath() + " zu "
						+ outFile.getAbsolutePath());

			return readFile(outFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inFile != null)
				inFile.delete();
			if (outFile != null)
				outFile.delete();
		}
		return null;
	}

	@WebMethod
	public byte[] mergePdfs(@WebParam(name = "files") List<byte[]> files) {
		List<File> inFiles = new ArrayList<File>();
		File outFile = null;
		try {
			for (byte[] file : files) {
				inFiles.add(writeFile(file, getRand() + ".pdf"));
			}
			List<String> cmdList = new ArrayList<String>();
			// pdfsam-console -f 1.pdf -f 2.pdf -o ./new.pdf concat
			cmdList.add("pdfsam-console");
			for (File file : inFiles) {
				cmdList.add("-f");
				cmdList.add(file.getAbsolutePath());
			}
			outFile = new File(getRand() + ".pdf");
			cmdList.add("-o");
			cmdList.add(outFile.getAbsolutePath());
			cmdList.add("concat");

			Process p = new ProcessBuilder(cmdList).start();
			p.waitFor();
			if (!outFile.exists())
				throw new IOException("Ausgabedatei wurde nicht erstellt: " + outFile.getAbsolutePath());

			return readFile(outFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			for (File file : inFiles) {
				file.delete();
			}
			if (outFile != null)
				outFile.delete();
		}
		return null;
	}

	private byte[] readFile(File file) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		byte[] buffer = new byte[fis.available()];
		bis.read(buffer);
		bis.close();
		fis.close();
		return buffer;
	}

	private File writeFile(byte[] data, String name) throws Exception {
		File tmpFile = new File(name);
		FileOutputStream fos = new FileOutputStream(tmpFile);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		bos.write(data);
		bos.flush();
		fos.flush();
		bos.close();
		fos.close();
		return tmpFile;
	}

}
