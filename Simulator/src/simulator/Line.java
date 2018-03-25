package simulator;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

//------###LINE CLASS###
public class Line {
    private ArrayList<Point> line = new ArrayList<Point>();//Çizgiye ait nokta listesi
    public Connection targetCon;//hedef bağlantı
    public Component targetComp;//hedef logic devre elemanı
    public Color color;//line color.
    
    //Line constructor
    public Line(ArrayList<Point> line, Connection targetCon, Component targetComp, Color color)
    {
        this.line = line;
        this.targetComp=targetComp;
        this.targetCon= targetCon;
        this.color = color;
    }
    
    //line listesini döndür
    public ArrayList<Point> getPoints()
    {
        return line;
    }
    
    //line listesini set et
    public void setPoints(ArrayList<Point> line)
    {
        this.line = line;
    }
    
    //line listesini sıfırla
    public void reset()
    {
        line.clear();
    }
}