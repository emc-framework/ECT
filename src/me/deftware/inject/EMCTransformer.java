package me.deftware.inject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.launchwrapper.IClassTransformer;

public class EMCTransformer implements IClassTransformer {

	public static EMCTransformer instance;
	private ZipFile emcZipFile = null;

	public EMCTransformer() {
		instance = this;
		System.out.println("Loading EMCTransformer");
		try {
			URL url = EMCTransformer.class.getProtectionDomain().getCodeSource().getLocation();
			emcZipFile = new ZipFile(new File(url.toURI()));
		} catch (Exception ex) {
			System.err.println("EMCTransformer: Failed to find EMC jar");
			ex.printStackTrace();
		}
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		String nameClass = name + ".class";
		byte[] ofBytes = getEMCResource(nameClass);
		if (ofBytes != null) {
			System.out.println("EMCTransformer: Transforming class " + name + ".class");
			return ofBytes;
		}
		return bytes;
	}

	public static String removePrefix(String str, String prefix) {
		if ((str == null) || (prefix == null)) {
			return str;
		}
		if (str.startsWith(prefix)) {
			str = str.substring(prefix.length());
		}
		return str;
	}

	public synchronized byte[] getEMCResource(String name) {
		name = removePrefix(name, "/");
		byte[] bytes = getEMCResourceZip(name);
		if (bytes != null) {
			return bytes;
		}
		return null;
	}

	public synchronized byte[] getEMCResourceZip(String name) {
		if (emcZipFile == null) {
			return null;
		}
		name = removePrefix(name, "/");
		ZipEntry ze = emcZipFile.getEntry(name);
		if (ze == null) {
			return null;
		}
		try {
			InputStream in = emcZipFile.getInputStream(ze);
			byte[] bytes = readAll(in);
			in.close();
			if (bytes.length != ze.getSize()) {
				System.err.println("EMCTransformer: Invalid size, name: " + name + ", zip: " + ze.getSize()
						+ ", stream: " + bytes.length);
				return null;
			}
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readAll(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte['È'];
		for (;;) {
			int len = is.read(buf);
			if (len < 0) {
				break;
			}
			baos.write(buf, 0, len);
		}
		is.close();
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

}
