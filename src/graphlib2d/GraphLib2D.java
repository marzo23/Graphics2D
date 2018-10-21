/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphlib2d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphLib2D extends JFrame {

    BufferedImage buffer;
    Graphics graphics;
    int width = 540, height = 540;
    int screenSize = 500;

    public GraphLib2D() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setResizable(false);
        this.setSize(width, height);
        this.setLocationByPlatform(true);
        this.setVisible(true);

        buffer = new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB);
        graphics = (Graphics2D) buffer.createGraphics();
    }

    public static void main(String[] args) {
        GraphLib2D p = new GraphLib2D();
    }

    public void drawPixel(int x, int y, Color color) {
        buffer.setRGB(0, 0, color.getRGB());
        this.getGraphics().drawImage(buffer, x, y, this);
    }
    
    public void drawPixel(int x, int y, Graphics g, Color color) {
        drawPixel(x,y,g,1,color);
    }
    
    public void drawPixel(int x, int y, Graphics g, int factor, Color color) {
        for (int i = 0; i < factor; i++) {
            for (int j = 0; j < factor; j++) {
                buffer.setRGB(0, 0, color.getRGB());
                g.drawImage(buffer, x+j, y+i, this);
            }
        }
    }
    
    public void drawPixel(int x, int y, int width, int m, Color color) {
        for (int i = -width / 2; i < width / 2; i++) {
            drawPixel(m < 1 ? x : x + i, m < 1 ? y + 1 : y, color);
        }
    }

    public void drawPixelC(int x, int y, int width, int angle, Color color) {
        for (int i = -width / 2; i < width / 2; i++) {
            drawPixel(angle > 45 ? x : x + i, angle < 45 ? y + 1 : y, color);
        }
    }

    public void drawCircleFormula(int x1, int y1, int radio, Color color) {
        for (int x = -radio; x < radio; x++) {
            int y = (int) Math.sqrt(radio * radio - x * x);
            drawPixel(x + radio + x1, y + y1, Color.BLUE);
            drawPixel(x + radio + x1, (y1 - y), Color.BLUE);
        }
    }

    public void drawCirclePolar(int x1, int y1, int radio, Color color) {
        for (int i = 0; i < 360; i++) {
            int x = (int) (radio * Math.sin(Math.toRadians(i)));
            int y = (int) (radio * Math.cos(Math.toRadians(i)));
            drawPixel(x + x1, y + y1, Color.BLUE);
        }
    }

    public void drawCircleOct(int x1, int y1, int radio, Color color) {
        for (int i = 0; i < (360 / 8); i++) {
            int x = (int) (radio * Math.sin(Math.toRadians(i)));
            int y = (int) (radio * Math.cos(Math.toRadians(i)));
            drawC(x, x1, y, y1, color);
        }
    }

    public void drawCirclePolar(int x1, int y1, int radio, int lineWidth, Color color) {
        for (int i = 0; i < 360; i++) {
            int x = (int) (radio * Math.sin(Math.toRadians(i)));
            int y = (int) (radio * Math.cos(Math.toRadians(i)));
            drawPixel(x + x1, y + y1, lineWidth, i, Color.BLUE);
        }
    }

    public void drawCirclePolar(int x1, int y1, int radio, int[] pattern, Color color) {
        for (int i = 0; i < 360; i++) {
            int x = (int) (radio * Math.sin(Math.toRadians(i)));
            int y = (int) (radio * Math.cos(Math.toRadians(i)));
            if (pattern[i % pattern.length] == 1)
                drawPixel(x + x1, y + y1, Color.BLUE);
        }
    }

    public void drawC(int x, int x1, int y, int y1, Color color) {
        drawPixel(x + x1, y + y1, color);
        drawPixel(x + x1, y1 - y, color);
        drawPixel(x1 - x, y + y1, color);
        drawPixel(x1 - x, y1 - y, color);
        
        drawPixel(y + x1, x + y1, color);
        drawPixel(x1 - y, x + y1, color);
        drawPixel(y + x1, y1 - x, color);
        drawPixel(x1 - y, y1 - x, color);
    }

    public void drawCircleBresenham(int x1, int y1, int radio, Color color) {
        int x = 0, y = radio;
        int d = 3 - 2 * radio;
        while (y >= x) {
            drawC(x, x1, y, y1, color);
            x++;
            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else
                d = d + 4 * x + 6;
            drawC(x, x1, y, y1, color);
        }
    }
    

    public void drawCircleMiddlePoint(int x1, int y1, int radio, Color color) {
        int x = radio, y = 0;
        if (radio > 0) {
            drawC(x, x1, y, y1, color);
        }
        int P = 1 - radio;
        while (x > y) {

            y++;
            if (P <= 0)
                P = P + 2 * y + 1;
            else {
                x--;
                P = P + 2 * y - 2 * x + 1;
            }
            if (x < y)
                break;
            drawC(x, x1, y, y1, color);
            if (x != y) {
                drawC(x, x1, y, y1, color);
            }
        }
    }
    
    public void drawRec(int x, int y, int width, int height, Color color){
        drawLineDAA(x, y, x+width,y,color);
        drawLineDAA(x, y, x,y+height,color);
        drawLineDAA(x+width, y, x+width,y+height,color);
        drawLineDAA(x, y+height, x+width,y+height,color);
    }
    
    public void drawElipse(int x0, int y0, int a, int b, Color color){
        int x, y = 0;
        if(a>b)
        for (x = 0; x < a; x++) {
            double tmp = (double)x/(double)a;
            y = (int)(b * Math.sqrt(1-tmp)*Math.sqrt(1+tmp));
            //if(x!=0?Math.abs(y/x)<0.2:false)
                //break;
            
            drawPixel(x0+x, y+y0, color);
            drawPixel(x0+x, y0-y, color);
            drawPixel(x0-x, y+y0, color);
            drawPixel(x0-x, y0-y, color);
        }
        else
        for (; y > 0; y--) {
            double tmp = (double)y/(double)b;
            x = (int)(a * Math.sqrt(1-tmp)*Math.sqrt(1+tmp));
            drawPixel(x0+x, y+y0, color);
            drawPixel(x0+x, y0-y, color);
            drawPixel(x0-x, y+y0, color);
            drawPixel(x0-x, y0-y, color);
        }
    }
    
    public void figurasPractica(){
        /*for (int i = 0; i < screenSize; i+=10) {
            drawLineDAA(0, i, screenSize, i, Color.black);
            drawLineDAA(i,0,i,screenSize, Color.black);
        }*/
        drawRec(10,50,200,150,Color.BLUE);
        drawRec(60,100,100,50,Color.BLUE);
        
        drawCircleOct(300,120,80,Color.GREEN);
        drawCircleOct(300,120,60,Color.GREEN);
        drawCircleOct(300,120,40,Color.GREEN);
        
        drawElipse(300, 300, 200, 100, Color.red);
        drawElipse(300, 300, 160, 80, Color.red);
        drawElipse(300, 300, 120, 60, Color.red);
        drawElipse(300, 300, 80, 40, Color.red);
        
        drawLine(0,0,screenSize, screenSize, Color.orange);
        drawLine(0,screenSize, screenSize, 0, Color.orange);
        drawLine(screenSize/2, 0, screenSize/2, screenSize, Color.orange);
        drawLine(0,screenSize/2, screenSize, screenSize/2, Color.orange);
    }
    
    public BufferedImage RenderImg(String path){
        File file= new File(path);
        BufferedImage image;
        try {
            image = ImageIO.read(file);
            return image;
        } catch (IOException ex) {
            Logger.getLogger(GraphLib2D.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public BufferedImage resize(BufferedImage img, double factor){
        if(factor==1)
            return img;
        
        BufferedImage newImg = new BufferedImage((int)(factor * img.getWidth()), (int)(factor * img.getHeight()), BufferedImage.TYPE_INT_ARGB);
        Graphics graphNew = newImg.getGraphics();
        int i = 0;
        int j = 0;
        int x = 0;
        int y = 0;
        while (i < img.getHeight()) {
            j = 0;
            x = 0;
            while (j < img.getWidth()) {
                int clr=  img.getRGB(j,i); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                if(factor<1){
                    if(clr!=0)
                        drawPixel(x,y, graphNew, 1, new Color(red, green, blue));
                    j+=1/factor;
                    x++;
                }else{
                    if(clr!=0)
                        drawPixel(x, y, graphNew, (int)factor, new Color(red, green, blue));
                    j++;
                    x+=factor;
                }
            }
            if(factor<1){
                i+=1/factor;
                y++;
            }
            else{
                i++;
                y+=factor;
            }
        }
        return newImg;
    }
    
    public BufferedImage rotateImage(BufferedImage img, int angle){
        int diag = (int) Math.sqrt(img.getWidth()*img.getWidth()+img.getHeight()*img.getHeight());
        BufferedImage newImg = new BufferedImage(diag, diag, BufferedImage.TYPE_INT_ARGB);
        Graphics newGraph = newImg.getGraphics();
        for (int i = -img.getHeight()/2; i < img.getHeight()/2; i++) {
            for (int j = -img.getWidth()/2; j < img.getWidth()/2; j++) {
                int clr=  img.getRGB((j+img.getWidth()/2),(i+img.getHeight()/2)); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                int x = (int)(j*Math.cos(Math.toRadians(angle))-i*Math.sin(Math.toRadians(angle)));
                int y = (int)(j*Math.sin(Math.toRadians(angle))+i*Math.cos(Math.toRadians(angle)));
                if(clr!=0)
                    drawPixel(diag/2+x,diag/2+y, newGraph, new Color(red, green, blue));
            }
        }
        return newImg;
    }
    
    public void drawImage(int x, int y, BufferedImage img){
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                int clr=  img.getRGB(j,i); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                if(clr!=0)
                    drawPixel(j+x,i+y,new Color(red, green, blue));
            }
        }
    }
    
    public void croptLineByRect(int xRect, int yRect, int widthRect, int heightRect, Color rectColor, int x1Line, int y1Line, int x2Line, int y2Line, Color lineColor){
        if(rectColor!=null)
            drawRec(xRect, yRect, widthRect, heightRect, rectColor);
        
        boolean firstPoint =  x1Line>=xRect && x1Line<=xRect+widthRect && y1Line>=yRect && y1Line<=yRect+heightRect;
        boolean secondPoint = x2Line>=xRect && x2Line<=xRect+widthRect && y2Line>=yRect && y2Line<=yRect+heightRect;
        if(firstPoint && secondPoint){
            drawLineDAA(x1Line, y1Line, x2Line, y2Line, lineColor);
        }else{
            ArrayList<Point> pointList = new ArrayList<Point>();
            Point p1 = getIntersectionPoint(new Point(xRect, yRect),new Point(xRect, yRect+heightRect), new Point(x1Line, y1Line), new Point(x2Line, y2Line));
            if(p1!=null)
                pointList.add(p1);
            p1 = getIntersectionPoint(new Point(xRect, yRect),new Point(xRect+widthRect, yRect), new Point(x1Line, y1Line), new Point(x2Line, y2Line));
            if(p1!=null)
                pointList.add(p1);
            p1= getIntersectionPoint(new Point(xRect+widthRect, yRect+heightRect),new Point(xRect+widthRect, yRect), new Point(x1Line, y1Line), new Point(x2Line, y2Line));
            if(p1!=null)
                pointList.add(p1);
            p1= getIntersectionPoint(new Point(xRect+widthRect, yRect+heightRect),new Point(xRect, yRect+heightRect), new Point(x1Line, y1Line), new Point(x2Line, y2Line));
            if(p1!=null)
                pointList.add(p1);
            if(pointList.isEmpty())
                return;
            else if(pointList.size() > 0 && (firstPoint||secondPoint)){
                if(firstPoint)
                    drawLineMiddlePoint(pointList.get(0).x, pointList.get(0).y, x1Line, y1Line, lineColor);
                else
                    drawLineMiddlePoint(pointList.get(0).x, pointList.get(0).y, x2Line, y2Line, lineColor);
            }else if(pointList.size() == 2)
                drawLineMiddlePoint(pointList.get(0).x, pointList.get(0).y, pointList.get(1).x, pointList.get(1).y, lineColor);
        }
    }
    
    public void croptCircleByRect(int xRect, int yRect, int widthRect, int heightRect, Color rectColor, int xCircle, int yCircle, int radio, Color colorCircle){
        if(rectColor!=null)
            drawRec(xRect, yRect, widthRect, heightRect, rectColor);
        for (int i = 0; i < (360 / 8); i++) {
            int x = (int) (radio * Math.sin(Math.toRadians(i)));
            int y = (int) (radio * Math.cos(Math.toRadians(i)));
            paintIfIsInRectangle(x + xCircle, y + yCircle, colorCircle, xRect, yRect, widthRect, heightRect);
            paintIfIsInRectangle(x + xCircle, yCircle - y, colorCircle, xRect, yRect, widthRect, heightRect);
            paintIfIsInRectangle(xCircle - x, y + yCircle, colorCircle, xRect, yRect, widthRect, heightRect);
            paintIfIsInRectangle(xCircle - x, yCircle - y, colorCircle, xRect, yRect, widthRect, heightRect);

            paintIfIsInRectangle(y + xCircle, x + yCircle, colorCircle, xRect, yRect, widthRect, heightRect);
            paintIfIsInRectangle(xCircle - y, x + yCircle, colorCircle, xRect, yRect, widthRect, heightRect);
            paintIfIsInRectangle(y + xCircle, yCircle - x, colorCircle, xRect, yRect, widthRect, heightRect);
            paintIfIsInRectangle(xCircle - y, yCircle - x, colorCircle, xRect, yRect, widthRect, heightRect);
        }
    }
    
    public void paintIfIsInRectangle(int x, int y, Color pointColor, int xRect, int yRect, int widthRect, int heightRect){
        if(x>=xRect && x<=xRect+widthRect && y>=yRect && y<=yRect+heightRect)
            drawPixel(x, y, pointColor);
    }
    
    public Point getIntersectionPoint(Point p1Line1, Point p2Line1, Point p1Line2, Point p2Line2){
        double m1 = (p1Line1.x-p2Line1.x)==0? Double.POSITIVE_INFINITY:(double)(p1Line1.y-p2Line1.y)/(double)(p1Line1.x-p2Line1.x);
        double m2 = (p1Line2.x-p2Line2.x)==0? Double.POSITIVE_INFINITY:(double)(p1Line2.y-p2Line2.y)/(double)(p1Line2.x-p2Line2.x);
        Point p = null;
        if((m1 ==Double.POSITIVE_INFINITY && m2 == Double.POSITIVE_INFINITY) || (m1 ==0 && m2 == 0) || m1==m2)
            return null;
        if(m1 ==Double.POSITIVE_INFINITY || m2 == Double.POSITIVE_INFINITY){
            double b1 = m1!=Double.POSITIVE_INFINITY?p1Line1.y-m1*p1Line1.x:0;
            double b2 = m2!=Double.POSITIVE_INFINITY?p1Line2.y-m2*p1Line2.x:0;
            double x = b1==0? p1Line1.y: p1Line2.y;
            double y = b1==0? m2*x+b2: m1*x+b1;
            p = new Point((int)x,(int)y);
        }
        else{
            double b1 = p1Line1.y-m1*p1Line1.x;
            double b2 = p1Line2.y-m2*p1Line2.x;
            double x = (b1-b2)/(m2-m1);
            double y = m1==0?m2*x+b2:m1*x+b1;
            p = new Point((int)x,(int)y);
        }
        if(((p1Line1.x<=p.x && p2Line1.x>=p.x)||(p2Line1.x<=p.x && p1Line1.x>=p.x)) && ((p1Line1.y<=p.y && p2Line1.y>=p.y)||(p2Line1.y<=p.y && p1Line1.y>=p.y))&&
           ((p1Line2.x<=p.x && p2Line2.x>=p.x)||(p2Line2.x<=p.x && p1Line2.x>=p.x)) && ((p1Line2.y<=p.y && p2Line2.y>=p.y)||(p2Line2.y<=p.y && p1Line2.y>=p.y)))
            return p;
        
        return null;
    }
    
    @Override
    public void paint(Graphics g) {
        //croptLineByRect(50,50,100,100,Color.red,120,120,70,170,Color.BLUE);
        croptCircleByRect(50,50,100,200, Color.BLUE, 100,100,55,Color.pink);
//        figurasPractica();
//        BufferedImage img = resize(RenderImg("C:\\Users\\L440\\Downloads\\ant.png"), .2);
//        drawImage(200, 200,rotateImage(img, 360));
        //drawCircleBresenham(300,300,40,Color.GREEN);
        /*
        try {
            scanlineFilling(100,100, null, Color.red);
        } catch (AWTException ex) {
            Logger.getLogger(GraphLib2D.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        //drawLine(0,0,400,400,new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },Color.BLUE);
        //drawRec(200,200,100,100,Color.BLUE);
        //drawCirclePolar(100, 100, 50, new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },Color.BLUE);
        /*/ floodFilling(100, 100, null, Color.red);
        drawLineMiddlePoint(0,0,screenSize, screenSize, Color.BLUE);
        drawLineMiddlePoint(screenSize, screenSize+20, 0,0+20,Color.BLUE);
        drawLineMiddlePoint(screenSize, screenSize+20, 0,0+20,Color.BLUE);
        drawLineMiddlePoint(screenSize, 0,0,screenSize, Color.BLUE);
        drawLineMiddlePoint(0,screenSize+20, screenSize, 0+20,Color.BLUE);
        drawLineMiddlePoint(screenSize/2,0,screenSize/2, screenSize,Color.BLUE);
        drawLineMiddlePoint(screenSize/2+20, screenSize,screenSize/2+20,0, Color.BLUE);
        drawLineMiddlePoint(0,screenSize/2, screenSize,screenSize/2,Color.BLUE);
        drawLineMiddlePoint(screenSize, screenSize/2+20,0,screenSize/2+20, Color.BLUE);
        /*
        drawLineBresenham(0,0,screenSize, screenSize, Color.BLUE);
        drawLineBresenham(screenSize, screenSize+20, 0,0+20,Color.BLUE);
        drawLineBresenham(screenSize, screenSize+20, 0,0+20,Color.BLUE);
        drawLineBresenham(screenSize, 0,0,screenSize, Color.BLUE);
        drawLineBresenham(0,screenSize+20, screenSize, 0+20,Color.BLUE);
        drawLineBresenham(screenSize/2,0,screenSize/2, screenSize, Color.BLUE);
        drawLineBresenham(screenSize/2+20, screenSize,screenSize/2+20,0, Color.BLUE);
        drawLineBresenham(0,screenSize/2, screenSize,screenSize/2, Color.BLUE);
        drawLineBresenham(screenSize, screenSize/2+20,0,screenSize/2+20, Color.BLUE);
        /*
        drawLineDAA(0,0,screenSize, screenSize, Color.BLUE);
        drawLineDAA(screenSize, screenSize+20, 0,0+20,Color.BLUE);
        drawLineDAA(screenSize, screenSize+20, 0,0+20,Color.BLUE);
        drawLineDAA(screenSize, 0,0,screenSize, Color.BLUE);
        drawLineDAA(0,screenSize+20, screenSize, 0+20,Color.BLUE);
        drawLineDAA(screenSize/2,0,screenSize/2, screenSize, Color.BLUE);
        drawLineDAA(screenSize/2+20, screenSize, screenSize/2+20,0, Color.BLUE);
        drawLineDAA(0,screenSize/2, screenSize,screenSize/2, Color.BLUE);
        drawLineDAA(screenSize, screenSize/2+20,0, screenSize/2+20, Color.BLUE);
        /*

        drawLine(0,0,screenSize, screenSize, Color.BLUE);
        drawLine(screenSize, screenSize+20, 0,0+20,Color.BLUE);
        drawLine(screenSize, 0,0,screenSize, Color.BLUE);
        drawLine(0,screenSize+20, screenSize, 0+20,Color.BLUE);
        drawLine(screenSize/2,0,screenSize/2, screenSize, Color.BLUE);
        drawLine(screenSize/2+20, screenSize, screenSize/2+20,0, Color.BLUE);
        drawLine(0,screenSize/2, screenSize,screenSize/2, Color.BLUE);
        drawLine(screenSize, screenSize/2+20,0, screenSize/2+20, Color.BLUE);
        //*/
    }

    public void floodFilling(int x, int y, Color color, Color toColor) throws AWTException {
        if (x < 0 || y < 0 || x > width - 1 || y > height - 1)
            return;
        Color tmp = new Robot().getPixelColor(x+this.getLocation().x, y+this.getLocation().y);
        if (color == null)
            color = tmp;
        if (tmp.getRGB()==color.getRGB() && tmp.getRGB()!=toColor.getRGB()) {
            drawPixel(x, y, toColor);
            floodFilling(x, y + 1, color, toColor);
            floodFilling(x + 1, y, color, toColor);
            floodFilling(x, y - 1, color, toColor);
            floodFilling(x - 1, y, color, toColor);
        }
    }
    
    public void scanlineFilling(int x, int y, Color color, Color toColor) throws AWTException {
        if (x < 0 || y < 0 || x > width - 1 || y > height - 1)
            return;
        
        Color tmp = new Robot().getPixelColor(x+this.getLocation().x, y+this.getLocation().y);
        if (color == null)
            color = tmp;
        int flag = 1;
        int xRight = x+1;
        int xLeft = x-1;
        Point pU = null;
        Point pD = null;
        while(flag>0){
            flag = 0;
            tmp = new Robot().getPixelColor(xLeft+this.getLocation().x, y+this.getLocation().y);
            if (tmp.getRGB()==color.getRGB() && tmp.getRGB()!=toColor.getRGB() && xLeft!=x) {
                drawPixel(xLeft--, y, toColor);
                flag++;
            }else
                xLeft=x;
            tmp = new Robot().getPixelColor(xRight+this.getLocation().x, y+this.getLocation().y);
            if (tmp.getRGB()==color.getRGB() && tmp.getRGB()!=toColor.getRGB() && xRight!=x) {
                drawPixel(xRight++, y, toColor);
                flag++;
            }else
                xRight=x;
            if(pU==null){
                tmp = new Robot().getPixelColor(xLeft+1+this.getLocation().x, y-1+this.getLocation().y);
                if (tmp.getRGB()==color.getRGB() && tmp.getRGB()!=toColor.getRGB() && xLeft!=x)
                    pU = new Point(xLeft+1,y-1);
                else{
                    tmp = new Robot().getPixelColor(xRight-1+this.getLocation().x, y-1+this.getLocation().y);
                    if (tmp.getRGB()==color.getRGB() && tmp.getRGB()!=toColor.getRGB() && xRight!=x)
                        pU = new Point(xRight-1,y-1);
                }
            }
            if(pD==null){
                tmp = new Robot().getPixelColor(xLeft+1+this.getLocation().x, y+1+this.getLocation().y);
                if (tmp.getRGB()==color.getRGB() && tmp.getRGB()!=toColor.getRGB() && xLeft!=x)
                    pD = new Point(xLeft+1,y+1);
                else{
                    tmp = new Robot().getPixelColor(xRight-1+this.getLocation().x, y+1+this.getLocation().y);
                    if (tmp.getRGB()==color.getRGB() && tmp.getRGB()!=toColor.getRGB() && xRight!=x)
                        pD = new Point(xRight-1,y+1);
                }
            }
        }
        if(pD!=null)
            scanlineFilling(pD.x, pD.y, color, toColor);
        if(pU!=null)
            scanlineFilling(pU.x, pU.y, color, toColor);
        drawPixel(x, y, toColor);
    }

    public void scanlineFillingW(int x, int y, Color color, Color toColor) throws AWTException {
        if (x < 0 || y < 0 || x > width - 1 || y > height - 1)
            return;
        Color tmp = new Robot().getPixelColor(x+this.getLocation().x, y+this.getLocation().y);
        if (color == null)
            color = tmp;
        drawPixel(x, y, toColor);
        int xRightU = x+1;
        int xLeftU = x-1;
        int xRightD = x+1;
        int xLeftD = x-1;
        int yUp = y;
        int yDown = y;
        int flag = 1;
        while(flag>0){
            flag = 0;
            
            tmp = new Robot().getPixelColor(xLeftU+this.getLocation().x, yUp+this.getLocation().y);
            if (tmp.getRGB()==color.getRGB() && tmp.getRGB()!=toColor.getRGB() && (xLeftU!=x || yUp!=y)) {
                drawPixel(xLeftU, yUp, toColor);
                flag++;
            }else
                xLeftU=x;
            tmp = new Robot().getPixelColor(xRightU+this.getLocation().x, yUp+this.getLocation().y);
            if (tmp.getRGB()==color.getRGB() && tmp.getRGB()!=toColor.getRGB() && (xRightU!=x || yUp!=y)) {
                drawPixel(xRightU, yUp, toColor);
                flag++;
            }else
                xRightU=x;
            int tmpI = flag;
            if(flag==0){
                yUp--;
                tmp = new Robot().getPixelColor(x+this.getLocation().x, yUp+this.getLocation().y);
                if(tmp.getRGB()!=color.getRGB() && tmp.getRGB()!=toColor.getRGB())
                    yUp=y;
            }else{
                xLeftU--;
                xRightU++;
            }              
            
            tmp = new Robot().getPixelColor(xLeftD+this.getLocation().x, yDown+this.getLocation().y);
            if (tmp.getRGB()==color.getRGB() && tmp.getRGB()!=toColor.getRGB() && (xLeftD!=x || yDown!=y)) {
                drawPixel(xLeftD, yDown, toColor);
                flag++;
            }else
                xLeftD=x;
            
            tmp = new Robot().getPixelColor(xRightD+this.getLocation().x, yDown+this.getLocation().y);
            if (tmp.getRGB()==color.getRGB() && tmp.getRGB()!=toColor.getRGB() && (xRightD!=x || yDown!=y)) {
                drawPixel(xRightD, yDown, toColor);
                flag++;
            }else
                xRightD=x;
            
            if(flag==tmpI){
                yDown++;
                tmp = new Robot().getPixelColor(x+this.getLocation().x, yDown+this.getLocation().y);
                if(tmp.getRGB()!=color.getRGB() && tmp.getRGB()!=toColor.getRGB())
                    yDown=y;
            }else{
                xLeftD--;
                xRightD++;
            }
        }
    }
    
    public void drawLine(int x1, int y1, int x2, int y2, Color color){
        if((x2-x1)==0 && (y2-y1)==0){
            drawPixel(x1, y1, color);
        }else if((x2-x1)==0){
            for (int i = y2<y1?y2:y1; i < (y2>y1?y2:y1); i++) {
                drawPixel(x1, i, color);
            }
        }else if((y2-y1)==0){
            for (int i = x2<x1?x2:x1; i < (x2>x1?x2:x1); i++) {
                drawPixel(i, y1, color);
            }
        }else{
            double m = (x2>x1?y2-y1:y1-y2)/(x2>x1?x2-x1:x1-x2);
            if(Math.abs(m)>1){
                for (int i = y2<y1?y2:y1; i < (y2>y1?y2:y1); i++) {
                    drawPixel((int)((i-(y2<y1?x2:x1))/m), i, color);
                }
            }else{
                for (int i = x2<x1?x2:x1; i < (x2>x1?x2:x1); i++) {
                    drawPixel(i, (int)((x2<x1?y2:y1)+i*m), color);
                }
            }
        }
    }

    public void drawLineMiddlePointW(int x1, int y1, int x2, int y2,Color color){
        double x = x1<x2?x1:x2;
        double y = x1<x2?y1:y2;

        y2 = x1>x2?y1:y2;
        x2 = x1>x2?x1:x2;
        x1 = (int)x;
        y1 = (int)y;

        int dx = x2 - x1;
        int dy = y2 - y1;
        int d = dy - (dx/2);
        //int x = x1, y = y1;
        drawPixel((int)x, (int)y, color);
        while (x < x2){
            x++;
            if (d < 0)
                d = d + dy;
            else{
                d += (dy - dx);
                y++;
            }

            drawPixel((int)x, (int)y, color);
        }
    }
    
    public void drawLineMiddlePoint(int x1, int y1,int x2, int y2, Color color){
        int x, y, dx, dy, pk, A, B, pxlx, pxly;
        dx = (x2 - x1);
        dy = (y2 - y1);
        if (dy < 0) { 
          dy = -dy; 
          pxly = -1; 
        } 
        else {
          pxly = 1;
        }
        if (dx < 0) {  
          dx = -dx;  
          pxlx = -1; 
        } else {
          pxlx = 1;
        }

        x = x1;
        y = y1;
        drawPixel(x1,y1,color);
        
        if(dx>dy){
          pk = 2*dy - dx;
          A = 2*dy;
          B = 2*(dy-dx);
          while (x != x2){
            x = x + pxlx;
            if (pk < 0){
              pk = pk + A;
            } else {
              y = y + pxly + 1/2;
              pk = pk + B;
            }
            drawPixel(x,y,color);
          }
        } else {
          pk = 2*dx - dy;
          A = 2*dx;
          B = 2*(dx-dy);
          while (y != y2){
            y = y + pxly + 1/2;
            if (pk < 0){
              pk = pk + A;
            } else {
              x = x + pxlx;
              pk = pk + B;
            }
            drawPixel(x,y,color);
          }
        }
    }

    public void drawLineBresenham(int x1, int y1, int x2, int y2, Color color){
        int d = 0;
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int dx2 = 2 * dx;
        int dy2 = 2 * dy;
        int ix = x1 < x2 ? 1 : -1;
        int iy = y1 < y2 ? 1 : -1;
        int x = x1;
        int y = y1;

        if (dx >= dy) {
            while (true) {
                drawPixel((int)x, (int)y, color);
                if (x == x2)
                    break;
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        }else{
            while (true) {
                drawPixel((int)x, (int)y, color);
                if (y == y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }
    }

    public void drawLineDAA(int x1, int y1, int x2, int y2, Color color){
        int deltaX = x2-x1;
        int deltaY = y2-y1;
        int steps = 0;
        if(Math.abs(deltaX)>Math.abs(deltaY))
            steps = Math.abs(deltaX);
        else
            steps = Math.abs(deltaY);
        double xInc = deltaX/steps;
        double yInc = deltaY/steps;
        double x = x1;
        double y = y1;
        drawPixel((int)x, (int)y, color);
        for (int i = 0; i < steps; i++) {
            x += xInc;
            y += yInc;
            drawPixel((int)x, (int)y, color);
        }
    }
    
    public void drawLineDAA(int x1, int y1, int x2, int y2, int width, Color color){
        int deltaX = x2-x1;
        int deltaY = y2-y1;
        int steps = 0;
        if(Math.abs(deltaX)>Math.abs(deltaY))
            steps = Math.abs(deltaX);
        else
            steps = Math.abs(deltaY);
        double xInc = deltaX/steps;
        double yInc = deltaY/steps;
        double x = x1;
        double y = y1;
        drawPixel((int)x, (int)y, width, deltaX!=0?deltaY/deltaX:2, color);
        for (int i = 0; i < steps; i++) {
            x += xInc;
            y += yInc;
            drawPixel((int)x, (int)y, width, deltaX!=0?deltaY/deltaX:2, color);
        }
    }
    public void drawLine(int x1, int y1, int x2, int y2, int[] pattern, Color color){
        if((x2-x1)==0 && (y2-y1)==0){
            drawPixel(x1, y1, color);
        }else if((x2-x1)==0){
            for (int i = y2<y1?y2:y1; i < (y2>y1?y2:y1); i++) {
                if (pattern[i % pattern.length] == 1)
                    drawPixel(x1, i, color);
            }
        }else if((y2-y1)==0){
            for (int i = x2<x1?x2:x1; i < (x2>x1?x2:x1); i++) {
                if (pattern[i % pattern.length] == 1)
                    drawPixel(i, y1, color);
            }
        }else{
            double m = (x2>x1?y2-y1:y1-y2)/(x2>x1?x2-x1:x1-x2);
            if(Math.abs(m)>1){
                for (int i = y2<y1?y2:y1; i < (y2>y1?y2:y1); i++) {
                    if (pattern[i % pattern.length] == 1)
                        drawPixel((int)((i-(y2<y1?x2:x1))/m), i, color);
                }
            }else{
                for (int i = x2<x1?x2:x1; i < (x2>x1?x2:x1); i++) {
                    if (pattern[i % pattern.length] == 1)
                        drawPixel(i, (int)((x2<x1?y2:y1)+i*m), color);
                }
            }
        }
        
    }
}
