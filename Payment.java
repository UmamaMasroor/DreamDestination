import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Payment extends JFrame implements ActionListener {

    JButton paytmBtn, googlePayBtn, amazonPayBtn, backBtn, showHistoryBtn, confirmBtn;
    JTextArea historyArea;
    JTextField amountField;
    JLabel amountLabel, title;
    StringBuilder paymentHistory;
    String selectedMethod = null;

    Payment() {
        setTitle("Payment Methods");
        setBounds(200, 100, 300, 400);
        setLayout(null);
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        paymentHistory = new StringBuilder();

        // Title Label
        title = new JLabel("Select a Payment Method");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(180, 20, 300, 30);
        title.setForeground(Color.BLACK);
        add(title);

        // Payment Buttons
        paytmBtn = new JButton("Paytm");
        paytmBtn.setBounds(50, 80, 150, 50);
        paytmBtn.setBackground(new Color(173, 216, 230)); // Light blue
        paytmBtn.setForeground(Color.BLACK);
        paytmBtn.addActionListener(this);
        add(paytmBtn);

        googlePayBtn = new JButton("Google Pay");
        googlePayBtn.setBounds(220, 80, 150, 50);
        googlePayBtn.setBackground(new Color(173, 216, 230));
        googlePayBtn.setForeground(Color.BLACK);
        googlePayBtn.addActionListener(this);
        add(googlePayBtn);

        amazonPayBtn = new JButton("Amazon Pay");
        amazonPayBtn.setBounds(390, 80, 150, 50);
        amazonPayBtn.setBackground(new Color(173, 216, 230));
        amazonPayBtn.setForeground(Color.BLACK);
        amazonPayBtn.addActionListener(this);
        add(amazonPayBtn);

        // Amount Input
        amountLabel = new JLabel("Enter Amount:");
        amountLabel.setBounds(50, 160, 150, 30);
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        amountLabel.setForeground(Color.BLACK);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(180, 160, 150, 30);
        add(amountField);

        // Confirm Button
        confirmBtn = new JButton("Confirm Payment");
        confirmBtn.setBounds(350, 160, 180, 30);
        confirmBtn.setBackground(new Color(60, 179, 113)); // Green
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.addActionListener(this);
        add(confirmBtn);

        // Show History Button
        showHistoryBtn = new JButton("Show Payment History");
        showHistoryBtn.setBounds(180, 210, 220, 50);
        showHistoryBtn.setBackground(new Color(0, 191, 255)); // Sky Blue
        showHistoryBtn.setForeground(Color.WHITE);
        showHistoryBtn.addActionListener(this);
        add(showHistoryBtn);

        // Back Button
        backBtn = new JButton("Exit");
        backBtn.setBounds(240, 420, 100, 40);
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(this);
        add(backBtn);

        // Payment History Area
        historyArea = new JTextArea();
        historyArea.setBounds(50, 280, 490, 120);
        historyArea.setBackground(Color.WHITE);
        historyArea.setForeground(Color.BLACK);
        historyArea.setEditable(false);
        historyArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(historyArea);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == paytmBtn) {
            selectedMethod = "Paytm";
            JOptionPane.showMessageDialog(this, "Paytm selected!", "Payment Method", JOptionPane.INFORMATION_MESSAGE);
        } else if (ae.getSource() == googlePayBtn) {
            selectedMethod = "Google Pay";
            JOptionPane.showMessageDialog(this, "Google Pay selected!", "Payment Method", JOptionPane.INFORMATION_MESSAGE);
        } else if (ae.getSource() == amazonPayBtn) {
            selectedMethod = "Amazon Pay";
            JOptionPane.showMessageDialog(this, "Amazon Pay selected!", "Payment Method", JOptionPane.INFORMATION_MESSAGE);
        } else if (ae.getSource() == confirmBtn) {
            String amountText = amountField.getText();
            if (selectedMethod == null) {
                JOptionPane.showMessageDialog(this, "Please select a payment method!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (amountText.isEmpty() || !amountText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String paymentDetails = "Method: " + selectedMethod + ", Amount: $" + amountText;
                paymentHistory.append(paymentDetails).append("\n");
                JOptionPane.showMessageDialog(this, "Payment successful!\n" + paymentDetails, "Payment", JOptionPane.INFORMATION_MESSAGE);
                amountField.setText("");
                selectedMethod = null;
            }
        } else if (ae.getSource() == showHistoryBtn) {
            historyArea.setText(paymentHistory.toString());
        } else if (ae.getSource() == backBtn) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Payment().setVisible(true);
    }
}