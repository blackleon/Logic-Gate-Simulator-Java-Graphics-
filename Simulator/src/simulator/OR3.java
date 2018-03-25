package simulator;

//------###OR3 CLASS I COMPONENTTEN MİRAS###
public class OR3 extends Component{

    //Or3 Constructor
    public OR3(int x, int y, boolean tbItem) {
        super(x, y, tbItem);
        initImage();
        in1 = new Connection(x-width/2, y-height/2, false);
        in2 = new Connection(x-width/2, y+height/2, false);
        in3 = new Connection(x-width/2, y, false);
        out = new Connection(x+width/2, y, true);
        in1.setX(in1.getX()-in1.width/2);
        in2.setX(in2.getX()-in2.width/2);
        in3.setX(in3.getX()-in2.width/2);
        out.setX(out.getX()+out.width/2);
        in1.parent=this;
        in2.parent=this;
        in3.parent=this;
        out.parent=this;
        in1.code=super.code;
        in2.code=super.code;
        in3.code=super.code;
        out.code=super.code;
    }

    //Bağlantı güncelleme
    @Override
    public void updateConnect()
    {
        in1.setX(x-in1.width);
        in1.setY(y-in1.height/2);
        in2.setX(x-in2.width);
        in2.setY(y+height/2+in2.height);
        in3.setX(x-in3.width);
        in3.setY(y+in3.height);
        out.setX(x+width);
        out.setY(y+height/2-out.height/2);
    }
    
    //Çıkış bağlantısı hesaplama
    @Override
    public void Calculate()
    {
        out.value=in1.value||in2.value||in3.value;
        calculated = true;
    }
    
    //Class Adı döndürme
    @Override
    public String getClassName()
    {
        return "simulator.OR3";
    }
    
    //Resmi güncelleme
    public void initImage()
    {
        loadImage("images\\or3.png");
        getImageDimensions();
    }
}