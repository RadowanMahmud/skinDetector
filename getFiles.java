package train;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class getFiles {

    public getFiles(){

    }

    public String[] GetAllFilesInFolder(String Folderpath){

        //File[] files = new File(Folderpath).listFiles();
        File folder = new File(Folderpath);
        File[] files = folder.listFiles();

        ArrayList<String> filePathList = new ArrayList<String>();

        for(File file: files){
            filePathList.add(file.getAbsolutePath());
        }

        String[] filePaths = new String[filePathList.size()];
        filePathList.toArray(filePaths);

        Arrays.sort(filePaths);

        return filePaths;
    }
}
