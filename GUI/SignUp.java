package GUI;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import Controller.Controller;

public class SignUp extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextField txtUsernameemail;
	LoginWindowUI newlogin;

	private JTextField creditCardtxt;

	public SignUp(JFrame parent) {
		super(parent, "Sign up", true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 460, 400);
		getContentPane().setLayout(null);
		creditCardtxt = new JTextField();
		setResizable(false);

		JLabel label = new JLabel("PostNET");
		label.setForeground(SystemColor.textHighlight);
		label.setFont(new Font("Times New Roman", Font.BOLD, 20));
		label.setBounds(10, 24, 304, 23);
		getContentPane().add(label);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 49, 304, 15);
		getContentPane().add(separator);

		JLabel lblEmail = new JLabel("Email\r\n (username)");
		lblEmail.setBounds(22, 103, 118, 23);
		getContentPane().add(lblEmail);

		JLabel lblPostnetNew = new JLabel("PostNET new registration");
		lblPostnetNew.setFont(new Font("Sitka Small", Font.ITALIC, 13));
		lblPostnetNew.setBounds(20, 60, 214, 32);
		getContentPane().add(lblPostnetNew);

		txtUsernameemail = new JTextField();
		txtUsernameemail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtUsernameemail.setText(null);
			}
		});
		txtUsernameemail.setBounds(187, 103, 211, 20);
		getContentPane().add(txtUsernameemail);
		txtUsernameemail.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(22, 144, 94, 14);
		getContentPane().add(lblPassword);

		JPasswordField txtpnEnterPassword = new JPasswordField();
		txtpnEnterPassword.setBounds(187, 144, 211, 20);
		getContentPane().add(txtpnEnterPassword);

		JLabel lblPasswordAgain = new JLabel("Re-enter password");
		lblPasswordAgain.setBounds(22, 183, 130, 14);
		getContentPane().add(lblPasswordAgain);

		JPasswordField txtpnEnterPassword_1 = new JPasswordField();
		txtpnEnterPassword_1.setBounds(187, 183, 211, 20);
		getContentPane().add(txtpnEnterPassword_1);

		JButton btnFinish = new JButton("Finish");
		btnFinish.setEnabled(false); // button finish will be enabled after the user fills all of the blanks
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String password = String.valueOf(txtpnEnterPassword.getPassword());
				String password_1 = String.valueOf(txtpnEnterPassword_1.getPassword());

				if (passwordCheck(password, password_1)) { // checks if both passwords match.
					txtpnEnterPassword.setText(null);
					txtpnEnterPassword_1.setText(null);
					txtUsernameemail.setText(null);
					dispose();
				}

				else {
					txtpnEnterPassword.setText(null);
					txtUsernameemail.setText(null);
					txtpnEnterPassword_1.setText(null);

					JOptionPane.showMessageDialog(null, "Invalid details", "Sign up error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnFinish.setBounds(309, 274, 89, 23);
		getContentPane().add(btnFinish);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(206, 274, 89, 23);
		getContentPane().add(btnCancel);

		creditCardtxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c = arg0.getKeyChar();
				if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					arg0.consume();
				}
				if (creditCardtxt.getText().length() == 16)
					btnFinish.setEnabled(true);
				else
					btnFinish.setEnabled(false);
			}
		});
		creditCardtxt.setBounds(187, 227, 130, 20);
		getContentPane().add(creditCardtxt);
		creditCardtxt.setColumns(10);

		JLabel lblCardNumber = new JLabel("Credit card number:");
		lblCardNumber.setBounds(22, 227, 118, 14);
		getContentPane().add(lblCardNumber);
	}

	public boolean passwordCheck(String password, String password1) {
		if (password.compareTo(password1) == 0) { // checks if both of the passwords entered match.
			if (checkDetails(txtUsernameemail.getText(), password))
				;
			return true;
		}
		return false;
	}

	public boolean checkDetails(String username, String password) {
		Controller control = Controller.getInstance();

		if (control.getDatabaseObject().getUserList().stream().anyMatch(e -> e.getEmail().equals(username))) { // checks
																												// if
																												// username
																												// exists
																												// in
																												// the
																												// database
			JOptionPane.showConfirmDialog(null, "Username already exists in PostNET", "Username",
					JOptionPane.CLOSED_OPTION);

		} else if (control.newUser(txtUsernameemail.getText(), password, creditCardtxt.getText(), 0)) {// returns true
																										// if the
																										// username was
																										// added to the
																										// database
			return true;
		}
		return false;
	}
}