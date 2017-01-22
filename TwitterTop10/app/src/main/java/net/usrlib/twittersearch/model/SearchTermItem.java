package net.usrlib.twittersearch.model;

import android.database.Cursor;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class SearchTermItem {
	public static final String ITEM_ID_COLUMN = "itemId";
	public static final String DESCRIPTION_COLUMN = "description";

	private int itemId;
	private String description;

	public int getItemId() {
		return itemId;
	}

	public String getDescription() {
		return description;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public static SearchTermItem fromDbCursor(final Cursor cursor) {
		if (cursor == null) {
			return null;
		}

		final SearchTermItem searchItem = new SearchTermItem();

		searchItem.itemId = cursor.getInt(cursor.getColumnIndex(ITEM_ID_COLUMN));
		searchItem.description = cursor.getString(cursor.getColumnIndex(DESCRIPTION_COLUMN));

		return searchItem;
	}

	public static final String formatDescriptionValue(final String description) {
		final String[] values = description.replaceAll("#", "").split(" ");
		String entry = "";

		for (int i = 0; i < values.length; i++) {
			String value = values[i].replaceAll(" ", "");
			if (value.length() > 1) {
				entry += String.format("#%s ", value);
			}
		}

		return entry;
	}
}
