package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;

import Controller.Controller;
import Controller.inputVerifier;
import tableModel.RemoveAdsTableModel;

public class ShowAdsDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTable table;
	private static String username; // logged in username
	static Controller control = Controller.getInstance();
	static RemoveAdsTableModel tableModel = new RemoveAdsTableModel(control.distinctPublications(username));
	inputVerifier inputVerifierObject = new inputVerifier(); // whenever the user decides to modify an ad,
																// the new details needs to be verified before updating
																// the details

	public ShowAdsDialog(JFrame parent, String username) {
		super(parent, "Show ads", true);
		ShowAdsDialog.username = username;
		tableChanged(username);
		setResizable(false);

		setBounds(100, 100, 980, 520);
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(42, 80, 897, 341);
		getContentPane().add(scrollPane);
		JButton btnModifyAd = new JButton("Modify ad");

		table = new JTable(tableModel);
		tableModel.fireTableDataChanged();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		scrollPane.setViewportView(table);
		table.setRequestFocusEnabled(false);
		table.setIntercellSpacing(new Dimension(3, 2));
		table.setFocusTraversalKeysEnabled(false);
		table.setFocusCycleRoot(true);
		table.setFillsViewportHeight(true);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setAutoCreateRowSorter(true);
		JLabel lblYourAds = new JLabel("Your Ads:");
		lblYourAds.setBounds(42, 53, 212, 31);
		getContentPane().add(lblYourAds);

		JButton btnFinish = new JButton("Finish");
		btnFinish.setBounds(850, 433, 89, 23);
		getContentPane().add(btnFinish);
		btnFinish.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		btnModifyAd.addActionListener(new ActionListener() { // action listener on the modify ad button
			public void actionPerformed(ActionEvent arg0) {

				if (table.getSelectedRowCount() != 1) {// case the user did not select an ad
					JOptionPane.showConfirmDialog(null, "Please select an ad to modify", "Ad selection",
							JOptionPane.CLOSED_OPTION);
					return;
				} else {
					long publicationIDToModify = (Long) table.getValueAt(table.getSelectedRow(), 3);
					newAdDialogUI.setAdInserted(false); // gets the publication ID of the ad to be modified
					modifyAd modifierObject = new modifyAd(parent, username, publicationIDToModify);// calls a new class
																									// that extends the
																									// new ad dialog

					modifierObject.setVisible(true);
					newAdDialogUI newAdWindow = new newAdDialogUI(parent, username);
					if (newAdWindow.isAdInserted()) {
						control.removeAds(control.AdsofThisPublicationID(publicationIDToModify));
						// if the user hits finish, and the new ad is added to the database, the older
						// ad needs to be deleted
					}
					tableChanged(username); // refreshe the modify ad table
					newAdDialogUI.setAdInserted(false); // ad inserted flag
				}
			}
		});
		btnModifyAd.setBounds(751, 433, 89, 23);
		getContentPane().add(btnModifyAd);

		JSeparator separator = new JSeparator();
		separator.setBounds(25, 46, 473, 23);
		getContentPane().add(separator);

		JLabel label = new JLabel("PostNET");
		label.setForeground(SystemColor.textHighlight);
		label.setFont(new Font("Times New Roman", Font.BOLD, 20));
		label.setBounds(25, 25, 157, 23);
		getContentPane().add(label);
	}

	public void tableChanged(String username) { // refresh the table that displays the ads added by this user
		try {
			tableModel.setData(control.distinctPublications(username));
			tableModel.fireTableDataChanged();
		} catch (NullPointerException e) {
		}
	}
}