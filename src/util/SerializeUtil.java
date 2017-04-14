package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializeUtil {
	public static Serializable clone(Serializable ser) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(ser);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			Serializable obj = (Serializable) ois.readObject();
			oos.close();
			baos.close();
			bais.close();
			ois.close();
			return obj;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void save(Serializable ser, File file) {
		try {
			file.createNewFile();
			FileOutputStream baos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(ser);
			oos.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Serializable load(File f){
		try {
			FileInputStream bais = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Serializable obj = (Serializable) ois.readObject();
			bais.close();
			ois.close();
			return obj;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
