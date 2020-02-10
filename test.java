package app;

import train.getFiles;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class test {


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

    public void detectSkin(String image,String output,String knowledge){

        double[][][] keepresult= getData(knowledge);

        int white=new Color(255,255,255).getRGB();
        int black=new Color(0,0,0).getRGB();


        File mainImage=new File(image);

        BufferedImage mainpic=null;

        try {
            mainpic= ImageIO.read(mainImage);

            for(int i=0;i<mainpic.getWidth();i++){
                for(int j=0;j<mainpic.getHeight();j++){
                    Color mainpiccolor=new Color(mainpic.getRGB(i,j));
                    if(keepresult[mainpiccolor.getRed()][mainpiccolor.getGreen()][mainpiccolor.getBlue()]<0.3){
                       // System.out.println("it is non skin");
                            mainpic.setRGB(i,j,white);
                    }
                    else{
                       // mainpic.setRGB(i,j,white);
                       // System.out.println("it is  skin");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ImageIO.write(mainpic, "bmp", new File(output));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run(int p){
        String imageFolderPath="data/images";
        String output="data/output/";
        //String maskFolderPath="data/mask";

        getFiles GetImage=new getFiles();
        String[] imagepaths=GetImage.GetAllFilesInFolder(imageFolderPath);
        int level=0;
        if(p!=5)level=(imagepaths.length/5)*p;
        for(int k=level;k<level+(imagepaths.length/5) && k<imagepaths.length;k++) {
            String image=imagepaths[k];
            System.out.println(p+" this is "+k);
            String filepTH="knowledge.dat";
            String outputimage=output+"output"+k+".bmp";
            new test().detectSkin(image,outputimage,filepTH);

        }
    }

    public static void main(String[] args) {

    }
}
