package train;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class process {

    public process(){

    }
    double[][][] skin = new double[256][256][256];
    double[][][] nonskin=new double[256][256][256];

    public void initialArray(){
        for(int i=0;i<256;i++){
            for(int j=0;j<256;j++){
                for(int k=0;k<256;k++){
                    skin[i][j][k]=0;
                    nonskin[i][j][k]=0;
                }
            }
        }
    }

    public void minedata(String[] images,String[] masks) throws IOException {

        initialArray();

        for(int k=0;k<images.length;k++){
            System.out.println(k);
            System.out.println(images[k]);
            System.out.println(masks[k]);

            File mainImage=new File(images[k]);
            File maskImage=new File(masks[k]);

            BufferedImage mainpic=null;
            BufferedImage maskpic=null;

            try {
                mainpic= ImageIO.read(mainImage);
                maskpic= ImageIO.read(maskImage);
                int r,g,b;
                for(int i=0;i<mainpic.getHeight();i++){
                    for(int j=0;j<mainpic.getWidth();j++){
                        Color mainpiccolor=new Color(mainpic.getRGB(j,i));
                        Color maskpiccolor=new Color(maskpic.getRGB(j,i));
                        r=mainpiccolor.getRed();
                        g=mainpiccolor.getGreen();
                        b=mainpiccolor.getBlue();
                       // System.out.println(maskpic.getRGB(j,i));
                        if(maskpiccolor.getRed()>=230 && maskpiccolor.getGreen()>=230 && maskpiccolor.getBlue()>=230){
                           // System.out.println("it is non skin");
                            nonskin[r][g][b]++;
                        }
                        else{
                           // System.out.println("it is skin");
                            skin[r][g][b]++;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        double totalskincount=0;
        double totalnonskincount=0;

        for(int i=0;i<256;i++){
            for(int j=0;j<256;j++){
                for(int k=0;k<256;k++){
                    totalskincount=skin[i][j][k]+totalskincount;
                    totalnonskincount=nonskin[i][j][k]+totalnonskincount;
                }
            }
        }

        System.out.println(totalskincount);
        System.out.println(totalnonskincount);

        double[][][] keepresult=new double[256][256][256];

        BufferedWriter mainBW = new BufferedWriter(new FileWriter("knowledge.dat"));
        mainBW.write("");
        for(int i=0;i<256;i++){
            for(int j=0;j<256;j++){
                for(int k=0;k<256;k++){
                 /*   if(nonskin[i][j][k]==0)
                    {
                        keepresult[i][j][k]=100;
                    }
                    else
                    {
                        keepresult[i][j][k]=(skin[i][j][k]/totalskincount)/(nonskin[i][j][k]/totalnonskincount);

                    }
                    */
                    nonskin[i][j][k]=nonskin[i][j][k]/totalnonskincount;
                    skin[i][j][k]=skin[i][j][k]/totalskincount;


                    if(nonskin[i][j][k]==0 && skin[i][j][k]>0) keepresult[i][j][k]=100;

                    else if(nonskin[i][j][k]==0 && skin[i][j][k]==0) keepresult[i][j][k]=0;

                    else {
                        keepresult[i][j][k]=skin[i][j][k]/nonskin[i][j][k];
                    }
                    System.out.println(String.format("%.3f\n", keepresult[i][j][k]));
                    mainBW.append(String.format("%.3f\n", keepresult[i][j][k]));
                }
            }
        }

        mainBW.close();

        System.out.println("Successful");

    }
}
