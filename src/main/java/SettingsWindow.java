import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

public class SettingsWindow extends JDialog {
    public static final int BTN_WIDTH = 128;
    public static final int BTN_HEIGHT = 40;

    public boolean modalResultOK;

    SpringLayout layout = new SpringLayout();
    Container contentPane = this.getContentPane();
    JButton btnOK;
    JButton btnCancel;
    SpringUtility springUtility;
    ButtonGroup buttonGroup;
    JRadioButton rbHumanHuman;
    JRadioButton rbHumanComputer;
    JLabel lblGameType;
    JLabel lblFieldSize;
    JSlider slFieldSize;
    JLabel lblWinRow;
    JSlider slWinRow;
    Dimension dmWinRow;

    boolean isHumanHuman;
    int curFieldSize;
    int curWinRow;

    public int getFieldSize(){
        return slFieldSize.getValue();
    }

    public int getWinRow(){
        return slWinRow.getValue();
    }

    protected void closeWindowWOSave(){
        rbHumanHuman.setSelected(isHumanHuman);
        rbHumanComputer.setSelected(!isHumanHuman);
        slFieldSize.setValue(curFieldSize);
        slWinRow.setValue(curWinRow);
        modalResultOK = false;
        dispose();
    }

    public SettingsWindow(){
        // window show
        setModal(true);
        setTitle("Настройки");
        setBounds(100, 100, 500, 330);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(layout);
        isHumanHuman = false;
        curFieldSize = 3;
        curWinRow = 3;

        // create buttons
        btnOK = new JButton("OK");
        btnOK.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        btnOK.addActionListener(actionEvent -> {
            isHumanHuman = rbHumanHuman.isSelected();
            curFieldSize = slFieldSize.getValue();
            curWinRow = slWinRow.getValue();
            modalResultOK = true;
            dispose();
        });
        btnCancel = new JButton("Отмена");
        btnCancel.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        btnCancel.addActionListener(actionEvent -> {
            closeWindowWOSave();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindowWOSave();
            }
        });

        // create modificator
        lblGameType = new JLabel("Режим игры:");
        lblGameType.setPreferredSize(new Dimension(200, 32));
        buttonGroup = new ButtonGroup();
        rbHumanComputer = new JRadioButton("Человек против компьютера");
        rbHumanHuman = new JRadioButton("Человек против человека");
        rbHumanHuman.setEnabled(false);
        rbHumanComputer.setSelected(true);
        buttonGroup.add(rbHumanComputer);
        buttonGroup.add(rbHumanHuman);

        lblFieldSize = new JLabel("Размерность поля:");
        lblFieldSize.setPreferredSize(new Dimension(200, 32));
        lblFieldSize.setVerticalAlignment(JLabel.BOTTOM);
        slFieldSize = new JSlider(JSlider.HORIZONTAL, 3, 10, 3);
        slFieldSize.setSnapToTicks(true);
        slFieldSize.setMajorTickSpacing(1);
        slFieldSize.setPaintTicks(true);
        slFieldSize.setPreferredSize(new Dimension(400, 64));
        Hashtable labelTable = new Hashtable();
        for (int i = 3; i <= 10; i++){
            labelTable.put(i, new JLabel(String.valueOf(i) + "x" + String.valueOf(i)));
        }
        slFieldSize.setLabelTable( labelTable );
        slFieldSize.setPaintLabels(true);

        lblWinRow = new JLabel("Победный ряд:");
        lblWinRow.setPreferredSize(new Dimension(200, 32));
        lblWinRow.setVerticalAlignment(JLabel.BOTTOM);

        lblFieldSize = new JLabel("Размерность поля:");
        lblFieldSize.setPreferredSize(new Dimension(200, 32));
        lblFieldSize.setVerticalAlignment(JLabel.BOTTOM);
        slWinRow = new JSlider(JSlider.HORIZONTAL, 3, 10, 3);
        dmWinRow = new Dimension();
        dmWinRow.height = 64;
        dmWinRow.width = 30;
        slWinRow.setSnapToTicks(true);
        slWinRow.setMajorTickSpacing(1);
        slWinRow.setPaintTicks(true);
        Hashtable labelTable2 = new Hashtable();
        for (int i = 3; i <= 10; i++){
            labelTable2.put(i, new JLabel(String.valueOf(i)));
        }
        slWinRow.setLabelTable(labelTable2);
        slWinRow.setPaintLabels(true);

        slWinRow.setMaximum(3);
        slWinRow.setPreferredSize(dmWinRow);
        slFieldSize.addChangeListener(changeEvent -> {
                int value = slFieldSize.getValue();
                slWinRow.setMaximum(value);
                dmWinRow.width = (value - 2) * 30;
                slWinRow.setPreferredSize(dmWinRow);
                slWinRow.setVisible(false);
                slWinRow.setVisible(true);
        });

        springUtility = new SpringUtility(layout, contentPane);

        springUtility.setRelative(btnOK, contentPane, contentPane, SpringUtility.SU_BOTTOMRIGHT_BASE);
        springUtility.setRelative(btnCancel, contentPane, btnOK, SpringUtility.SU_BOTTOMBASE_RIGHT);
        springUtility.setRelative(lblGameType, contentPane, contentPane, SpringUtility.SU_TOPLEFT_BASE);
        springUtility.setRelative(rbHumanComputer, lblGameType, contentPane, SpringUtility.SU_TOP_LEFTBASE);
        springUtility.setRelative(rbHumanHuman, lblGameType, rbHumanComputer, SpringUtility.SU_TOPLEFT);
        springUtility.setRelative(lblFieldSize, rbHumanComputer, contentPane, SpringUtility.SU_TOP_LEFTBASE);
        springUtility.setRelative(slFieldSize, lblFieldSize, contentPane, SpringUtility.SU_TOP_LEFTBASE);
        springUtility.setRelative(lblWinRow, slFieldSize, contentPane, SpringUtility.SU_TOP_LEFTBASE);
        springUtility.setRelative(slWinRow, lblWinRow, contentPane, SpringUtility.SU_TOP_LEFTBASE);
    }

    public void showModal(){
        setVisible(true);
    }
}
