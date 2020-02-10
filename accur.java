package accuracy;

import train.getFiles;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class accur {


    private double[][][] getData(String knowledgePath)  {
        String string = "";	//for temporary data store
        BufferedReader mainBR = null;
        try {
            mainBR = new BufferedReader(new FileReader(knowledgePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        double[][][] ratio = new double[256][256][256];

        for(int i=0; i<256; i++){
            for(int j=0; j<256; j++){
                for(int k=0; k<256; k++){
                    try {
                        string = mainBR.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ratio[i][j][k] = Double.parseDouble(string);
                }
            }
        }

        try {
            mainBR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ratio;
    }

    String filepTH="knowledge.dat";

    double[][][] keepresult= getData(filepTH);

    public double training(String[] givenmask,String[] mymasks,int k){
        //System.out.println(k);
        //System.out.println(givenmask[k]);
        //System.out.println(mymasks[k]);

        File mainImage=new File(givenmask[k]);
        File maskImage=new File(mymasks[k]);

        BufferedImage mainpic=null;
        BufferedImage maskpic=null;
        double total_pixel=0;
        double right_count=0;

        try {
            mainpic= ImageIO.read(mainImage);
            maskpic= ImageIO.read(maskImage);
            total_pixel=mainpic.getHeight()*mainpic.getWidth();
            right_count=0;
            int r,g,b;
            for(int i=0;i<mainpic.getHeight();i++){
                for(int j=0;j<mainpic.getWidth();j++){
                    Color mainpiccolor=new Color(mainpic.getRGB(j,i));
                    Color maskpiccolor=new Color(maskpic.getRGB(j,i));
                    r=mainpiccolor.getRed();
                    g=mainpiccolor.getGreen();
                    b=mainpiccolor.getBlue();
                    // System.out.println(maskpic.getRGB(j,i));
                    if(maskpiccolor.getRed()>=245 && maskpiccolor.getGreen()>=245 && maskpiccolor.getBlue()>=245){
                        if(keepresult[mainpiccolor.getRed()][mainpiccolor.getGreen()][mainpiccolor.getBlue()]<0.25){
                            // System.out.println("it is non skin");
                            right_count++;
                        }
                    }
                    else{
                        // System.out.println("it is skin");
                        //skin[r][g][b]++;
                        if(keepresult[mainpiccolor.getRed()][mainpiccolor.getGreen()][mainpiccolor.getBlue()]>0.25){
                            // System.out.println("it is non skin");
                            right_count++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return right_count/total_pixel;
        }
    }

    public static void main(String[] args) {
        String imageFolderPath="data/mask";
        String maskFolderPath="data/output";

        getFiles GetImage=new getFiles();
        String[] imagepaths=GetImage.GetAllFilesInFolder(imageFolderPath);
        String[] maskapaths=GetImage.GetAllFilesInFolder(maskFolderPath);


        double[] arr=new double[555];
        double sum=0;

       for(int i=0;i<imagepaths.length;i++){
           arr[i]=new accur().training(imagepaths,maskapaths,i);
           System.out.println("accuracy for pic "+i+" is "+arr[i]*100+"%");
           sum=sum+arr[i];
       }

        System.out.println(sum/555);
    }
}
