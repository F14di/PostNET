package tableModel;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import Databases.Ad;

public class AdsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private List<Ad> adsList;
	private String[] colNames = { "Filename", "Type", "Location", "SignID", "Start Time", "End Time", "Urban/Rural" };

	public AdsTableModel(List<Ad> AdsList) {
		setData(AdsList);

	}

	public void setData(List<Ad> AdsList) {
		this.adsList = AdsList;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public int getRowCount() {

		return adsList.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (adsList.isEmpty())
			return 0;
		Ad Ad = adsList.get(row);

		switch (col) {
		case 0:
			return Ad.getFileName();
		case 1:
			return Ad.getAdType();
		case 2:
			return Ad.getSign().getLocation();
		case 3:
			return Ad.getSign().getSignID();
		case 4:
			return Ad.getStartDate();
		case 5:
			return Ad.getEndDate();
		case 6:
			return Ad.getSign().getUrbanRural();
		}
		return null;
	}

}