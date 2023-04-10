
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class UserImpl implements User {

    private JTextArea textArea;
    public JTextField textField;
    public Frame frame;

    public Forum forum;
    public int clientId=-1;

    public UserImpl() {
        // Create the GUI
        frame = new JFrame("IRC Chat Client");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);

        textArea = new JTextArea(10, 60);
        textArea.setEditable(false);
        textArea.setForeground(Color.BLACK);
        textArea.setBackground(Color.WHITE);
    JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        textField = new JTextField(60);
        textField.setBackground(Color.WHITE);
        JLabel label = new JLabel("Enter your message below");
        label.setForeground(Color.GRAY);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(label, BorderLayout.NORTH);
        southPanel.add(textField, BorderLayout.SOUTH);
        frame.add(southPanel, BorderLayout.SOUTH);

       // frame.add(textField, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

    JButton  connectButton = new JButton("Connect");
        connectButton.setBackground(new Color(0, 204, 102));
        connectButton.addActionListener(new connectListener(this));
        buttonPanel.add(connectButton);

        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.setBackground(new Color(255, 102, 102));
        disconnectButton.addActionListener(new leaveListener(this));
        buttonPanel.add(disconnectButton);

        JButton whoButton = new JButton("Who is Online");
        whoButton.addActionListener(new whoListener(this));
        whoButton.setBackground(new Color(255, 255, 102));
        buttonPanel.add(whoButton);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new writeListener(this));
        sendButton.setBackground(new Color(102, 204, 255));
        buttonPanel.add(sendButton);

        frame.add(buttonPanel, BorderLayout.NORTH);

        frame.setSize(470, 300);
        frame.setFont(new Font("Open Sans", Font.PLAIN, 12));
        textField.setFont(new Font("Open Sans", Font.PLAIN, 12));

        textField.setForeground(Color.BLACK);


        textArea.setBackground(Color.lightGray);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(600, 300);
        frame.setVisible(true);
    }

    public void ecrire(String msg) {
        try {
            this.textArea.append(msg + "\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    public static void main(String args[]) {
        new UserImpl();
    }
}

class connectListener implements ActionListener {
    UserImpl irc;

    public connectListener(UserImpl i) {
        irc = i;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println("//" + irc.textField.getText() + "/IRCServer");
            Forum server = (Forum) Naming.lookup("//" + irc.textField.getText() + "/IRCServer");
            irc.forum = server;
            Proxy c = new ProxyImpl(irc);
            irc.clientId = irc.forum.entrer(c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

class writeListener implements ActionListener {
    UserImpl irc;

    public writeListener(UserImpl i) {
        irc = i;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if(irc.clientId!=-1)
            irc.forum.dire(irc.clientId, irc.textField.getText());
            else JOptionPane.showMessageDialog(irc.frame, "You are not connected to the server");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
}

class whoListener implements ActionListener {
    UserImpl irc;

    public whoListener(UserImpl i) {
        irc = i;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if(irc.clientId!=-1)
            irc.forum.qui();
            else JOptionPane.showMessageDialog(irc.frame, "You are not connected to the server");
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }
}

class leaveListener implements ActionListener {
    UserImpl irc;

    public leaveListener(UserImpl i) {
        irc = i;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if(irc.clientId!=-1)
            irc.forum.quitter(irc.clientId);
            else JOptionPane.showMessageDialog(irc.frame, "You are not connected to the server");

        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
}