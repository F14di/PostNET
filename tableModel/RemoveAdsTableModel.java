package tableModel;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import Databases.Ad;

public class RemoveAdsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private List<Ad> adsList;
	private String[] colNames = { "Row", "Filename", "Type", "Publication ID" };

	public RemoveAdsTableModel(List<Ad> AdsList) {
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
		return 4;
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
			return row + 1;
		case 1:
			return Ad.getFileName();
		case 2:
			return Ad.getAdType();
		case 3:
			return Ad.getPublicationID();

		}
		return null;
	}

}