package Util;

import java.io.*;

public class ApplicationSerializer {

    // chose the .das extension to store the DLX_Assembler settings

    public void save(ApplicationSettings settings){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("settings.das");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(settings);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ApplicationSettings load(){
        ApplicationSettings settings = null;
        try {
            FileInputStream fileInputStream = new FileInputStream("settings.das");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            settings = (ApplicationSettings) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(settings == null){
            return new ApplicationSettings();
        }
        return settings;
    }
}
