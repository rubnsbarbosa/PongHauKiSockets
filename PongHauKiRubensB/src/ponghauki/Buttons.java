package ponghauki;

import java.awt.Point;
import javax.swing.JButton;

/**
 * Created by Rubens Santos Barbosa
 */
public class Buttons implements moveCircles {
    public JButton btn;
    public int index;
        
    public Buttons(JButton btn, int index) {
        this.btn = btn;
        this.index = index;
    }
    
    private short FLAG = 9000; // Can't move
    
    @Override
    public void onVertically(JButton button) {
        Point pt = button.getLocation();
        //System.out.println(pt);
        int x1 = pt.x;
        int y1 = pt.y;
        if (y1 == 100 && FLAG == 9000) {
            button.setLocation(x1, 320);
            FLAG = 9001;
        } else if (y1 == 320 && FLAG == 9000) {
            button.setLocation(x1, 100);
            FLAG = 9001;
        }
    }

    @Override
    public void onBottomHorizontally(JButton button) {
        Point pt = button.getLocation();
        //System.out.println(pt);
        int x1 = pt.x;
        int y1 = pt.y;
        if (x1 == 70 && y1 == 320 && FLAG == 9000) {
            button.setLocation(456, y1);
            FLAG = 9001;
        } else if (x1 == 456 && y1 == 320 && FLAG == 9000) {
            button.setLocation(70, y1);
            FLAG = 9001;
        }
    }

    @Override
    public void onAnyPosition2Center(JButton button) {
        button.setLocation(260, 200);
    }

    @Override
    public void onCenter2BottomLeft(JButton button) {
        button.setLocation(70, 320);
    }

    @Override
    public void onCenter2BottomRight(JButton button) {
        button.setLocation(456, 320);
    }

    @Override
    public void onCenter2TopLeft(JButton button) {
        button.setLocation(70, 100);
    }

    @Override
    public void onCenter2TopRight(JButton button) {
        button.setLocation(456, 100);
    }

    @Override
    public void startingPositionOrangeOne(JButton buttonOrange1) {
        buttonOrange1.setLocation(70, 100);
    }
    
    @Override
    public void startingPositionOrangeTwo(JButton buttonOrange2) {
        buttonOrange2.setLocation(456, 100);
    }
    
    @Override
    public void startingPositionYellowOne(JButton buttonYellow1) {
        buttonYellow1.setLocation(70, 320);
    }
    
    @Override
    public void startingPositionYellowTwo(JButton buttonYellow2) {
        buttonYellow2.setLocation(456, 320);
    }
   
}
