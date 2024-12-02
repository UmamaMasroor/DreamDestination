import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TravelManagementSystem {
    private JFrame frame;
    private JPanel sidePanel, mainPanel;
    private CardLayout cardLayout;
    private JTextField nameField, contactField, emailField, passportField, idField, addressField, countryField, cityField, costField;
    private JComboBox<String> packageComboBox, destinationComboBox, transportComboBox, paymentComboBox;
    private JButton addButton, updateButton, deleteButton, viewButton, calculateButton;
    private DefaultListModel<String> dataModel;
    private JList<String> dataList;
    private Map<String, Map<String, String>> userData;

    private final String FILE_NAME = "data.txt";

    public TravelManagementSystem() {
        loadUserData();
        initialize();
    }
    

    private void initialize() {
        frame = new JFrame("Dream Destination");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Side Panel
        sidePanel = new JPanel(new GridLayout(6, 1));
        sidePanel.setPreferredSize(new Dimension(200, 0));
        sidePanel.setBackground(new Color(41, 128, 185));

        String[] menuOptions = {"Home", "My Details", "Packages", "Choose Destination", "Transport", "Payment"};
        for (String option : menuOptions) {
            JButton button = createButton(option);
            button.addActionListener(e -> showForm(option));
            sidePanel.add(button);
        }
        class BackgroundPanel extends JPanel {
            private Image backgroundImage;

            public BackgroundPanel(String imagePath) {
                try {
                    backgroundImage = new ImageIcon(imagePath).getImage();
                    if (backgroundImage == null) {
                        System.out.println("Error: Image not loaded from path: " + imagePath);
                    }
                } catch (Exception e) {
                    System.out.println("Error loading image: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.out.println("Error: Background image is null.");
                }
            }
        }

        // Main Panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        BackgroundPanel homePanel = new BackgroundPanel("nature.jpg");  // Update the path as needed
        homePanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Dream Destinations!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(0, 0, 0)); // Set text color to white for better contrast
        homePanel.add(welcomeLabel, BorderLayout.NORTH);

        mainPanel.add(homePanel, "Home");

        // // Main Panel
        // cardLayout = new CardLayout();
        // mainPanel = new JPanel(cardLayout);

        // BackgroundPanel homePanel = new BackgroundPanel("/image.avif");
        // homePanel.setLayout(new BorderLayout());
        
        // JLabel welcomeLabel = new JLabel("Welcome to Dream Destinations!", JLabel.CENTER);
        // welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        // welcomeLabel.setForeground(new Color(255, 255, 255)); // Set text color to white for better contrast
        // homePanel.add(welcomeLabel, BorderLayout.CENTER);
        
        // mainPanel.add(homePanel, "Home");
        

     // Unified Form
     createUnifiedFormPanel();
     mainPanel.add(new JScrollPane(createDataListPanel()), "DataList");

     frame.add(sidePanel, BorderLayout.WEST);
     frame.add(mainPanel, BorderLayout.CENTER);
     frame.setVisible(true);
 }

    private void loadDataList() {
        dataModel.clear();
        if (userData != null) {
            userData.forEach((id, details) -> dataModel.addElement("ID: " + id + " | Name: " + details.get("Name")));
        } else {
            JOptionPane.showMessageDialog(frame, "No data found!");
        }
    }
    
    private JPanel createDataListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Saved Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(41, 128, 185));
        panel.add(titleLabel, BorderLayout.NORTH);
    
        // Use JTable for displaying all details
        String[] columnNames = {"ID", "Name", "Contact", "Email", "Package", "Destination", "Transport", "Cost"};
        Object[][] data = getAllUserData();
        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
    
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        JButton backButton = createButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "My Details"));
        panel.add(backButton, BorderLayout.SOUTH);
    
        return panel;
    }
    private Object[][] getAllUserData() {
        if (userData == null || userData.isEmpty()) {
            return new Object[][] {{"No Data", "", "", "", "", "", "", ""}};
        }
    
        Object[][] data = new Object[userData.size()][8];
        int index = 0;
        for (Map.Entry<String, Map<String, String>> entry : userData.entrySet()) {
            Map<String, String> details = entry.getValue();
            data[index++] = new Object[] {
                entry.getKey(),
                details.get("Name"),
                details.get("Contact"),
                details.get("Email"),
                details.get("Package"),
                details.get("Destination"),
                details.get("Transport"),
                details.get("Cost")
            };
        }
        return data;
    }
        
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
        });
        return button;
    }
    private void updateEntry() {
        String id = idField.getText();
        if (id.isEmpty() || !userData.containsKey(id)) {
            JOptionPane.showMessageDialog(frame, "ID not found or invalid.");
            return;
        }
        userData.put(id, collectFormData());
        saveUserData();
        loadDataList();
        clearFields();
        JOptionPane.showMessageDialog(frame, "Entry updated successfully!");
    }
    
    private void deleteEntry() {
        String id = idField.getText();
        if (id.isEmpty() || !userData.containsKey(id)) {
            JOptionPane.showMessageDialog(frame, "ID not found or invalid.");
            return;
        }
        userData.remove(id);
        saveUserData();
        loadDataList();
        clearFields();
        JOptionPane.showMessageDialog(frame, "Entry deleted successfully!");
    }
    
    
    private void createUnifiedFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(20, 5));
        formPanel.setBackground(new Color(236, 240, 241));

        nameField = new JTextField();
        contactField = new JTextField();
        emailField = new JTextField();
        passportField = new JTextField();
        idField = new JTextField();
        addressField = new JTextField();
        countryField = new JTextField();
        cityField = new JTextField();
        costField = new JTextField();
        costField.setEditable(false);

        String[] packages = {"Select Package", "Budget", "Standard", "Premium"};
        packageComboBox = new JComboBox<>(packages);

        String[] destinations = {"Select Destination", "Paris", "New York", "Tokyo", "Sydney","Turkey","Lahore","Islmabad"};
        destinationComboBox = new JComboBox<>(destinations);

        String[] transports = {"Select Transport", "Bus", "Train", "Flight"};
        transportComboBox = new JComboBox<>(transports);

        String[] paymentMethods = {"Select Payment Method", "Credit Card", "PayPal", "Bank Transfer"};
        paymentComboBox = new JComboBox<>(paymentMethods);

        calculateButton = createButton("Calculate Cost");
        calculateButton.addActionListener(e -> calculateCost());

        addButton = createButton("Add");
        updateButton = createButton("Update");
        deleteButton = createButton("Delete");
        viewButton = createButton("View All");

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Contact:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Passport No:"));
        formPanel.add(passportField);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Country:"));
        formPanel.add(countryField);
        formPanel.add(new JLabel("City:"));
        formPanel.add(cityField);
        formPanel.add(new JLabel("Package:"));
        formPanel.add(packageComboBox);
        formPanel.add(new JLabel("Destination:"));
        formPanel.add(destinationComboBox);
        formPanel.add(new JLabel("Transport:"));
        formPanel.add(transportComboBox);
        formPanel.add(new JLabel("Cost:"));
        formPanel.add(costField);
        formPanel.add(calculateButton);
        formPanel.add(paymentComboBox);
        formPanel.add(addButton);
        formPanel.add(updateButton);
        formPanel.add(deleteButton);
        formPanel.add(viewButton);

        addButton.addActionListener(e -> addEntry());
        updateButton.addActionListener(e -> updateEntry());
        deleteButton.addActionListener(e -> deleteEntry());
        viewButton.addActionListener(e -> cardLayout.show(mainPanel, "DataList"));

        mainPanel.add(formPanel, "My Details");
    }

    private void calculateCost() {
        String packageType = (String) packageComboBox.getSelectedItem();
        String transport = (String) transportComboBox.getSelectedItem();

        int packageCost = switch (packageType) {
            case "Budget" -> 500;
            case "Standard" -> 1000;
            case "Premium" -> 1500;
            default -> 0;
        };

        int transportCost = switch (transport) {
            case "Bus" -> 50;
            case "Train" -> 100;
            case "Flight" -> 300;
            default -> 0;
        };

        int totalCost = packageCost + transportCost;
        costField.setText("$" + totalCost);
    }

    private void showForm(String option) {
        if ("Home".equals(option)) {
            cardLayout.show(mainPanel, "Home");
        } else {
            cardLayout.show(mainPanel, "My Details");
        }
    }

    private void loadUserData() {
        userData = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            userData = (Map<String, Map<String, String>>) ois.readObject();
        } catch (Exception ignored) {
        }
    }

    private void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(userData);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving data: " + e.getMessage());
        }
    }

    private void addEntry() {
        String id = idField.getText();
        if (id.isEmpty() || userData.containsKey(id)) {
            JOptionPane.showMessageDialog(frame, "ID is required and must be unique.");
            return;
        }
        Map<String, String> details = collectFormData();
        userData.put(id, details);
        saveUserData();
        loadDataList();
        clearFields();
        JOptionPane.showMessageDialog(frame, "Entry added successfully!");
    }

    private Map<String, String> collectFormData() {
        Map<String, String> details = new HashMap<>();
        details.put("Name", nameField.getText());
        details.put("Contact", contactField.getText());
        details.put("Email", emailField.getText());
        details.put("Passport", passportField.getText());
        details.put("ID", idField.getText());
        details.put("Address", addressField.getText());
        details.put("Country", countryField.getText());
        details.put("City", cityField.getText());
        details.put("Package", (String) packageComboBox.getSelectedItem());
        details.put("Destination", (String) destinationComboBox.getSelectedItem());
        details.put("Transport", (String) transportComboBox.getSelectedItem());
        details.put("Cost", costField.getText());
        details.put("Payment Method", (String) paymentComboBox.getSelectedItem());
        return details;
    }

    private void clearFields() {
        nameField.setText("");
        contactField.setText("");
        emailField.setText("");
        passportField.setText("");
        idField.setText("");
        addressField.setText("");
        countryField.setText("");
        cityField.setText("");
        packageComboBox.setSelectedIndex(0);
        destinationComboBox.setSelectedIndex(0);
        transportComboBox.setSelectedIndex(0);
        costField.setText("");
        paymentComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new TravelManagementSystem();
    }
}
