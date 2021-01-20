import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
public class example {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(500,500);
        f.setPreferredSize(new Dimension(500,500));
        f.setMinimumSize(new Dimension(500,500));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel j = new JPanel();
        j.setLayout(new BoxLayout(j, BoxLayout.X_AXIS));
        f.add(j);
        j.setBackground(new Color(170,170,170));
        j.add(Box.createHorizontalGlue());
        LoadingAnimation load = new LoadingAnimation(30, 30);
        LoadingAnimation loadv2 = new LoadingAnimation(30, 30);
        j.setBackground(new Color(0,0,0));
        j.add(load);
        j.add(Box.createHorizontalGlue());
        j.add(loadv2);
        j.add(Box.createHorizontalGlue());
        f.pack();
        f.setVisible(true);
        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch(Exception e) {}
                System.out.println("Finished");
                load.setFinishedLoading(true);
                loadv2.setFinishedLoading(false);
            }
        }; t.start();
    }
}
