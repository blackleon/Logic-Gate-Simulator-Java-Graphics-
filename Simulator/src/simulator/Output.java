package simulator;

//------###OUTPUT CLASS I COMPONENTTEN MİRAS###
public class Output extends Component{
    
    //Output constructor
    public Output(int x, int y, boolean tbItem) {
        super(x, y, tbItem);
        initImage();
        in1 = new Connection(x-width/2, y, false);
        in1.setX(in1.getX()-in1.width/2);
        in1.parent=this;
        in1.code=super.code;
    }
    
    //Bağlantı güncelleme
    @Override
    public void updateConnect()
    {
        in1.setX(x-in1.width);
        in1.setY(y+height/2-in1.height/2);
    }
    
    //Resmi hesap ile dinamik güncelleme (İki farklı durum için dinamik)
    @Override
    public void Calculate()
    {
        if(in1.value)
        {
            loadImage("images\\outon.png");
        }else{
            loadImage("images\\outoff.png");
        }
    }

    //Class Adı döndürme
    @Override
    public String getClassName()
    {
        return "simulator.Output";
    }
    
    //Resmi güncelleme (ilk çalışma)
    public void initImage()
    {
        loadImage("images\\outoff.png");
        getImageDimensions();
    }
}