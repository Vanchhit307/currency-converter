package collegeassignment;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class currencyConvertor extends JFrame {

    // Currency exchange rates (base: USD)
    private static final Map<String, Double> exchangeRates = new HashMap<>();

    static {
        // Fixed exchange rates relative to USD
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.92);
        exchangeRates.put("GBP", 0.79);
        exchangeRates.put("JPY", 148.50);
        exchangeRates.put("INR", 83.50);
        exchangeRates.put("CAD", 1.36);
        exchangeRates.put("AUD", 1.53);
        exchangeRates.put("CHF", 0.90);
        exchangeRates.put("CNY", 7.23);
        exchangeRates.put("AED", 3.67);
        exchangeRates.put("SGD", 1.34);
        exchangeRates.put("NZD", 1.64);
    }

    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JTextField amountField;
    private JLabel resultLabel;
    private JButton convertButton;
    private JButton swapButton;
    private JButton clearButton;
    private JLabel rateLabel;

    // Constructor
    public currencyConvertor() {
        setTitle("Currency Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(550, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Header
        JLabel headerLabel = new JLabel("Currency Converter", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerLabel.setForeground(new Color(0, 102, 204));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(headerLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // From Currency Panel
        JPanel fromPanel = createCurrencyPanel("From Currency:");
        mainPanel.add(fromPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // To Currency Panel
        JPanel toPanel = createCurrencyPanel("To Currency:");
        mainPanel.add(toPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Swap and Rate Panel
        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        infoPanel.setOpaque(false);

        swapButton = new JButton("⇄ Swap Currencies");
        swapButton.setFont(new Font("Arial", Font.BOLD, 12));
        swapButton.setBackground(new Color(102, 204, 102));
        swapButton.setForeground(Color.WHITE);
        swapButton.setFocusPainted(false);
        swapButton.addActionListener(new SwapButtonListener());
        infoPanel.add(swapButton, BorderLayout.WEST);

        rateLabel = new JLabel("", SwingConstants.RIGHT);
        rateLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        rateLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(rateLabel, BorderLayout.EAST);

        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Amount Panel
        JPanel amountPanel = new JPanel(new BorderLayout(10, 10));
        amountPanel.setBorder(new TitledBorder("Enter Amount"));
        amountPanel.setBackground(Color.WHITE);

        JLabel amountLabel = new JLabel("Amount: ");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField = new JTextField(15);
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add input validation
        amountField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != '\b') {
                    e.consume();
                }
                if (c == '.' && amountField.getText().contains(".")) {
                    e.consume();
                }
            }
        });

        amountPanel.add(amountLabel, BorderLayout.WEST);
        amountPanel.add(amountField, BorderLayout.CENTER);
        mainPanel.add(amountPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setOpaque(false);

        convertButton = new JButton("Convert");
        convertButton.setFont(new Font("Arial", Font.BOLD, 14));
        convertButton.setBackground(new Color(0, 102, 204));
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        convertButton.addActionListener(new ConvertButtonListener());

        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.setBackground(new Color(204, 204, 204));
        clearButton.setForeground(Color.BLACK);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(new ClearButtonListener());

        buttonsPanel.add(convertButton);
        buttonsPanel.add(clearButton);
        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Result Panel
        JPanel resultPanel = new JPanel();
        resultPanel.setBorder(new TitledBorder("Converted Amount"));
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultLabel.setForeground(new Color(0, 102, 204));
        resultPanel.add(resultLabel, BorderLayout.CENTER);
        mainPanel.add(resultPanel);

        add(mainPanel);

        // Set default values
        fromCurrency.setSelectedItem("USD");
        toCurrency.setSelectedItem("EUR");
        updateRateLabel();

        // Set Enter key to convert
        getRootPane().setDefaultButton(convertButton);
    }

    // Method to create currency panel with dropdown
    private JPanel createCurrencyPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new TitledBorder(title));
        panel.setBackground(Color.WHITE);

        String[] currencies = exchangeRates.keySet().toArray(new String[0]);
        JComboBox<String> comboBox = new JComboBox<>(currencies);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.addActionListener(e -> updateRateLabel());

        panel.add(comboBox, BorderLayout.CENTER);

        if (title.equals("From Currency:")) {
            this.fromCurrency = comboBox;
        } else {
            this.toCurrency = comboBox;
        }

        return panel;
    }

    // Method to update the exchange rate display
    private void updateRateLabel() {
        String from = (String) fromCurrency.getSelectedItem();
        String to = (String) toCurrency.getSelectedItem();
        double rate = exchangeRates.get(to) / exchangeRates.get(from);
        rateLabel.setText(String.format("1 %s = %.4f %s", from, rate, to));
    }

    // Method to convert currency
    private double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        // Convert to USD first, then to target currency
        double amountInUSD = amount / exchangeRates.get(fromCurrency);
        double result = amountInUSD * exchangeRates.get(toCurrency);
        return result;
    }

    // Method to get currency symbol
    private String getCurrencySymbol(String currencyCode) {
        Map<String, String> symbols = new HashMap<>();
        symbols.put("USD", "$");
        symbols.put("EUR", "€");
        symbols.put("GBP", "£");
        symbols.put("JPY", "¥");
        symbols.put("INR", "₹");
        symbols.put("CAD", "C$");
        symbols.put("AUD", "A$");
        symbols.put("CHF", "CHF");
        symbols.put("CNY", "¥");
        symbols.put("AED", "د.إ");
        symbols.put("SGD", "S$");
        symbols.put("NZD", "NZ$");

        return symbols.getOrDefault(currencyCode, currencyCode);
    }

    // Listener for Convert button
    private class ConvertButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String from = (String) fromCurrency.getSelectedItem();
                String to = (String) toCurrency.getSelectedItem();

                if (amountField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(currencyConvertor.this,
                            "Please enter an amount to convert",
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double amount = Double.parseDouble(amountField.getText().trim());

                if (amount < 0) {
                    JOptionPane.showMessageDialog(currencyConvertor.this,
                            "Please enter a positive amount",
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double result = convertCurrency(amount, from, to);

                String formattedResult = String.format("%s %.2f %s = %s %.2f %s",
                        getCurrencySymbol(from), amount, from,
                        getCurrencySymbol(to), result, to);

                resultLabel.setText(formattedResult);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(currencyConvertor.this,
                        "Please enter a valid number",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Listener for Swap button
    private class SwapButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Swap the selected currencies
            Object from = fromCurrency.getSelectedItem();
            Object to = toCurrency.getSelectedItem();

            fromCurrency.setSelectedItem(to);
            toCurrency.setSelectedItem(from);

            updateRateLabel();

            // If there's a result already, update it
            if (!resultLabel.getText().isEmpty() && !amountField.getText().trim().isEmpty()) {
                convertButton.doClick();
            }
        }
    }

    // Listener for Clear button
    private class ClearButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            amountField.setText("");
            resultLabel.setText("");
            fromCurrency.setSelectedIndex(0);
            toCurrency.setSelectedIndex(1);
            amountField.requestFocus();
            updateRateLabel();
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show the GUI
        SwingUtilities.invokeLater(() -> {
            new currencyConvertor().setVisible(true);
        });
    }
}
