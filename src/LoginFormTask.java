import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class User {

    private String username;
    private String password;
    private String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

class WelcomePage extends Frame {

    WelcomePage(User user) {
        super("Welcome");

        setLayout(new BorderLayout(10, 10));

        Label welcome = new Label("Hello, " + user.getUsername(), Label.CENTER);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 22));

        Label role = new Label("Role: " + user.getRole(), Label.CENTER);

        Panel center = new Panel(new GridLayout(2, 1, 5, 5));
        center.add(welcome);
        center.add(role);

        Button close = new Button("Close");

        add(center, BorderLayout.CENTER);
        add(close, BorderLayout.SOUTH);

        close.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(350, 200);
        setLocationRelativeTo(null);
    }
}

public class LoginFormTask extends Frame {

    private List<User> users = new ArrayList<>();
    private int failedAttempts = 0;
    private static final int MAX_FAILED_ATTEMPTS = 3;

    public LoginFormTask() {
        super("Login");
        loadUsers();

        setLayout(new GridBagLayout());
        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));
        form.add(new Label("Username:", Label.RIGHT));
        TextField username = new TextField(18);
        form.add(username);

        form.add(new Label("Password:", Label.RIGHT));
        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);

        form.add(new Label("Role:", Label.RIGHT));
        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);

        content.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        Button cancel = new Button("Cancel");
        Button login = new Button("Login");
        buttons.add(cancel);
        buttons.add(login);
        content.add(buttons, BorderLayout.SOUTH);

        add(content);

        login.addActionListener(e -> {
            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText();
            String selectedRole = role.getSelectedItem();

            if (enteredUsername.isEmpty()) {
                showMessage("Please enter your username.");
                return;
            }

            if (enteredPassword.isEmpty()) {
                showMessage("Please enter your password.");
                return;
            }

            User user = findUser(enteredUsername);

            if (user == null) {
                showMessage("Username not found.");
                return;
            }

            if (!user.getPassword().equals(enteredPassword)) {
                failedAttempts++;

                if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                    login.setEnabled(false);
                    showMessage("Login disabled after 3 incorrect password attempts.");
                } else {
                    showMessage("Incorrect password. Attempt " + failedAttempts
                            + " of " + MAX_FAILED_ATTEMPTS + ".");
                }

                return;
            }

            if (!user.getRole().equals(selectedRole)) {
                showMessage("Incorrect role selected.");
                return;
            }

            WelcomePage welcomePage = new WelcomePage(user);
            welcomePage.setVisible(true);
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    private void loadUsers() {
        users.add(new User("student", "1234", "Student"));
        users.add(new User("ta", "1234", "TA"));
        users.add(new User("professor", "1234", "Professor"));
    }

    private User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        return null;
    }

    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setLayout(new BorderLayout(10, 10));

        Label label = new Label(message, Label.CENTER);
        Button ok = new Button("OK");

        ok.addActionListener(e -> dialog.dispose());

        dialog.add(label, BorderLayout.CENTER);
        dialog.add(ok, BorderLayout.SOUTH);

        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFormTask().setVisible(true);
    }
}
