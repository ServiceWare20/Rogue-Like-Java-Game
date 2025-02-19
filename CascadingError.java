import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class CascadingError extends Game{
    public CascadingError(String msj, String title) {
        SwingUtilities.invokeLater(() -> showErrorDialog(0, 700, msj, title)); // Start with 10 dialogs
    }

    public static void showErrorDialog(int count, int max, String error, String title) {
        if (count >= max) return; // Stop recursion after max dialogs

        // Create the dialog
        JWindow window = new JWindow();
        window.setLayout(new BorderLayout());
        window.setSize(500, 150);

        JLabel label = new JLabel(error, JLabel.CENTER);
        label.setBackground(Color.darkGray);
        label.setFont(new Font("Arial", Font.BOLD, 32));
        label.setForeground(Color.RED);
        window.add(label, BorderLayout.CENTER);

        Random rdm = new Random();
        // Position the dialog in a cascading pattern
        int x = rdm.nextInt(50, 900); // Shift 20px horizontally
        int y = rdm.nextInt(50, 900); // Shift 20px vertically
        window.setLocation(x, y);
        window.setFocusable(true);

        // Make the window visible
        window.setVisible(true);

        // Automatically close the window after 100ms
        Timer timer = new Timer(800, e -> {
            window.dispose(); // Close the window
        });
        timer.setRepeats(false); // Execute only once
        timer.start();

        // Spawn the next dialog at an offset position
        SwingUtilities.invokeLater(() -> {
            JFrame nextFrame = new JFrame();
            showErrorDialog(count + 1, max, error, title);
        });
    }
}
