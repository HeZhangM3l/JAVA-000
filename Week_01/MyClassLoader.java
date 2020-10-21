package geek_time;

import daili.MyJdkProxy;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader{

    @Override
    protected Class<?> findClass(String moduleName, String fileName){
        byte[] bytes = getBytes(fileName);
        return defineClass(moduleName, bytes, 0, bytes.length);
    }

    private byte[] getBytes(String file){
        byte[] res = null;
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(file));
            byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1];
            byte[] restoreBytes = new byte[1];
            byte b255 = (byte) 255;
            int n;
            while ((n = fileInputStream.read(b)) != -1) {
                restoreBytes[0] = (byte) (b255 - b[0]);
                byteArrayOutputStream.write(restoreBytes, 0, n);
            }
            res = byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String filePath = MyClassLoader.class.getResource("Hello.xlass").getPath();
        try{
            Class<?> clz = new MyClassLoader().findClass("Hello", filePath);
            Method hello = clz.getDeclaredMethod("hello");
            hello.setAccessible(true);
            hello.invoke(clz.newInstance());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
