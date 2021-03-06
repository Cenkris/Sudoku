package Application.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Game extends JFrame {
    private JPanel basePanel = new JPanel(new FlowLayout());
    private JPanel gamePanel = new JPanel(new GridLayout(3, 3, 7, 7));
    private JPanel extrasPanel = new JPanel(new BorderLayout());
    private final Map<String, JTextField> storage = new HashMap<>();
    private final Deque<JTextField> highlightedCells = new ArrayDeque<>();
    private JCheckBoxMenuItem highlightMenuItem;

    public Game() {
        initGameFields();
        initGamePanel();
        initGameInsertButtons();
        initOptionsMenu();
        initDefaultValues();
    }

    private void initOptionsMenu() {
        JMenuBar optionsMenuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenu extrasMenu = new JMenu("Extras");
        highlightMenuItem = new JCheckBoxMenuItem("Highlight rows and columns");
        JMenuItem clearMenuItem = new JMenuItem("Clear");
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Clickes");
                for(String number : storage.keySet()){
                    storage.get(number).setText("");
                }
            }
        };
        clearMenuItem.addMouseListener(mouseAdapter);
        menu.add(clearMenuItem);
        extrasMenu.add(highlightMenuItem);
        menu.add(extrasMenu);
        optionsMenuBar.add(menu);
        setJMenuBar(optionsMenuBar);
    }

    private void initGameInsertButtons() {
        JPanel insertButtonsPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.setPreferredSize(new Dimension(50, 50));
            insertButtonsPanel.add(button);
        }
        extrasPanel.add(insertButtonsPanel, BorderLayout.SOUTH);

    }

    private void initGameFields() {
        for (int i = 1; i <= 9; i++) {
            JPanel smallGamePanel = new JPanel(new GridLayout(3, 3, 1, 1));
            for (int j = 1; j <= 9; j++) {
                JTextField textField = new JTextField();
                Font font = textField.getFont();
                textFieldNameSetter(textField, i, j);
                textField.setFont(new Font(font.getName(), Font.PLAIN, 40));
                textField.setHorizontalAlignment(SwingConstants.CENTER);
                textField.setEditable(false);
                textField.setHighlighter(null);
                textField.setFocusable(true);
                textField.setPreferredSize(new Dimension(100, 100));
                Color background = textField.getBackground();
                KeyAdapter keyPressedAdapter = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        int pressedKeyCode = e.getKeyCode();
                        if (pressedKeyCode >= 49 && pressedKeyCode <= 57) {
                            textField.setText(String.valueOf((char) pressedKeyCode));
                        }
                        if (pressedKeyCode == 8) {
                            textField.setText("");
                        }
                    }
                };
                textField.addKeyListener(keyPressedAdapter);

                textField.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (highlightMenuItem.getState()) {
                            highlight(e);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        textField.setBackground(new Color(25500000));
                        Component selectedComponent = e.getComponent();
                        if (selectedComponent instanceof JTextField) {
                            JTextField selectedTextField = (JTextField) selectedComponent;
                            selectedTextField.requestFocus();
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        while (!highlightedCells.isEmpty()) {
                            highlightedCells.pop().setBackground(background);
                        }
                        textField.setBackground(background);
                    }
                });
                smallGamePanel.add(textField);
                storage.put(textField.getName(), textField);
            }
            gamePanel.add(smallGamePanel);
        }
    }

    private void highlight(MouseEvent event) {
        Component component = event.getComponent();
        String squareName = component.getName();
        String squareNumber = squareName.split(" ")[1];
        String squareLetter = squareName.split(" ")[0];

        for (String number : storage.keySet()) {
            String vertical = number.split(" ")[1];
            String horizontal = number.split(" ")[0];

            if (vertical.equals(squareNumber)) {
                JTextField field = storage.get(number);
                field.setBackground(new Color(9990762));
                highlightedCells.add(field);
            }

            if (horizontal.equals(squareLetter)) {
                JTextField field = storage.get(number);
                field.setBackground(new Color(9990762));
                highlightedCells.add(field);
            }
        }
    }

    private void textFieldNameSetter(JTextField textField, int i, int j) {
        //TODO: fix this mess
        //make rows A-I, columns 1-9
        String name = "";
        if (i <= 3) {
            if (j <= 3) {
                name = "A";
            } else if (j <= 6) {
                name = "B";
            } else {
                name = "C";
            }
        } else if (i <= 6) {
            if (j <= 3) {
                name = "D";
            } else if (j <= 6) {
                name = "E";
            } else {
                name = "F";
            }
        } else {
            if (j <= 3) {
                name = "G";
            } else if (j <= 6) {
                name = "H";
            } else {
                name = "I";
            }
        }


        if (i % 3 == 1) {
            if (j % 3 == 0) {
                name += " 3";
            } else if (j % 3 == 1) {
                name += " 1";
            } else {
                name += " 2";
            }
        } else if (i % 3 == 0) {
            if (j % 3 == 0) {
                name += " 9";
            } else if (j % 3 == 1) {
                name += " 7";
            } else {
                name += " 8";
            }
        } else {
            if (j % 3 == 0) {
                name += " 6";
            } else if (j % 3 == 1) {
                name += " 4";
            } else {
                name += " 5";
            }
        }


        textField.setName(name);
    }

    private void initGamePanel() {
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(1, 900));
        separator.setOrientation(SwingConstants.VERTICAL);
        basePanel.add(gamePanel);
    }

    private void initDefaultValues() {
        setTitle("Sudoku");
        setResizable(false);
        add(basePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
}
