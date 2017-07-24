package spotlight.model;

import java.util.ArrayList;

public class RawMovie {
	private ArrayList<String> titleList;
	private String rawTitle;
	private ArrayList<Integer> dataList;
	
	public RawMovie(){
		super();
		this.titleList = new ArrayList<String>();
		this.dataList = new ArrayList<Integer>();
	}

	@Override
	public String toString() {
		return "Title:\t\t" + titleList + "\nrawTitle:\t" + rawTitle + "\ndata:\t\t" + dataList + "\n";
	}

	public ArrayList<String> getTitleList() {
		return titleList;
	}

	public String getRawTitle() {
		return rawTitle;
	}

	public ArrayList<Integer> getDataList() {
		return dataList;
	}

	public void setTitleList(ArrayList<String> titleList) {
		this.titleList = titleList;
	}

	public void setRawTitle(String rawTitle) {
		this.rawTitle = rawTitle;
	}

	public void setDataList(ArrayList<Integer> dataList) {
		this.dataList = dataList;
	}

	public RawMovie(String rawTitle) {
		super();
		this.rawTitle = rawTitle;
		this.titleList = new ArrayList<String>();
		this.dataList = new ArrayList<Integer>();
	}

}
