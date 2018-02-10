package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import Controller.Controller;
import Databases.Ad;
import tableModel.RemoveAdsTableModel;

public class RemoveAdDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private static String username; // logged in username
	private JTable table;
	static Controller control = Controller.getInstance();
	static RemoveAdsTableModel tableModel = new RemoveAdsTableModel(control.distinctPublications(username));

	public RemoveAdDialog(JFrame parent, String username) {
		super(parent, "Remove ad", true);
		this.username = username;

		setResizable(false);
		tableDataChanged(username);

		setBounds(100, 100, 650, 520);
		getContentPane().setLayout(null);

		JButton btnFinish = new JButton("Finish");
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnFinish.setBounds(511, 447, 89, 23);
		getContentPane().add(btnFinish);

		JButton btnRemoveSelectedAds = new JButton("Remove selected ads");
		btnRemoveSelectedAds.setHorizontalTextPosition(SwingConstants.CENTER);
		btnRemoveSelectedAds.setBounds(190, 447, 212, 23);
		getContentPane().add(btnRemoveSelectedAds);
		btnRemoveSelectedAds.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Ad> removeThese = new ArrayList<>(); // holds a list of ads to be deleted from the database
				int[] selectedRows = table.getSelectedRows();
				for (int i : selectedRows) {
					control.AdsByThisUser(username).stream()
							.filter(ID -> ID.getPublicationID() == (Long) table.getValueAt(i, 3))
							.forEach(removeThese::add); // a stream to collect the marked ads to be deleted
				}
				control.removeAds(removeThese, username); // calls a function to remove the selected ad
				tableDataChanged(username); // refreshes the table after removing the selected ads
			}
		});

		JLabel label = new JLabel("PostNET");
		label.setForeground(SystemColor.textHighlight);
		label.setFont(new Font("Times New Roman", Font.BOLD, 20));
		label.setBounds(33, 23, 157, 23);
		getContentPane().add(label);

		JSeparator separator = new JSeparator();
		separator.setBounds(33, 44, 647, 23);
		getContentPane().add(separator);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(43, 78, 542, 341);
		getContentPane().add(scrollPane);

		table = new JTable(tableModel);
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

	}

	public static void tableDataChanged(String username) {
		try {
			tableModel.setData(control.distinctPublications(username)); // returns ads of distinct publication ID that
																		// are added by this user
			tableModel.fireTableDataChanged(); // this is the list of ads that will be viewed in the table
		} catch (NullPointerException e) {
		}
	}

}