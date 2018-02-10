package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DateFormatter;

import com.sun.glass.events.KeyEvent;

import Controller.Controller;
import Controller.inputVerifier;
import Databases.Ad;
import Databases.DatabaseFunctions;
import Databases.Sign;
import tableModel.SignTableModel;

public class newAdDialogUI extends JDialog {

	private static final long serialVersionUID = 1L;

	private final ButtonGroup AdFormatbtnGrp = new ButtonGroup();
	private final ButtonGroup FrequencybtnGrp = new ButtonGroup();
	JButton btnSeveralAds;
	JPanel publish_Until_Panel;
	JPanel DateAndTime_Panel;
	JPanel frequency_Panel;
	private JTable table;
	JFileChooser fileChooserObject = new JFileChooser();
	private static String frequencySelection = "Once"; // frequency selected type flag
	private static String TypeSelection = "Text"; // ad selected type flag
	private String username;
	private static boolean AdInserted; // ad accepted in the program - flag
	private static long publicationID;
	inputVerifier inputVerifierObject = new inputVerifier();
	JTextPane browseTextPane;
	JRadioButton rdbtnText;
	JRadioButton rdbtnGraphic;
	JCheckBox chckbxHaifa;
	JCheckBox chckbxAkko;
	JCheckBox chckbxTiberias;
	JCheckBox chckbxGalile;
	JCheckBox chckbxNazareth;
	JCheckBox chckbxDeadsea;
	JCheckBox chckbxTelaviv;
	JCheckBox chckbxEilat;

	Calendar calendar = Calendar.getInstance();
	Calendar startTimeCalendar = Calendar.getInstance();
	Calendar endTimeCalendar = Calendar.getInstance();

	SimpleDateFormat hourFormatter = new SimpleDateFormat("HH");
	SimpleDateFormat minuteFormatter = new SimpleDateFormat("mm");
	SimpleDateFormat secondFormatter = new SimpleDateFormat("ss");

	SignTableModel tableModel;
	Controller control = Controller.getInstance();

	static List<Sign> locationFilteredSignList = new ArrayList<>();

	public newAdDialogUI(JFrame parent, String userName) {

		super(parent, "Publish a new ad", true);
		this.username = userName;
		this.publicationID = DatabaseFunctions.getMaxAdID()[1] + 1;
		Initialize();
	}

	public boolean isAdInserted() {
		return AdInserted;
	}

	public static void setAdInserted(boolean adInserted) {
		AdInserted = adInserted;
	}

	public boolean Initialize() {
		List<Sign> EmptyArray = new ArrayList<>(); // used in order to initialize the sign table model
		tableModel = new SignTableModel(EmptyArray);
		setSize(980, 520);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JDialog.setDefaultLookAndFeelDecorated(true);
		JFrame.setDefaultLookAndFeelDecorated(true);

		JCheckBox chckbxImmediate = new JCheckBox("Immediate");
		if (control.returnUser(username).isOfficial()) {
			chckbxImmediate.setEnabled(true);
		} else {
			chckbxImmediate.setEnabled(false);
		}

		JPanel ad_Format_Panel = new JPanel();
		ad_Format_Panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Ad Format",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		ad_Format_Panel.setBounds(14, 125, 271, 46);
		getContentPane().add(ad_Format_Panel);
		ad_Format_Panel.setLayout(null);

		rdbtnText = new JRadioButton("Text");
		rdbtnText.setInheritsPopupMenu(true);
		rdbtnText.setBounds(6, 16, 55, 23);
		ad_Format_Panel.add(rdbtnText);
		AdFormatbtnGrp.add(rdbtnText);
		rdbtnText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rdbtnText.isSelected())
					TypeSelection = "txt";
				SignTypeFilter(true, "Text");

			}
		});

		rdbtnGraphic = new JRadioButton("Graphic");
		rdbtnGraphic.setInheritsPopupMenu(true);
		rdbtnGraphic.setBounds(156, 16, 72, 23);
		ad_Format_Panel.add(rdbtnGraphic);
		AdFormatbtnGrp.add(rdbtnGraphic);
		rdbtnGraphic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (rdbtnGraphic.isSelected()) {
					TypeSelection = "JPGmp4";
					SignTypeFilter(true, "Graphic");
				}
			}
		});

		frequency_Panel = new JPanel();
		frequency_Panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Frequency",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		frequency_Panel.setBounds(14, 182, 271, 46);
		getContentPane().add(frequency_Panel);
		frequency_Panel.setLayout(null);

		DateAndTime_Panel = new JPanel();
		DateAndTime_Panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Date and time",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		DateAndTime_Panel.setBounds(14, 242, 335, 46);
		getContentPane().add(DateAndTime_Panel);
		DateAndTime_Panel.setLayout(null);

		JComboBox from_Month = new JComboBox(new DefaultComboBoxModel(Month.values()));
		from_Month.setBounds(52, 16, 93, 23);
		DateAndTime_Panel.add(from_Month);
		from_Month.setAlignmentX(Component.RIGHT_ALIGNMENT);
		from_Month.setAlignmentY(Component.TOP_ALIGNMENT);
		from_Month.setAutoscrolls(true);
		from_Month.setEditable(true);

		JComboBox from_Day = new JComboBox();
		from_Day.setBounds(6, 16, 42, 23);
		DateAndTime_Panel.add(from_Day);
		from_Day.setEditable(true);
		from_Day.setModel(new DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09",
				"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
				"27", "28", "29", "30", "31" }));

		JSpinner fromYear = new JSpinner();
		fromYear.setBounds(149, 16, 60, 23);
		DateAndTime_Panel.add(fromYear);
		fromYear.setModel(new SpinnerListModel(new String[] { "2018", "2019", "2020", "2021", "2022", "2023", "2024",
				"2025", "2026", "2027", "2028", "2029", "2030" }));

		JSpinner fromTime = new JSpinner(new SpinnerDateModel(new Date(1514315476345L), null, null, Calendar.SECOND));
		fromTime.setBounds(215, 16, 110, 23);
		DateAndTime_Panel.add(fromTime);
		JSpinner.DateEditor de = new JSpinner.DateEditor(fromTime, "HH:mm:ss");
		DateFormatter formatter = (DateFormatter) de.getTextField().getFormatter();
		fromTime.setEditor(de);

		JPanel everyPanel = new JPanel();
		everyPanel.setBounds(185, 365, 160, 22);
		everyPanel.setVisible(false);
		getContentPane().add(everyPanel);
		everyPanel.setLayout(null);

		JLabel lblEvery = new JLabel("Every:");
		lblEvery.setBounds(0, 0, 42, 22);
		everyPanel.add(lblEvery);
		lblEvery.setVisible(false);
		lblEvery.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDay = new JLabel("Days");
		lblDay.setBounds(104, 5, 46, 14);
		everyPanel.add(lblDay);

		JSpinner cmboBoxEvery = new JSpinner();
		cmboBoxEvery.setFocusCycleRoot(true);
		cmboBoxEvery.setFocusTraversalPolicyProvider(true);
		cmboBoxEvery.setIgnoreRepaint(true);
		cmboBoxEvery.setInheritsPopupMenu(true);
		cmboBoxEvery.setModel(new SpinnerNumberModel(1, 1, 365, 1));
		cmboBoxEvery.setBounds(45, 2, 49, 20);
		everyPanel.add(cmboBoxEvery);

		JRadioButton rdbtnContinuous = new JRadioButton("Continuous");
		rdbtnContinuous.setBounds(156, 16, 109, 23);
		frequency_Panel.add(rdbtnContinuous);
		FrequencybtnGrp.add(rdbtnContinuous);
		rdbtnContinuous.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frequencySelection = "Continuous";
				cmboBoxEvery.setVisible(true);
				everyPanel.setVisible(true);
				lblEvery.setVisible(true);
				lblDay.setVisible(true);
				publish_Until_Panel.setVisible(true);
				btnSeveralAds.setVisible(false);

			}
		});

		JRadioButton rdbtnOnce = new JRadioButton("Once");
		rdbtnOnce.setBounds(6, 16, 55, 23);
		frequency_Panel.add(rdbtnOnce);
		FrequencybtnGrp.add(rdbtnOnce);
		rdbtnOnce.setSelected(true);
		rdbtnOnce.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frequencySelection = "Once";
				publish_Until_Panel.setVisible(false);
				cmboBoxEvery.setVisible(false);
				everyPanel.setVisible(false);
				lblEvery.setVisible(false);
				lblDay.setVisible(false);
				btnSeveralAds.setVisible(false);
				chckbxImmediate.setVisible(true);
			}
		});

		JRadioButton rdbtnSeveral = new JRadioButton("Several");
		rdbtnSeveral.setBounds(79, 16, 72, 23);
		frequency_Panel.add(rdbtnSeveral);
		FrequencybtnGrp.add(rdbtnSeveral);
		rdbtnSeveral.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frequencySelection = "Several";
				cmboBoxEvery.setVisible(false);
				lblEvery.setVisible(false);
				everyPanel.setVisible(false);
				lblDay.setVisible(false);
				publish_Until_Panel.setVisible(false);
				btnSeveralAds.setVisible(true);
				chckbxImmediate.setVisible(false);
			}
		});

		JPanel locationPanel = new JPanel();
		locationPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Location",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		locationPanel.setBounds(14, 45, 359, 70);
		getContentPane().add(locationPanel);
		locationPanel.setLayout(null);

		chckbxHaifa = new JCheckBox("Haifa");
		chckbxHaifa.setBounds(6, 16, 55, 23);
		locationPanel.add(chckbxHaifa);
		chckbxHaifa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxHaifa.isSelected()) {
					LocationSignFilter(true, "Haifa");
				} else
					LocationSignFilter(false, "Haifa");
				AdFormatbtnGrp.clearSelection();

			}
		});

		chckbxAkko = new JCheckBox("Akko");
		chckbxAkko.setBounds(105, 16, 55, 23);
		locationPanel.add(chckbxAkko);
		chckbxAkko.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxAkko.isSelected()) {
					LocationSignFilter(true, "Akko");
				} else
					LocationSignFilter(false, "Akko");
				AdFormatbtnGrp.clearSelection();
			}
		});

		chckbxTiberias = new JCheckBox("Tiberias");
		chckbxTiberias.setBounds(205, 16, 72, 23);
		locationPanel.add(chckbxTiberias);
		chckbxTiberias.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxTiberias.isSelected()) {
					LocationSignFilter(true, "Tiberias");
				} else
					LocationSignFilter(false, "Tiberias");
				AdFormatbtnGrp.clearSelection();
			}
		});

		chckbxGalile = new JCheckBox("Galile");
		chckbxGalile.setBounds(295, 16, 58, 23);
		locationPanel.add(chckbxGalile);
		chckbxGalile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxGalile.isSelected()) {
					LocationSignFilter(true, "Galile");
				} else
					LocationSignFilter(false, "Galile");
				AdFormatbtnGrp.clearSelection();
			}
		});

		chckbxNazareth = new JCheckBox("Nazareth");
		chckbxNazareth.setBounds(6, 40, 72, 23);
		locationPanel.add(chckbxNazareth);
		chckbxNazareth.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxNazareth.isSelected()) {
					LocationSignFilter(true, "Nazareth");
				} else
					LocationSignFilter(false, "Nazareth");
				AdFormatbtnGrp.clearSelection();
			}
		});

		chckbxDeadsea = new JCheckBox("DeadSea");
		chckbxDeadsea.setBounds(105, 40, 72, 23);
		locationPanel.add(chckbxDeadsea);
		chckbxDeadsea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxDeadsea.isSelected()) {
					LocationSignFilter(true, "DeadSea");
				} else
					LocationSignFilter(false, "DeadSea");
				AdFormatbtnGrp.clearSelection();
			}
		});

		chckbxTelaviv = new JCheckBox("Tel Aviv");
		chckbxTelaviv.setBounds(205, 40, 72, 23);
		locationPanel.add(chckbxTelaviv);
		chckbxTelaviv.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxTelaviv.isSelected()) {
					LocationSignFilter(true, "Tel Aviv");
				} else
					LocationSignFilter(false, "Tel Aviv");
				AdFormatbtnGrp.clearSelection();
			}
		});

		chckbxEilat = new JCheckBox("Eilat");
		chckbxEilat.setBounds(295, 40, 58, 23);
		locationPanel.add(chckbxEilat);
		chckbxEilat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxEilat.isSelected()) {
					LocationSignFilter(true, "Eilat");
				} else
					LocationSignFilter(false, "Eilat");
				AdFormatbtnGrp.clearSelection();
			}
		});

		chckbxImmediate.setBounds(263, 285, 93, 23);
		getContentPane().add(chckbxImmediate);
		chckbxImmediate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxImmediate.isSelected()) {

					publish_Until_Panel.setVisible(true);
					rdbtnSeveral.setEnabled(false);
					rdbtnContinuous.setEnabled(false);
					rdbtnOnce.setSelected(true);
					rdbtnOnce.setEnabled(false);
					cmboBoxEvery.setVisible(false);
					everyPanel.setVisible(false);
					lblEvery.setVisible(false);
					lblDay.setVisible(false);
					btnSeveralAds.setVisible(false);
				} else {
					publish_Until_Panel.setVisible(false);
					rdbtnSeveral.setEnabled(true);
					rdbtnContinuous.setEnabled(true);
					rdbtnOnce.setEnabled(true);

				}
			}
		});

		browseTextPane = new JTextPane();
		browseTextPane.setBorder(UIManager.getBorder("ScrollPane.border"));
		browseTextPane.setBounds(14, 413, 260, 23);
		getContentPane().add(browseTextPane);
		browseTextPane.setEditable(false);

		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setSelectedIcon(null);
		btnBrowse.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, Color.GRAY, null));
		btnBrowse.setBounds(276, 413, 89, 23);
		getContentPane().add(btnBrowse);
		btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (e.getSource() == btnBrowse) {
					int approvalOption = fileChooserObject.showOpenDialog(null);
					if (approvalOption == JFileChooser.APPROVE_OPTION) {
						File file = fileChooserObject.getSelectedFile();
						browseTextPane.setText("Opening" + file.getName() + ".\n");
					} else {
						browseTextPane.setText("Open command cancelled");
					}
					browseTextPane.setCaretPosition(browseTextPane.getDocument().getLength());
				}

			}
		});

		Date date = new Date();
		new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);

		publish_Until_Panel = new JPanel();
		publish_Until_Panel.setLayout(null);
		publish_Until_Panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Until",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		publish_Until_Panel.setBounds(14, 308, 335, 46);
		getContentPane().add(publish_Until_Panel);
		publish_Until_Panel.setLayout(null);
		publish_Until_Panel.setVisible(false);

		JComboBox comboBox_Until_Month = new JComboBox(new DefaultComboBoxModel(Month.values()));
		comboBox_Until_Month.setBounds(52, 16, 93, 23);
		publish_Until_Panel.add(comboBox_Until_Month);
		comboBox_Until_Month.setAlignmentX(Component.RIGHT_ALIGNMENT);
		comboBox_Until_Month.setAlignmentY(Component.TOP_ALIGNMENT);
		comboBox_Until_Month.setAutoscrolls(true);
		comboBox_Until_Month.setEditable(true);

		JComboBox comboBox_Until_Day = new JComboBox();
		comboBox_Until_Day.setBounds(6, 16, 42, 23);
		publish_Until_Panel.add(comboBox_Until_Day);
		comboBox_Until_Day.setEditable(true);
		comboBox_Until_Day.setModel(new DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
				"25", "26", "27", "28", "29", "30", "31" }));

		JSpinner untilYear = new JSpinner();
		untilYear.setBounds(149, 16, 60, 23);
		publish_Until_Panel.add(untilYear);
		untilYear.setModel(new SpinnerListModel(new String[] { "2018", "2019", "2020", "2021", "2022", "2023", "2024",
				"2025", "2026", "2027", "2028", "2029", "2030" }));

		JSpinner spinner_Until_Time = new JSpinner(
				new SpinnerDateModel(new Date(1514315476345L), null, null, Calendar.SECOND));
		spinner_Until_Time.setBounds(215, 16, 110, 23);
		publish_Until_Panel.add(spinner_Until_Time);
		JSpinner.DateEditor de_1 = new JSpinner.DateEditor(spinner_Until_Time, "HH:mm:ss");
		de_1.getTextField().getFormatter();
		spinner_Until_Time.setEditor(de_1);

		JButton btnFinish = new JButton("Finish");
		btnFinish.setMnemonic(KeyEvent.VK_ENTER);
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * whenever the user hits finish, a series of tests is performed on the data
				 * input before the ad is inserted to the databases
				 */
				if (table.getSelectedRowCount() == 0) { // case the user did not chose a sign
					JOptionPane.showConfirmDialog(null, "Select a location and to choose at least one sign",
							"Sign selection", JOptionPane.CLOSED_OPTION);
					return;
				}

				if (AdFormatbtnGrp.getSelection() == null) { // case the user did not chose an ad format
					JOptionPane.showConfirmDialog(null, "Please select ad format", "Type Selection",
							JOptionPane.CLOSED_OPTION);
					return;
				}

				if (browseTextPane.getText().isEmpty()) { // case the user did not upload a file
					JOptionPane.showConfirmDialog(null, "Please upload ad content", "Upload content",
							JOptionPane.CLOSED_OPTION);
					return;
				}

				if (!inputVerifierObject.fileTypeCheck(browseTextPane.getText())) {
					// case the user uploaded a file of type other than txt/MP4/JPG
					JOptionPane.showConfirmDialog(null, "You can only upload txt, JGP, and MP4 files", "Upload file",
							JOptionPane.CLOSED_OPTION);
					return;
				}

				// in order to check the date and time input, the data needs to be set on a
				// Calendar/time stamp
				if (chckbxImmediate.isSelected()) { // if immediate, take the current time as starting time
					startTimeCalendar.setTime(new Timestamp(System.currentTimeMillis()));
					endTimeCalendar.set(Integer.parseInt(untilYear.getValue().toString()),
							comboBox_Until_Month.getSelectedIndex(), comboBox_Until_Day.getSelectedIndex() + 1,
							Integer.parseInt(hourFormatter.format((Date) fromTime.getValue())),
							Integer.parseInt(minuteFormatter.format((Date) fromTime.getValue())),
							Integer.parseInt(secondFormatter.format((Date) fromTime.getValue())));
				} else {
					// if immediate is unchecked, take the data from the "from" fields as a starting
					// time
					startTimeCalendar.set(Integer.parseInt(fromYear.getValue().toString()),
							from_Month.getSelectedIndex(), from_Day.getSelectedIndex() + 1,
							Integer.parseInt(hourFormatter.format((Date) fromTime.getValue())),
							Integer.parseInt(minuteFormatter.format((Date) fromTime.getValue())),
							Integer.parseInt(secondFormatter.format((Date) fromTime.getValue())));
				}
				Timestamp startDate = new Timestamp(startTimeCalendar.getTime().getTime()); // assign as a time stamp

				if (!inputVerifierObject.checkTimeInputValidity(calendar.getTime())) { // check if startDate is in the
																						// past
					JOptionPane.showConfirmDialog(null, "The publishing date you have selected is in the past",
							"Date selection", JOptionPane.CLOSED_OPTION);
					return;
				}

				// now, get the end date and time
				Timestamp endDate;

				List<Ad> tempArrayList = new ArrayList<>();

				int[] selectedRows = table.getSelectedRows();
				if (frequencySelection == "Continuous") {
					if (endTimeCalendar != null) {
						endTimeCalendar.clear();
					}
					Calendar finalAdStream = Calendar.getInstance();

					// get the end time and date from the relevant fields
					finalAdStream.set(Integer.parseInt(untilYear.getValue().toString()),
							comboBox_Until_Month.getSelectedIndex(), comboBox_Until_Day.getSelectedIndex() + 1,
							Integer.parseInt(hourFormatter.format((Date) spinner_Until_Time.getValue())),
							Integer.parseInt(minuteFormatter.format((Date) spinner_Until_Time.getValue())),
							Integer.parseInt(secondFormatter.format((Date) spinner_Until_Time.getValue())));
					// for the continuous option, there are two different end date and time.
					// one from the endDate of the final ad
					// and the other one is the endDate of each publication (every ad screen)

					endTimeCalendar.set(Integer.parseInt(fromYear.getValue().toString()), from_Month.getSelectedIndex(),
							from_Day.getSelectedIndex() + 1,
							Integer.parseInt(hourFormatter.format((Date) spinner_Until_Time.getValue())),
							Integer.parseInt(minuteFormatter.format((Date) spinner_Until_Time.getValue())),
							Integer.parseInt(secondFormatter.format((Date) spinner_Until_Time.getValue())));

					// test if end date is after start date
					if (!inputVerifierObject.checkEndDate(startDate.getTime(), endTimeCalendar.getTime().getTime())
							|| !inputVerifierObject.checkEndDate(startDate.getTime(),
									finalAdStream.getTime().getTime())) {
						JOptionPane.showConfirmDialog(null, "Start publishing time must be before end publishing time",
								"Date selection", JOptionPane.CLOSED_OPTION);
						return;
					}

					List<List<Ad>> continuousAdList = new ArrayList<>();

					// screen the ad every interval according to the user input
					// until we have reached the end date of the final ad screening
					while (endTimeCalendar.before(finalAdStream)) {
						List<Ad> miniAdList = new ArrayList<>();
						for (int thisSign : selectedRows) {
							if (inputVerifierObject.checkIfSignIsBusy(startDate, endTimeCalendar.getTime(),
									(long) table.getValueAt(thisSign, 0))) {
								continue;
							}
							miniAdList.add(new Ad(startTimeCalendar.getTime(), endTimeCalendar.getTime(), publicationID,
									(String) table.getValueAt(thisSign, 1), username,
									(long) table.getValueAt(thisSign, 0), browseTextPane.getText()));

						}
						continuousAdList.add(miniAdList);
						endTimeCalendar.add(Calendar.DATE, (int) cmboBoxEvery.getValue());
						startTimeCalendar.add(Calendar.DATE, (int) cmboBoxEvery.getValue());

					}

					table.clearSelection();
					FrequencybtnGrp.clearSelection();
					AdFormatbtnGrp.clearSelection();
					browseTextPane.setText(null);
					AdInserted = true;
					publicationID++;
					control.addListOfListsOfAds(continuousAdList, username);
					dispose();
					return;
				} else { // if continuous was not selected,
					// add the advertisements to the databases according to the chosen signs
					// where every ad starts according to the startDate input and ends 30 seconds
					// later

					for (int thisSign : selectedRows) {
						if (!chckbxImmediate.isSelected()) {
							endTimeCalendar.setTimeInMillis(startDate.getTime());
							endTimeCalendar.add(Calendar.SECOND, 30);
							endDate = new Timestamp(endTimeCalendar.getTime().getTime());
						}

						endDate = new Timestamp(endTimeCalendar.getTime().getTime());

						if (inputVerifierObject.checkIfSignIsBusy(startDate, endTimeCalendar.getTime(),
								(long) table.getValueAt(thisSign, 0))) {
							continue;
						}
						// tempArrayList holds all of the ads that were accepted
						// and needs to be added to the database
						tempArrayList
								.add(new Ad(startDate, endDate, publicationID, (String) table.getValueAt(thisSign, 1),
										username, (long) table.getValueAt(thisSign, 0), browseTextPane.getText()));
					}
				}

				chckbxImmediate.setSelected(false);
				AdInserted = true;

				if (!tempArrayList.isEmpty())
					control.insertNewAd(tempArrayList, username);
				else { // in case no ads will be added
					JOptionPane.showConfirmDialog(null,
							"Ads were not added because signs are busy during the specified times", "Date selection",
							JOptionPane.CLOSED_OPTION);
				}
				browseTextPane.setText(null);
				table.clearSelection();
				FrequencybtnGrp.clearSelection();
				AdFormatbtnGrp.clearSelection();
				rdbtnText.setEnabled(true);
				browseTextPane.setEnabled(true);
				btnBrowse.setEnabled(true);
				rdbtnGraphic.setEnabled(true);
				rdbtnOnce.setEnabled(true);
				rdbtnSeveral.setEnabled(true);
				rdbtnContinuous.setEnabled(true);
				publicationID++;
				dispose();
			}

		});
		btnFinish.setBounds(840, 444, 89, 23);
		getContentPane().add(btnFinish);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(747, 444, 89, 23);
		getContentPane().add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
				} else if (response == JOptionPane.YES_OPTION)
					dispose();
				else if (response == JOptionPane.NO_OPTION)
					dispose();
			}
		});

		btnSeveralAds = new JButton("Add more ads");
		btnSeveralAds.setBounds(606, 444, 137, 23);
		getContentPane().add(btnSeveralAds);
		btnSeveralAds.setVisible(false);
		btnSeveralAds.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnText.setEnabled(false);
				browseTextPane.setEnabled(false);
				btnBrowse.setEnabled(false);
				rdbtnGraphic.setEnabled(false);
				rdbtnOnce.setEnabled(false);
				rdbtnSeveral.setEnabled(true);
				rdbtnContinuous.setEnabled(false);

				startTimeCalendar.set(Integer.parseInt(fromYear.getValue().toString()), from_Month.getSelectedIndex(),
						from_Day.getSelectedIndex() + 1,
						Integer.parseInt(hourFormatter.format((Date) fromTime.getValue())),
						Integer.parseInt(minuteFormatter.format((Date) fromTime.getValue())),
						Integer.parseInt(secondFormatter.format((Date) fromTime.getValue())));

				Timestamp startDate = new Timestamp(startTimeCalendar.getTime().getTime());
				Timestamp endDate;

				List<Ad> tempArrayList = new ArrayList<>();
				int[] selectedRows = table.getSelectedRows();
				for (int thisSign : selectedRows) {

					endTimeCalendar.setTimeInMillis(startDate.getTime());
					endTimeCalendar.add(Calendar.SECOND, 30);
					endDate = new Timestamp(endTimeCalendar.getTime().getTime());

					tempArrayList.add(new Ad(startDate, endDate, publicationID, (String) table.getValueAt(thisSign, 1),
							username, (long) table.getValueAt(thisSign, 0), browseTextPane.getText()));
				}
				setAdInserted(true);
				control.insertNewAd(tempArrayList, username);
				table.clearSelection();
				AdFormatbtnGrp.clearSelection();
			}
		});

		JScrollPane scrollPaneTable = new JScrollPane();
		scrollPaneTable.setBounds(383, 54, 546, 379);
		getContentPane().add(scrollPaneTable);

		table = new JTable(tableModel);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		scrollPaneTable.setViewportView(table);
		table.setRequestFocusEnabled(false);
		table.setIntercellSpacing(new Dimension(3, 2));
		table.setFocusTraversalKeysEnabled(false);
		table.setFocusCycleRoot(true);
		table.setFillsViewportHeight(true);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setAutoCreateRowSorter(true);

		JSeparator separator = new JSeparator();
		separator.setBounds(14, 32, 822, 23);
		getContentPane().add(separator);

		JLabel label = new JLabel("PostNET");
		label.setForeground(SystemColor.textHighlight);
		label.setFont(new Font("Times New Roman", Font.BOLD, 20));
		label.setBounds(14, 11, 157, 23);
		getContentPane().add(label);

		chckbxImmediate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxImmediate.isSelected()) {
					from_Day.setEnabled(false);
					from_Month.setEnabled(false);
					fromYear.setEnabled(false);
					fromTime.setEnabled(false);
				} else {
					from_Day.setEnabled(true);
					from_Month.setEnabled(true);
					fromYear.setEnabled(true);
					fromTime.setEnabled(true);
				}
			}
		});
		return false;

	}

	public void SignTypeFilter(boolean isSelected, String typeSelected) { // filters the signs table according to the
																			// chosen parameters
		List<Sign> typeFilteredSignList = new ArrayList<>();
		Predicate<Sign> FilterString = Sign -> Sign.getType().equals(typeSelected);
		if (isSelected)
			locationFilteredSignList.stream().filter(FilterString).forEach(typeFilteredSignList::add);
		tableModel.setData(typeFilteredSignList);
		tableModel.fireTableDataChanged();
	}

	public static String getTypeSelection() {
		return TypeSelection;
	}

	Function<String, Predicate<Sign>> locationEquals = pivot -> sign -> sign.getLocation().equals(pivot);

	public void LocationSignFilter(boolean isSelected, String location) {
		Predicate<Sign> FilterString = Sign -> Sign.getLocation().equals(location);
		if (isSelected) {
			control.getAllSigns().stream().filter(locationEquals.apply(location))
					.forEach(locationFilteredSignList::add);
		} else {
			locationFilteredSignList.removeIf(FilterString);
		}
		tableModel.setData(locationFilteredSignList);
		tableModel.fireTableDataChanged();

	}

}