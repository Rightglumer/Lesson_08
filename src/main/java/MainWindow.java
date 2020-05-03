import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    JButton btnNewGame;
    JButton btnSettings;
    JPanel pnlButtons;
    GameLogic battleField;
    SettingsWindow settingsWindow = new SettingsWindow();

    public MainWindow() {
        // window show
        setTitle("Крестики-нолики");
        setBounds(100, 100, 313, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        pnlButtons = new JPanel();
        pnlButtons.setPreferredSize(new Dimension(100, 64));
        pnlButtons.setLayout(new BorderLayout());
        pnlButtons.setBackground(Color.DARK_GRAY);
        add(pnlButtons, BorderLayout.SOUTH);
        btnNewGame = new JButton("Новая игра");
        btnNewGame.addActionListener(actionEvent -> {
            battleField.startNewGame(settingsWindow.getFieldSize(), settingsWindow.getWinRow());
        });
        pnlButtons.add(btnNewGame, BorderLayout.WEST);
        btnSettings = new JButton("Настройки");
        pnlButtons.add(btnSettings, BorderLayout.EAST);
        btnSettings.addActionListener(actionEvent -> {
            settingsWindow.showModal();
            if (settingsWindow.modalResultOK){
                battleField.startNewGame(settingsWindow.getFieldSize(), settingsWindow.getWinRow());
            }
        });

        battleField = new GameLogic(settingsWindow.getFieldSize(), settingsWindow.getWinRow());
        add(battleField, BorderLayout.CENTER);

        setVisible(true);
    }
}
