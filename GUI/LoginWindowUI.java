package GUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import Controller.Controller;

public class LoginWindowUI {

	private JFrame frame;
	private String userName; // logged in username

	private JTextField txtUsername;
	private JPasswordField txtPassword;
	SignUp signUpUI;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindowUI window = new LoginWindowUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LoginWindowUI() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("PostNET login");
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setSize(420, 265);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("PostNET");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel.setForeground(SystemColor.textHighlight);
		lblNewLabel.setBounds(23, 11, 346, 23);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsername.setBounds(40, 77, 60, 14);
		frame.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassword.setBounds(40, 105, 60, 14);
		frame.getContentPane().add(lblPassword);

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtUsername.setBounds(108, 74, 219, 20);
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);

		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtPassword.setBounds(108, 102, 219, 20);
		frame.getContentPane().add(txtPassword);

		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Sitka Display", Font.BOLD | Font.ITALIC, 13));
		btnLogin.setFocusCycleRoot(true);
		btnLogin.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		frame.getRootPane().setDefaultButton(btnLogin);

		btnLogin.setBounds(40, 149, 89, 25);
		frame.getContentPane().add(btnLogin);

		JButton btnReset = new JButton("Reset"); // sets the textFields to null
		btnReset.setFont(new Font("Sitka Display", Font.BOLD | Font.ITALIC, 13));
		btnReset.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtUsername.setText(null);
				txtPassword.setText(null);
			}
		});
		btnReset.setBounds(139, 149, 89, 25);
		frame.getContentPane().add(btnReset);

		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Sitka Display", Font.BOLD | Font.ITALIC, 13));
		btnExit.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame = new JFrame("Exit");
				if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
					System.exit(0);
				}

			}
		});
		btnExit.setBounds(300, 186, 89, 25);
		frame.getContentPane().add(btnExit);

		JButton btnSignup = new JButton("Sign up");
		btnSignup.setFont(new Font("Sitka Display", Font.BOLD | Font.ITALIC, 13));
		btnSignup.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnSignup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				signUpUI = new SignUp(frame);
				signUpUI.setVisible(true);
			}
		});
		btnSignup.setBounds(238, 149, 89, 25);
		frame.getContentPane().add(btnSignup);

		JSeparator separator = new JSeparator();
		separator.setBounds(23, 36, 346, 15);
		frame.getContentPane().add(separator);

		btnLogin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String password = String.valueOf(txtPassword.getPassword());
				String username = txtUsername.getText();
				Controller control = Controller.getInstance();
				if (control.checkLogin(username, password)) {// If the details entered are valid, the main window shows
																// up
					control.uploadUserAds(username); // and the user ads are uploaded from the SQL
					new mainWindowUI(txtUsername.getText());
					frame.dispose();

				} else {
					JOptionPane.showMessageDialog(null, "Invalid login details", "Login error",
							JOptionPane.ERROR_MESSAGE);
					txtPassword.setText(null);
					txtUsername.setText(null);
				}
			}
		});
	}
}