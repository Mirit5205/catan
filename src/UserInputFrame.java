import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInputFrame extends JFrame {
    private JTextField textField = new JTextField();
    private JButton button;
    private String userInput = "";
    private boolean isActionPreformed = false;


    public UserInputFrame() {
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // better to use
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setLocationRelativeTo(null);  // better to use..
        setLocationByPlatform(true);
        setTitle("Choose Card: ");
        setResizable(false);
        //setLayout(null); // better to use layouts with padding & borders

        textField.setBounds(30, 30, 180, 40);
        button = new JButton("Use Card");
        button.setBounds(70, 100, 100, 40);
        this.add(button);
        this.add(textField);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userInput = textField.getText();
                isActionPreformed = true;
                setVisible(false);
                dispose();
            }
        });
        this.setSize(250, 200);
        this.setLayout(null);
        this.setVisible(true);
    }

    public String getUserInput() {
        return this.userInput;
    }

    public boolean getIsActionPreformed() {
        return this.isActionPreformed;
    }
}

