import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class ChatBot extends JFrame {
    private JEditorPane chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JLabel typingLabel;
    private HashMap<String, String> faq;
    private StringBuilder chatHistory = new StringBuilder();

    public ChatBot() {
        setTitle("AI Chatbot");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color to Midnight Blue (#19150)
        Color darkBlue = new Color(25, 25, 112);

        // Chat area setup
        chatArea = new JEditorPane();
        chatArea.setContentType("text/html");
        chatArea.setEditable(false);
        chatArea.setBackground(darkBlue);
        chatArea.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.getViewport().setBackground(darkBlue);

        // Typing indicator
        typingLabel = new JLabel(" ");
        typingLabel.setForeground(Color.LIGHT_GRAY);
        typingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        typingLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Center panel with chat and typing label
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(typingLabel, BorderLayout.SOUTH);
        centerPanel.setBackground(darkBlue);
        add(centerPanel, BorderLayout.CENTER);

        // Input panel
        inputField = new JTextField();
        inputField.setBackground(Color.DARK_GRAY);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);

        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(30, 144, 255)); 
        sendButton.setForeground(Color.WHITE);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.setBackground(darkBlue);
        add(inputPanel, BorderLayout.SOUTH);

        // Load data
        loadFAQ();

        // Events
        sendButton.addActionListener(e -> respond());
        inputField.addActionListener(e -> respond());

        setVisible(true);
    }

    private void respond() {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) return;

        String preprocessed = preprocess(userInput);
        String response = generateResponse(preprocessed);

        chatHistory.append(formatUserMessage(userInput));
        chatArea.setText(chatHistory.toString());
        inputField.setText("");

        typingLabel.setText("Bot is typing...");

        Timer timer = new Timer(25, null);
        final String botMessage = formatBotMessage(response);
        final StringBuilder animated = new StringBuilder();
        final int[] index = {0};

        timer.addActionListener(e -> {
            if (index[0] < botMessage.length()) {
                animated.append(botMessage.charAt(index[0]));
                chatArea.setText(chatHistory.toString() + animated.toString());
                index[0]++;
            } else {
                chatHistory.append(botMessage);
                chatArea.setText(chatHistory.toString());
                typingLabel.setText("");
                timer.stop();
            }
        });
        timer.start();
    }

    private String preprocess(String input) {
        input = input.toLowerCase().trim();
        input = input.replaceAll("[^a-z0-9 ]", "");
        return input;
    }

    private String generateResponse(String input) {
        return faq.getOrDefault(input, "I'm not sure how to respond to that. Try asking something else!");
    }

    private String formatUserMessage(String message) {
        return "<div style='text-align: right; color: #00BFFF; margin: 5px;'><b>You:</b> " + message + "</div>";
    }

    private String formatBotMessage(String message) {
        return "<div style='text-align: left; color: #ADFF2F; margin: 5px;'><b>Bot:</b> " + message + "</div>";
    }

    private void loadFAQ() {
        faq = new HashMap<>();
        faq.put("hello", "Hello! How can I assist you today?");
        faq.put("hi", "Hi there! Ask me anything.");
        faq.put("how are you", "I'm just code, but thanks for asking!");
        faq.put("what is your name", "I'm a Java chatbot here to help.");
        faq.put("who created you", "I was created by a developer using Java and Swing.");
        faq.put("bye", "Goodbye! Have a great day.");
        faq.put("thank you", "You're welcome!");
        faq.put("thanks", "You're welcome!");
        faq.put("what is java", "Java is a programming language used for building cross-platform applications.");
        faq.put("what is ai", "AI stands for Artificial Intelligence — teaching machines to think like humans.");
        faq.put("what is machine learning", "Machine Learning is a subset of AI that allows systems to learn from data.");
        faq.put("what is oop", "OOP stands for Object-Oriented Programming — a paradigm based on the concept of objects.");
        faq.put("what is a class", "A class is a blueprint for creating objects in object-oriented programming.");
        faq.put("what is an object", "An object is an instance of a class, containing data and behavior.");
        faq.put("what is an operating system", "An operating system manages hardware and software resources of a computer.");
        faq.put("what is the internet", "The internet is a global network that connects millions of devices.");
       
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatBot::new);
    }
}
