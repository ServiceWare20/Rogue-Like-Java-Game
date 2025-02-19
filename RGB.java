import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RGB extends Game {
    public static Timer rgbTxt(int delay, int stp, JLabel messageLabel) {
        Timer rgb = new Timer(delay, new ActionListener() {
            private int red = 255;
            private int green = 0;
            private int blue = 0;
            private int step;// Color change step


            @Override
            public void actionPerformed(ActionEvent e) {
                this.step = stp;
                if (red > 0 && blue == 0) {
                    red -= step;
                    green += step;
                } else if (green > 0 && red == 0) {
                    green -= step;
                    blue += step;
                } else if (blue > 0 && green == 0) {
                    blue -= step;
                    red += step;
                }

                // Clamp values to [0, 255]
                red = Math.min(Math.max(red, 0), 255);
                green = Math.min(Math.max(green, 0), 255);
                blue = Math.min(Math.max(blue, 0), 255);

                messageLabel.setForeground(new Color(red, green, blue));
            }
        });
        return rgb;
    }

    public static Timer rgbTxt(int delay, int stp, JButton messageLabel) {
        Timer rgb = new Timer(delay, new ActionListener() {
            private int red = 255;
            private int green = 0;
            private int blue = 0;
            private int step;// Color change step


            @Override
            public void actionPerformed(ActionEvent e) {
                this.step = stp;
                if (red > 0 && blue == 0) {
                    red -= step;
                    green += step;
                } else if (green > 0 && red == 0) {
                    green -= step;
                    blue += step;
                } else if (blue > 0 && green == 0) {
                    blue -= step;
                    red += step;
                }

                // Clamp values to [0, 255]
                red = Math.min(Math.max(red, 0), 255);
                green = Math.min(Math.max(green, 0), 255);
                blue = Math.min(Math.max(blue, 0), 255);

                messageLabel.setForeground(new Color(red, green, blue));
            }
        });
        return rgb;
    }
}