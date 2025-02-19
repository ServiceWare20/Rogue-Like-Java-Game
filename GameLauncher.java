import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameLauncher extends JFrame {

    public void launcher(Grid grid, Game game) {
        // Load accounts from the JSON file
        ArrayList<Account> accounts = JsonInput.deserializeAccounts();

        if (accounts == null || accounts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No accounts found. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Display the login interface
        Account loggedInAccount = login(accounts);
        if (loggedInAccount == null) {
            JOptionPane.showMessageDialog(null, "Login failed. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Display the character selection interface
        Character selectedCharacter = selectCharacter(loggedInAccount);
        if (selectedCharacter == null) {
            JOptionPane.showMessageDialog(null, "No character selected. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Start the game with the selected character
        startGame(selectedCharacter, grid, game);
    }
//ionutpaiusan@yahoo.com

    private Account login(ArrayList<Account> accounts) {

        JFrame.isDefaultLookAndFeelDecorated();
        JFrame loginFrame = new JFrame("League Of Warriors - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(800, 800);
        loginFrame.setUndecorated(true);
        loginFrame.setBackground(new Color(0, 0, 0, 0)); // Transparent JFrame background
        // Transparent main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);

        // Fully opaque content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(true);
        contentPanel.setBackground(Color.DARK_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        contentPanel.add(loginButton, gbc);

        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        messageLabel.setText("This game is sponsored by my sweat and hard work");
        contentPanel.add(messageLabel, gbc);
        //RGB color Cycle
        Timer colorCycleTimer = RGB.rgbTxt(100, 25, messageLabel);
        //Start the RGB
        colorCycleTimer.start();


        mainPanel.add(contentPanel);
        loginFrame.add(mainPanel);

        final Account[] loggedInAccount = {null};
        //Verify credentials
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Exception ClassNotFoundException;
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                loggedInAccount[0] = authenticate(accounts, email, password);

                if (loggedInAccount[0] != null) {
                    colorCycleTimer.stop();
                    JOptionPane.showMessageDialog(loginFrame, "Login successful! Welcome, " + loggedInAccount[0].information.name + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loginFrame.dispose();
                } else {
                    colorCycleTimer.stop();
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Invalid credentials. Please try again.");
                }
            }

        });

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);

        while (loginFrame.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        return loggedInAccount[0];
    }

    private Account authenticate(ArrayList<Account> accounts, String email, String password) {
        for (Account account : accounts) {
            Account.Credentials credentials = account.information.credentials;
            if (credentials != null && credentials.email.equals(email) && credentials.password.equals(password)) {
                return account;
            }
        }
        return null;
    }

    private Character selectCharacter(Account account) {
        JFrame.isDefaultLookAndFeelDecorated();
        JFrame characterFrame = new JFrame("Select Your Character");
        characterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        characterFrame.setSize(800, 400);
        characterFrame.setLayout(new BorderLayout());


        characterFrame.setUndecorated(true);
        characterFrame.setBackground(new Color(0, 0, 0, 0)); // Transparent JFrame background

//            characterFrame.setOpacity(1.0f);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(true);
        contentPanel.setBackground(Color.DARK_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel instructionLabel = new JLabel("Select a character:", SwingConstants.CENTER);
        instructionLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(instructionLabel, gbc);

        DefaultListModel<String> characterListModel = new DefaultListModel<>();
        for (Character character : account.characters) {
            characterListModel.addElement(character.name + " - " + character.profession + ", Level: " + character.level);
        }

        JList<String> characterList = new JList<>(characterListModel);
        characterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(characterList);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        contentPanel.add(scrollPane, gbc);

        JButton selectButton = new JButton("Select");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        contentPanel.add(selectButton, gbc);

        mainPanel.add(contentPanel);
        characterFrame.add(mainPanel, BorderLayout.CENTER);

        final Character[] selectedCharacter = {null};

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = characterList.getSelectedIndex();
                if (selectedIndex != -1) {
                    selectedCharacter[0] = account.characters.get(selectedIndex);
                    JOptionPane.showMessageDialog(characterFrame, "You have selected " + selectedCharacter[0].name + ", the " + selectedCharacter[0].profession + ".", "Character Selected", JOptionPane.INFORMATION_MESSAGE);
                    characterFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(characterFrame, "Please select a character.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        characterFrame.setLocationRelativeTo(null);
        characterFrame.setVisible(true);

        while (characterFrame.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        return selectedCharacter[0];
    }

    private void startGame(Character character, Grid grid, Game game) {
        JOptionPane.showMessageDialog(null, "Starting the game with " + character.name + "... Have fun!", "Game Starting", JOptionPane.INFORMATION_MESSAGE);
        grid.generateMap(false, character); // Set hardcode to true to test the homework

        game.playMusic("D:\\UPB\\Anul2\\POO\\Tema1\\src\\LowOST.wav");
        Game.showMap();
        setFocusable(true); // Ensure the component is focusable
        requestFocusInWindow(); // Request focus so the KeyListener works
    }
}