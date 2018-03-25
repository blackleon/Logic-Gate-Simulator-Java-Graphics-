package simulator;

import java.util.ArrayList;

//------###RECURSİVE HESAPLAMA SINIFI###
public abstract class Calculate
{
    //Recursive metod
    /*
    Girilen output değerine bağlı logic devre elemanlarını listeye at
    bu işleme 
    ve en son bu listedeki elemanların çıktılarını bir sonraki elemanın girişine aktar.
    */
    public static int recCalculate(Component c, ArrayList<Component> list)
    {
        int success = 0;
        if(!c.calculated)
        {
            if(c.in1!=null)
            {
                if(c.in1.connected)
                {
                    list.add(c.in1.connecteds.get(0).parent);
                    success+=recCalculate(c.in1.connecteds.get(0).parent, list);
                }else{
                    success--;
                }
            }
            
            if(c.in2!=null&&c.in2.connected)
            {
                if(c.in2.connected)
                {
                    list.add(c.in2.connecteds.get(0).parent);
                    success+=recCalculate(c.in2.connecteds.get(0).parent, list);
                }else{
                    success--;
                }
            }
            
            if(c.in3!=null&&c.in3.connected)
            {
                if(c.in3.connected)
                {
                    list.add(c.in3.connecteds.get(0).parent);
                    success+=recCalculate(c.in3.connecteds.get(0).parent, list);
                }else{
                    success--;
                }
            }
        }
        
        if(c.calculated)
        {
            for(Component comp: list)
            {
                if(comp.out!=null)
                {
                    for(Connection con: comp.out.connecteds)
                    {
                        con.value = comp.out.value;
                    }
                }
            }
            
            for(Component comp: list)
            {
                comp.Calculate();
            }
        }
        return success;
    }
}