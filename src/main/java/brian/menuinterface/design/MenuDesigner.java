package brian.menuinterface.design;

import brian.menuinterface.IMenu;
import brian.menuinterface.button.DefaultButtons;
import brian.menuinterface.button.IMenuButton;
import brian.menuinterface.button.MenuButton;
import brian.menuinterface.exceptions.IncorrectRowSizeException;
import com.google.common.collect.Iterables;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * By default character S = none, it will skip that character.
 */

public class MenuDesigner {
	/**
	 * Represents a character and a IMenuButton
	 */
	Map<String, IMenuButton> items = new HashMap<>();

	/**
	 * Represents a row and a characters
	 */
	Map<Row, String> rowChars = new HashMap<>();

	private MenuDesigner() {}

	public static MenuDesigner create() {
		return new MenuDesigner();
	}

	/**
	 * @param character
	 *            represents a character to replace with item
	 * @param item
	 *            replaces character with the item.
	 * @return returns {@link #MenuDesigner()}
	 */

	public brian.menuinterface.design.MenuDesigner setItem(String character, ItemStack item) {
		items.put(character, new MenuButton(item, -1, DefaultButtons.FILLER));
		return this;
	}

	/**
	 * @param character
	 *            represents a character to replace with item
	 * @param button
	 *            replaces character with the button
	 * @return returns {@link #MenuDesigner()}
	 */

	public brian.menuinterface.design.MenuDesigner setButton(String character, IMenuButton button) {
		items.put(character, button);
		return this;
	}

	/**
	 * @param row
	 *            = represents an number of inventory row.
	 * @param design
	 *            = is a string that must contains 9 characters, if not a
	 *            {@link IncorrectRowSizeException} will be thrown
	 * @return returns {@link #MenuDesigner()}
	 */

	public brian.menuinterface.design.MenuDesigner setDesign(Row row, String design) {
		if (design.length() > 9 || design.length() < 9) {

			try {
				throw new IncorrectRowSizeException(
						"Incorrect row design size. Required: 9, found: " + design.length());
			} catch (IncorrectRowSizeException e) {
				e.printStackTrace();
			}

		}
		rowChars.put(row, design);
		return this;
	}

	/**
	 * Applies design to the menu in buttons form.
	 * It adds the items (fillers) to the ButtonList in {@link IMenu#buttons}
	 * 
	 * @param menu
	 *            is a {@link IMenu}
	 */

	public void applyAsButtons(IMenu menu) {

		int availableRows = menu.getSize() / 9;

		Map<Integer, Integer> rowSlots = new HashMap<>();
		for(int i = 1; i < availableRows+1; i++){

			rowSlots.put(i, (i * 9)-9);

		}

		int lastStartingSlot = Iterables.getLast(rowSlots.values());

		for (Row row : rowChars.keySet()) {

			String rowDesign = rowChars.get(row);
			int currentSlot = 0;

			if(row.getType() == RowType.NUMBER) {

				for (Character character : rowDesign.toCharArray()) {
					if (!character.toString().equalsIgnoreCase("S") && items.containsKey(character.toString())) {

						int slot = currentSlot;
						if (row.getRow() != 1)
							slot = ((row.getRow() - 1) * 9) + currentSlot;

						IMenuButton button = items.get(character.toString()).clone();
						button.setSlot(slot);

						menu.addButton(button);
					}
					currentSlot++;
				}
			} else if(row.getType() == RowType.FIRST){

				for (Character character : rowDesign.toCharArray()) {
					if (!character.toString().equalsIgnoreCase("S") && items.containsKey(character.toString())) {

						IMenuButton button = items.get(character.toString()).clone();
						button.setSlot(currentSlot);

						menu.addButton(button);
					}
					currentSlot++;
				}

			} else if(row.getType() == RowType.LAST){

				currentSlot = lastStartingSlot;

				for (Character character : rowDesign.toCharArray()) {
					if (!character.toString().equalsIgnoreCase("S") && items.containsKey(character.toString())) {

						IMenuButton button = items.get(character.toString()).clone();
						button.setSlot(currentSlot != menu.getSize() ? currentSlot : currentSlot--);

						menu.addButton(button);
					}
					currentSlot++;
				}

			}
		}
	}


	public Map<Row, String> getRowChars() {
		return rowChars;
	}

	public Map<String, IMenuButton> getItems() {
		return items;
	}

}
