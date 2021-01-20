
import javax.swing.JPanel;

import java.awt.*;
import java.util.ArrayList;

/**
 * A clean and simple loading with a completion animation
 * @author Teeds - Theo K
 */
public class LoadingAnimation extends JPanel {
    ArrayList<JPanel> parents = new ArrayList<JPanel>();
    boolean fading = false;
    boolean good = false;
    int width = 0;
    int height = 0;
    boolean[] actives = {
        false,//Loading boolean
        false, //Success Loading Boolean
        false, //checkmark alpha
        false //Complete boolean
    };
    int[] incrementSpeeds = {
        5, //Loading Speed
        7, //Successful / failed Speed
        20, //inner circle speed
        7 //checkmark speed
    };
    int[] Alphas = {
        255, //Loading Alpha
        0,   //Success Loading Alpha
        0,   //Complete Alpha
        0    //Checkmark Alpha
    };
    boolean[] steps = {
        false, //Finished Loading
        false, // Finished Completing Circle
        false, // Finished Fading Color
        false // Finished Checkmark
    };
    int Alpha = 0;
    int FinishedAlpha = 0;
    int Position = 0;
    int Size = 40;
    int circleSize = 0;
    /**
     * Creates the loading animation object
     * @param width width of the animation
     * @param height height of the animation
     */
    public LoadingAnimation(int width, int height) {
        this.width = width - 4;
        this.height = height - 4;
        setPreferredSize(new Dimension(width,height));
        setMaximumSize(new Dimension(width,height));
        // setOpaque(false);
        setBackground(new Color(0,0,0));
        startAnimation();
    }

    /**
     * sets the parent panels that will be repainted
     * @param panels list of jpanels to be added to a repaint list
     */
    public void setParents(JPanel... panels) {
        for(JPanel p : panels) {
            parents.add(p);
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(new Color(255,255,255, Alphas[0]));
        graphics.setStroke(new BasicStroke(1.8f));
        graphics.drawArc(2, 2, width, height, Position, Size);
        Color c;
        if(good) {
            c = new Color(49, 206, 125);
        } else {
            c = new Color(210, 49, 49);
        }
        if(steps[0]) {
            graphics.setStroke(new BasicStroke(1.8f));
            graphics.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), Alphas[1]));
            graphics.drawArc(2, 2, width, height, Position, Size);
        }
        if(steps[1]) {
            graphics.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), Alphas[2]));
            graphics.fillOval((width / 2) - (circleSize / 2) + 2,  (height / 2) - (circleSize / 2) + 2, circleSize, circleSize);
        }
        if(steps[2]) {
            graphics.setColor(new Color(255, 255, 255, Alphas[3]));
            if(good) {
                graphics.setStroke(new BasicStroke(2.7f));

                graphics.drawLine(getPoint(3.8) + 2, getPoint(1.9) + 2, getPoint(2.1) + 2, getPoint(1.4) + 2);
                graphics.drawLine(getPoint(2.1) + 2, getPoint(1.4) + 2,  getPoint(1.3) + 2, getPoint(3.2) + 2);

            } else {
                graphics.setStroke(new BasicStroke(2.2f));

                graphics.drawLine(getPoint(3.4) + 2, getPoint(3.4) + 2, getPoint(1.4) + 2, getPoint(1.5) + 2);
                graphics.drawLine(getPoint(3.4) + 2, getPoint(1.5) + 2,  getPoint(1.4) + 2, getPoint(3.4) + 2);
            }
        }
    }

    /**
     * @param percent the position the given point should be at
     * @return
     */
    private int getPoint(double percent) {
        return (int) (width / percent);
    }
    /**
     * Finishes the loading animation
     * @param b wether or not it was successful
     */
    public void setFinishedLoading(boolean b) {
        this.good = b;
        this.steps[0] = true;
        while(Size < 360) {
            Position-=11;
            Size+=7;
            update();
            try {
                Thread.sleep(15);
            } catch(Exception err1) {}
        }
    }

    /**
     * Starts the loading animation
     */
    public void startAnimation() {
        Thread t = new Thread() {
            public void run() {
                steps[0] = false;
                boolean add = true;
                while(!steps[0]) {
                    Position-=5;
                    if(add) {
                        Size+=2;
                    } else {
                        Size-=2;
                    }
                    if(Size > 180 || Size < 20) {
                        add = !add;
                    }
                    update();
                    try {
                        sleep(15);
                    } catch(Exception err1) {}
                }
                completeLoading();
            }
        };t.start();
    }

    /**
     * The called method for when the loading animation should be completed
     */
    public void completeLoading() {
        changeAlpha(true, 1, 255);
        changeAlpha(false, 0, 0);
        while(Alphas[1] < 250) {
            try {
                Thread.sleep(10);
            } catch(Exception e) {}
        }
        steps[1] = true;
        changeAlpha(true, 2, 255);
        IncreaseSize circle = new IncreaseSize(0, (width + 1));
        circle.setIncrement(1);
        circle.setSleepTime(10);
        
        while(circle.getSize() < (width + 1)) {
            this.circleSize = circle.getIncrementSize();
            repaint();
        }
        steps[2] = true;
        changeAlpha(true, 3, 255);
        update();
    }

    /**
     * repaints / revalidates this panel and any parent panels provided
     */
    public void update() {
        repaint();
        revalidate();
        for(JPanel p : parents) {
            p.repaint();
            p.revalidate();
        }
    }
    /**
     * fades the color in
     */
    public void fadeIn() {
        Thread t = new Thread() {
            public void run() {
                fading = true;
                while(Alpha < 255 && fading) {
                    Alpha += 5;
                    update();
                    try { 
                        sleep(10);
                    } catch(Exception err1) {}
                }
            }
        }; t.start();
    }

    /**
     * Changes the alpha 
     * @param increase increasing or decreasing alpha
     * @param position the position the alpha is in the array
     * @param wantedAlpha the desired alpha amount
     */
    public void changeAlpha(boolean increase, int position, int wantedAlpha) {
        Thread t = new Thread() {
            public void run() {
                actives[position] = increase;
                while(increase == actives[position] && Alphas[position] != wantedAlpha) {
                    if(increase) {
                        Alphas[position]+=incrementSpeeds[position];
                    } else {
                        Alphas[position]-=incrementSpeeds[position];
                    }
                    Alphas[position] = round(Alphas[position], wantedAlpha);
                    update();
                    try { 
                        sleep(10);
                    } catch(Exception err1) {}
                }
            }
        }; t.start();
    }

    /**
     * Rounds to the closest amount
     * @param current the current alpha
     * @param wanted the wanted alpha
     * @return the updated alpha
     */
    private int round(int current, int wanted) {
        if(Math.abs(wanted - current) < 10) {
            return wanted;
        }
        return current;
    }
    /**
     * fades the color out
     */
    public void fadeOut() {
        Thread t = new Thread() {
            public void run() {
                fading = false;
                while(Alpha > 0 && !fading) {
                    Alpha -= 5;
                    update();
                    try { 
                        sleep(5);
                    } catch(Exception err1) {}
                }
            }
        }; t.start();
    }
}
