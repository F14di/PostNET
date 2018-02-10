package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.sun.glass.events.KeyEvent;

import Controller.Controller;
import Databases.Databases;
import tableModel.AdsTableModel;

public class mainWindowUI {

	private JDialog btnNewAdDialog;
	private JDialog btnRemoveAd;
	private JDialog btnShowAds;

	private JFrame frame;
	AccountBalance accountBalance;
	Controller controller = Controller.getInstance();

	private String username; // logged in username
	Databases databasesObject = Databases.getInstance();

	AdsTableModel adsTableModel = new AdsTableModel(databasesObject.getAdList());

	public String getUsername() {
		return username;
	}

	public mainWindowUI(String username) {
		this.username = username;
		btnRemoveAd = new RemoveAdDialog(frame, username);
		btnNewAdDialog = new newAdDialogUI(frame, username);
		btnShowAds = new ShowAdsDialog(frame, username);

		initialize();
	}

	private void initialize() {

		frame = new JFrame("PostNET");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setBounds(200, 200, 630, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);

		TextArea textArea = new TextArea();
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(false);
		textArea.setText(
				"Using PostNet\r\n\r\n\r\nPublish a new ad \r\n\r\nSelect this option to insert a new ad into the system. In the main window, \r\ndo the following:\r\n1.\tIn the Location pane, select the sign location.\r\n2.\tIn the Ad Format pane, select the ad type.\r\n3.\tIn the Frequency pane, select the display frequency:\r\n\r\n\u2022Once \u2013 Displays an ad once on selected signs.\r\n       4a. Select the sign from the right pane.\r\n\r\n\u2022Several \u2013 Displays an ad multiple times on multiple signs.\r\n        4b. Select a sign from the right pane.\r\n        4c. To add new ad select Add more ads and continue with 4b.\r\n\r\n\u2022Continuous \u2013 displays an ad for a selected period on selected signs. \r\n  Between the selected dates, the ad is displayed sequentially  according \r\n  to the selected times. The interval between each display sequence is \r\n\tdetermined according to the Every field. \r\n         4c1. Select the signs from the right pane.\r\n         4c2. In Date and time select the beginning of the display period. \r\n         The time in this selection also indicates the start time of \r\n\t  each display sequence\r\n         4c3. In Until select the end of the display period. The time in this \r\n\t  selection also indicates the end time of each display sequence\r\n         4c4. In Days select how often (in days) the ad is displayed.\r\n\r\nExample:\r\n     To play and ad for 6 months between 1 January and 1 June from \r\n     10:00 to 20:00 every three days, set the following:\r\n       i. In the Date and time field select 1 Jan 2018 10:00.\r\n       ii. In the Until field select 1 Jun 2018 20:00.\r\n       iii. In the Every field select 3 days.\r\n\r\n5. Click Browse to upload a file (files mus be either TXT, JPG or MP4).\r\n6. Click Finish.\r\nNote: Official bodies can select Immediate. This setting overrides the \r\nfrequency settings and displays the ad immediately.\r\n\r\n\r\nRemove ad\r\n\r\nSelect this option to remove an ad from the PostNet database.\r\n1. In the Remove ad window, select the ads to be removed.\r\n2. Click Remove selected ads.\r\nNote: All publication instances of an ad are removed. This includes \r\nremoving an ad from all the signs it plays on.\r\n\r\n\r\nAccount Balance\r\n\r\nSelect this option to view your account balance.\r\nThe system displays the current debt owed to PostNet. Each played \r\nurban ad is worth 100 USD and each played rural ad is worth 50 USD.\r\n\r\n\r\nShow Ads\r\n\r\nSelect this option to display and modify ads that are saved in the PostNet \r\ndatabase.\r\nTo modify ads:\r\n1. In the Show ads window, select the ad to modify.\r\n2. Click Modify Ad. The Publish a new ad window opens.\r\n3. In the Publish a new ad modify the ad according to the instructions in \r\n    publish a new ad.\r\n4. After clicking Finish the Show ads window is displayed again. To modify \r\n    another ad go to step 1.\r\n5. Click Finish. \r\n");
		textArea.setBounds(194, 85, 410, 400);
		frame.getContentPane().add(textArea);

		JButton btnRemoveAd = new JButton("Remove ad");
		btnRemoveAd.setHorizontalAlignment(SwingConstants.LEFT);
		btnRemoveAd.setBounds(15, 120, 173, 23);
		frame.getContentPane().add(btnRemoveAd);
		btnRemoveAd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RemoveAdDialog.tableDataChanged(username); // updates the ads table in the remove ad window
				RemoveAdDialog removeAdObject = new RemoveAdDialog(frame, username);
				mainWindowUI.this.btnRemoveAd.setVisible(true);
			}
		});

		JButton btnShowAds = new JButton("Show ads");
		btnShowAds.setHorizontalAlignment(SwingConstants.LEFT);
		btnShowAds.setBounds(15, 154, 173, 23);
		frame.getContentPane().add(btnShowAds);
		btnShowAds.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowAdsDialog showAdsDialog = new ShowAdsDialog(frame, username);
				mainWindowUI.this.btnShowAds.setVisible(true);
			}
		});

		JButton btnPublishANew = new JButton("Publish a new ad");
		btnPublishANew.setHorizontalAlignment(SwingConstants.LEFT);
		btnPublishANew.setBounds(15, 85, 173, 23);
		frame.getContentPane().add(btnPublishANew);
		btnPublishANew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindowUI.this.btnNewAdDialog.setVisible(true);
			}
		});

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 784, 21);
		frame.getContentPane().add(menuBar);

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem publishAdMenu = new JMenuItem("Publish Ad");
		fileMenu.add(publishAdMenu);
		publishAdMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindowUI.this.btnNewAdDialog.setVisible(true);
			}
		});
		publishAdMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

		JMenuItem showBalanceMenu = new JMenuItem("Show Balance");
		fileMenu.add(showBalanceMenu);
		showBalanceMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				accountBalance = new AccountBalance(frame, username);
				accountBalance.setVisible(true);
			}
		});
		showBalanceMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));

		JSeparator separator = new JSeparator();
		fileMenu.add(separator);

		JMenuItem signOutMenu = new JMenuItem("Sign Out");
		fileMenu.add(signOutMenu);
		signOutMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LoginWindowUI logInWindow = new LoginWindowUI();
				frame.dispose();
			}
		});

		JMenuItem exitMenu = new JMenuItem("Exit");
		fileMenu.add(exitMenu);
		exitMenu.setMnemonic(KeyEvent.VK_X);
		exitMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		fileMenu.setMnemonic(KeyEvent.VK_F);
		publishAdMenu.setMnemonic(KeyEvent.VK_N);
		btnPublishANew.setMnemonic(KeyEvent.VK_N);

		JLabel label = new JLabel("PostNET");
		label.setForeground(SystemColor.textHighlight);
		label.setFont(new Font("Times New Roman", Font.BOLD, 20));
		label.setBounds(25, 32, 157, 23);
		frame.getContentPane().add(label);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(25, 53, 473, 23);
		frame.getContentPane().add(separator_1);

		JButton btnAccountBalance = new JButton("Account balance");
		btnAccountBalance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accountBalance = new AccountBalance(frame, username);
				accountBalance.setVisible(true);

			}
		});
		btnAccountBalance.setHorizontalAlignment(SwingConstants.LEFT);
		btnAccountBalance.setBounds(15, 188, 173, 23);
		frame.getContentPane().add(btnAccountBalance);

		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame = new JFrame("Exit");
				if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
					System.exit(0);
				}
			}
		});
		btnExit.setIcon(null);
		btnExit.setBounds(502, 510, 85, 32);
		frame.getContentPane().add(btnExit);

		JButton btnSignout = new JButton("SignOut");
		btnSignout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LoginWindowUI logInWindow = new LoginWindowUI();
				frame.dispose();
			}
		});
		btnSignout.setBounds(409, 510, 85, 32);
		btnSignout.setFont(new Font("Tahoma", Font.PLAIN, 13));
		frame.getContentPane().add(btnSignout);

		JLabel lblLogged = new JLabel("Logged in as: " + username);
		lblLogged.setBounds(409, 491, 181, 14);
		frame.getContentPane().add(lblLogged);
	}
}