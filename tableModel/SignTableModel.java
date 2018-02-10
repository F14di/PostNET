package tableModel;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import Databases.Sign;

public class SignTableModel extends AbstractTableModel {

	private List<Sign> SignList;
	private String[] colNames = { "Sign ID", "Sign type", "Location", "Urban/Rural" };

	public SignTableModel(List<Sign> thisList) {
		setData(thisList);
	}

	public void setData(List<Sign> SignList) {
		this.SignList = SignList;
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

		return SignList.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (SignList.isEmpty())
			return 0;
		Sign sign = SignList.get(row);

		switch (col) {
		case 0:
			return sign.getSignID();
		case 1:
			return sign.getType();
		case 2:
			return sign.getLocation();
		case 3:
			return sign.getUrbanRural();
		}
		return null;
	}

}