package ponghauki;

import javax.swing.JButton;

/**
 * Created by Rubens Santos Barbosa
 */
public interface moveCircles {
    void onVertically(JButton button);
    void onBottomHorizontally(JButton button);
    void onAnyPosition2Center(JButton button);
    void onCenter2BottomLeft(JButton button);
    void onCenter2BottomRight(JButton button);
    void onCenter2TopLeft(JButton button);
    void onCenter2TopRight(JButton button);
    void startingPositionOrangeOne(JButton button);
    void startingPositionOrangeTwo(JButton button);
    void startingPositionYellowOne(JButton button);
    void startingPositionYellowTwo(JButton button);
}
