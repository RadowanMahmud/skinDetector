package train;

import java.io.IOException;

public class main {

    public static void main(String[] args) {


        String imageFolderPath="data/images";
        String maskFolderPath="data/mask";

        getFiles GetImage=new getFiles();
        String[] imagepaths=GetImage.GetAllFilesInFolder(imageFolderPath);
        String[] maskapaths=GetImage.GetAllFilesInFolder(maskFolderPath);

        System.out.println(imagepaths.length);

        for(int i=0;i<imagepaths.length;i++){
            System.out.println(i+"    "+imagepaths[i]+" "+maskapaths[i]);
        }

        process p=new process();
        try {
            p.minedata(imagepaths,maskapaths);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
