package me.rayzr522.astra.ui;

import me.rayzr522.astra.Astra;
import me.rayzr522.astra.InputManager;
import me.rayzr522.astra.Start;
import me.rayzr522.astra.Tickable;
import me.rayzr522.astra.sound.Song;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

public class UIManager implements Tickable {
    private final Astra game;
    private List<Button> buttons = new ArrayList<>();

    public UIManager(Astra game) {
        this.game = game;

        ActionListener quit = e -> saveQuitDialog();
        ActionListener reset = e -> resetDialog();
        ActionListener music = e -> musicDialog();

        ButtonLAF laf = new ButtonLAF(
                // Background color
                new Color(180, 180, 180),
                // Text color
                new Color(10, 20, 10),
                // Border color
                new Color(140, 140, 140),
                ButtonStyle.ROUND,
                2,
                new Font("Monospace", Font.PLAIN, 20)
        );

        int buttonWidth = (int) Math.floor(game.getSidebarWidth() * 0.8);
        int buttonX = (game.getSidebarWidth() - buttonWidth) / 2;

        buttons.add(new Button("Save and Quit", laf, buttonX, 180, buttonWidth, 40, quit));
        buttons.add(new Button("Reset High Scores", laf, buttonX, 240, buttonWidth, 40, reset));
        buttons.add(new Button("Change Music", laf, buttonX, 300, buttonWidth, 40, music));
    }

    public void render(Graphics2D g) {
        buttons.forEach(button -> button.render(g));
    }

    @Override
    public void tick() {
        InputManager input = game.getInputManager();
        if (!input.isMousePressed()) {
            return;
        }

        buttons.stream()
                .filter(button -> button.checkBounds(input.getMouseX(), input.getMouseY()))
                .findFirst()
                .ifPresent(Button::execute);
    }


    private void saveQuitDialog() {
        String name = JOptionPane.showInputDialog(Start.f, "Please enter your name to record your score", "Joe Shmoe");

        if (name != null) {
            if (game.save(name)) JOptionPane.showMessageDialog(Start.f, "New high-score!");
            Start.f.dispose();
            System.exit(0);
        }
    }

    private void resetDialog() {
        int reset = JOptionPane.showConfirmDialog(Start.f, "Are you sure you want to reset?", "Reset High Scores", JOptionPane.YES_NO_CANCEL_OPTION);

        if (reset == 0) {
            game.getScoreManager().getScores().clear();
            game.getScoreManager().save();
        }
    }

    private void musicDialog() {
        game.setPaused(true);
        final JFrame musicSelector = new JFrame("Select music");

        musicSelector.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent arg0) {

            }

            @Override
            public void windowIconified(WindowEvent arg0) {

            }

            @Override
            public void windowDeiconified(WindowEvent arg0) {

            }

            @Override
            public void windowDeactivated(WindowEvent arg0) {

            }

            @Override
            public void windowClosing(WindowEvent arg0) {
                game.setPaused(false);
                musicSelector.dispose();
            }

            @Override
            public void windowClosed(WindowEvent arg0) {

            }

            @Override
            public void windowActivated(WindowEvent arg0) {

            }
        });

        musicSelector.setSize(480, 480);
        musicSelector.setResizable(false);
        musicSelector.setLocationRelativeTo(null);
        musicSelector.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JPanel musicSelectorPanel = new JPanel(new SpringLayout());
        musicSelectorPanel.setLayout(new BoxLayout(musicSelectorPanel, BoxLayout.Y_AXIS));
        musicSelectorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20), "Music Selector", TitledBorder.CENTER, TitledBorder.TOP, new Font("Monospace", Font.PLAIN, 20), Color.black));

        String[] options = new String[]{
                "River", "Lazy", "Funk"
        };

        final JComboBox<String> musicList = new JComboBox<>(options);
        musicList.setFont(new Font("Monospace", Font.PLAIN, 15));
        musicList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Select a Song", TitledBorder.CENTER, TitledBorder.TOP, new Font("Monospace", Font.PLAIN, 15), Color.black));

        JButton setMusic = new JButton("Set Music");
        setMusic.addActionListener(e -> {
            String selection = (String) musicList.getSelectedItem();
            if (selection == null) {
                return;
            }

            game.setCurrentSong(Song.valueOf(selection.toUpperCase()));
            game.setPaused(false);
            musicSelector.dispose();
        });

        musicSelectorPanel.add(musicList);
        musicSelectorPanel.add(setMusic);
        musicSelector.add(musicSelectorPanel);
        musicSelector.setVisible(true);
    }
}
