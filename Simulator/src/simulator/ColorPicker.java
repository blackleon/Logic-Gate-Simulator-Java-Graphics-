package simulator;

import java.awt.Color;

//------###COLORPICKER BUTONU BUTTON DAN MİRAS###
public class ColorPicker extends Button
{
    private Color colColor;//Renk seçicinin rengi
    private String sColor;//Renk string
    
    //Colorpicker contructor
    public ColorPicker(int x, int y, String sColor, Color colColor)
    {
        super(x,y);
        this.sColor = sColor;
        this.colColor = colColor;
        initImage();
    }
    
    //Resmi güncelle
    public void initImage()
    {
        sColor=String.format("images\\%s.png", sColor);
        loadImage(sColor);
        getImageDimensions();
    }
    
    //Rengi döndür
    public Color getColor()
    {
        return colColor;
    }
}
