package ponghauki;

/**
 * Created by Rubens Santos Barbosa
 */
public class MoveButtonsCircle {
  
    private int [] occupyPoints = { 0, 0, 0, 0, 0 };
    public Buttons playerOrangeOne = null;
    public Buttons playerOrangeTwo = null;
    public Buttons playerYellowOne = null;
    public Buttons playerYellowTwo = null;
    
    public void setPlayerOrangeOne(Buttons buttonOrangeOne) {
        this.playerOrangeOne = buttonOrangeOne;
        occupyPoints[buttonOrangeOne.index] = 1;
    }
    
    public void setPlayerOrangeTwo(Buttons buttonOrangeTwo) {
        this.playerOrangeTwo = buttonOrangeTwo;
        occupyPoints[buttonOrangeTwo.index] = 1;
    }
    
    public void setPlayerYellowOne(Buttons buttonYellowOne) {
        this.playerYellowOne = buttonYellowOne;
        occupyPoints[buttonYellowOne.index] = 1;
    }
    
    public void setPlayerYellowTwo(Buttons buttonYellowTwo) {
        this.playerYellowTwo = buttonYellowTwo;
        occupyPoints[buttonYellowTwo.index] = 1;
    }
    
    public void movingCircles(Buttons b) {
        switch(b.index) {
            case 0:
                System.out.println("Entrei no 0");
                if (occupyPoints[2] == 0) {
                    swap(b, 2);
                    b.onVertically(b.btn);
                } else if (occupyPoints[4] == 0) {
                    swap(b, 4);
                    b.onAnyPosition2Center(b.btn);
                }
                break;
            case 1:
                System.out.println("Entrei no 1");
                if (occupyPoints[3] == 0) {
                    swap(b, 3);
                    b.onVertically(b.btn);
                } else if (occupyPoints[4] == 0) {
                    swap(b, 4);
                    b.onAnyPosition2Center(b.btn);
                }
                break;
            case 2:
                System.out.println("Entrei no 2");
                if (occupyPoints[0] == 0) {
                    swap(b, 0);
                    b.onVertically(b.btn);
                } else if (occupyPoints[3] == 0) {
                    swap(b, 3);
                    b.onBottomHorizontally(b.btn);
                } else if (occupyPoints[4] == 0) {
                    swap(b, 4);
                    b.onAnyPosition2Center(b.btn);
                }
                break;
            case 3:
                System.out.println("Entrei no 3");
                if (occupyPoints[1] == 0) {
                    swap(b, 1);
                    b.onVertically(b.btn);
                } else if (occupyPoints[2] == 0) {
                    swap(b, 2);
                    b.onBottomHorizontally(b.btn);
                } else if (occupyPoints[4] == 0) {
                    swap(b, 4);
                    b.onAnyPosition2Center(b.btn);
                }
                break;
            case 4:
                System.out.println("Entrei no 4");
                if (occupyPoints[0] == 0) {
                    swap(b, 0);
                    b.onCenter2TopLeft(b.btn);
                } else if (occupyPoints[1] == 0) {
                    swap(b, 1);
                    b.onCenter2TopRight(b.btn);
                } else if (occupyPoints[2] == 0) {
                    swap(b, 2);
                    b.onCenter2BottomLeft(b.btn);
                } else if (occupyPoints[3] == 0) {
                    swap(b, 3);
                    b.onCenter2BottomRight(b.btn);
                }
                break;
            default:
                break;
        }
    }  
    
    public void movePlayerOrangeOne() {
        movingCircles(this.playerOrangeOne);
    }
    
    public void movePlayerOrangeTwo() {
        movingCircles(this.playerOrangeTwo);
    }
    
    public void movePlayerYellowOne() {
        movingCircles(this.playerYellowOne);
    }
    
    public void movePlayerYellowTwo() {
        movingCircles(this.playerYellowTwo);
    }
    
    private void swap(Buttons button, int newIndex) {
        occupyPoints[button.index] = 0;
        button.index = newIndex;
        occupyPoints[button.index] = 1;
    }
    
}
