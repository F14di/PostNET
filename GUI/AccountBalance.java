package GUI;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Controller.Controller;

public class AccountBalance extends JDialog {
	private static final long serialVersionUID = 1L;
	Controller control = Controller.getInstance();

	public AccountBalance(JFrame parent, String username) {
		super(parent, "Account Balance", true);
		getContentPane().setEnabled(false);
		setSize(350, 200);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
		JTextField textFieldBalance;

		JLabel lblYourAccountBalance = new JLabel("Current debt is (urban ads x100 + rural ads x50) USD:");
		lblYourAccountBalance.setHorizontalAlignment(SwingConstants.CENTER);
		lblYourAccountBalance.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblYourAccountBalance.setBounds(10, 39, 324, 53);
		getContentPane().add(lblYourAccountBalance);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(245, 137, 89, 23);
		getContentPane().add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		textFieldBalance = new JTextField();
		textFieldBalance.setEditable(false);
		textFieldBalance.setText(String.valueOf(control.returnUser(username).getAccountBalance())); // gets the account
																									// balance from
																									// Database
																									// functions
		textFieldBalance.setBounds(139, 91, 86, 20);
		getContentPane().add(textFieldBalance);
		textFieldBalance.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(18, 39, 275, 23);
		getContentPane().add(separator);

		JLabel label = new JLabel("PostNET");
		label.setForeground(SystemColor.textHighlight);
		label.setFont(new Font("Times New Roman", Font.BOLD, 20));
		label.setBounds(18, 18, 275, 23);
		getContentPane().add(label);

	}
}