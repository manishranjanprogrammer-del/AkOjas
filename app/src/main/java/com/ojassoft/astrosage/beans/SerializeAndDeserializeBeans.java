package com.ojassoft.astrosage.beans;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

/**
 * This Class is used to serialize and de-serialize objects.
 *
 * @author Hukum
 */
public class SerializeAndDeserializeBeans {


    /**
     * This method returns de-serialized object. user should cast it into appropriate type.
     *
     * @param context
     * @param serializedFileName key to retrieve particular object.
     * @return user object in the form of Object.class, user should cast it.
     * @throws IOException
     */
    public static Object getDeSerializedBeanObject(Context context, String serializedFileName) throws IOException {
        Object obj = null;
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        try {
            fileIn = context.openFileInput(serializedFileName);
            in = new ObjectInputStream(fileIn);
            obj = in.readObject();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();

        } finally {
            if (in != null)
                in.close();
            if (fileIn != null)
                fileIn.close();
        }
        return obj;
    }

    /**
     * This method save user object to internal memory (in the form of File system) of application.
     *
     * @param context
     * @param serializedFileName Key to save a Object into file system.
     * @param beanObject         Object-value to save in file syetem.
     * @throws IOException
     */
    public static void saveSerializedBeanObject(Context context, String serializedFileName, Object beanObject) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = context.openFileOutput(serializedFileName, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.writeObject(beanObject);

        } catch (IOException i) {
            i.printStackTrace();
        } finally {
            if (out != null)
                out.close();
            if (fos != null)
                fos.close();
        }
    }

    /**
     * @param context
     * @param serializedFileName
     */
    public static boolean cleanSerializedFile(Context context, String serializedFileName) {
        boolean success = false;
        success = context.deleteFile(serializedFileName);
        return success;
    }

}
