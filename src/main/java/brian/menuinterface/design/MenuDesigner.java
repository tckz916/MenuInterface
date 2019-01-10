package brian.menuinterface.design;

import brian.menuinterface.IMenu;
import brian.menuinterface.button.DefaultButtons;
import brian.menuinterface.button.IMenuButton;
import brian.menuinterface.button.MenuButton;
import brian.menuinterface.exceptions.IncorrectRowSizeException;
import org.bukkit.inventory.Inventory;
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
	Map<Integer, String> rowChars = new HashMap<>();

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

	public MenuDesigner setItem(String character, ItemStack item) {
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

	public MenuDesigner setButton(String character, IMenuButton button) {
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

	public MenuDesigner setDesign(int row, String design) {
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
		for (int row : rowChars.keySet()) {

			String rowDesign = rowChars.get(row);
			int currentSlot = 0;

			for (Character character : rowDesign.toCharArray()) {
				if (!character.toString().equalsIgnoreCase("S") && items.containsKey(character.toString())) {

					int slot = currentSlot;
					if (row != 1)
						slot = ((row - 1) * 9) + currentSlot;

					IMenuButton button = items.get(character.toString()).clone();
					button.setSlot(slot);

					menu.addButton(button);

				}
				currentSlot++;
			}
		}
	}

	/**
	 * Applies design in items form aka sets the items
	 * 
	 * @param menu
	 *            is a {@link IMenu}
	 */

	public void applyAsItems(IMenu menu) {
		for (int row : rowChars.keySet()) {

			String rowDesign = rowChars.get(row);
			int currentSlot = 0;

			Inventory inventory = menu.getInventory();

			for (Character character : rowDesign.toCharArray()) {
				if (!character.toString().equalsIgnoreCase("S") && items.containsKey(character.toString())) {

					int slot = currentSlot;
					if (row != 1)
						slot = ((row - 1) * 9) + currentSlot;

					IMenuButton button = items.get(character.toString()).clone();
					button.setSlot(slot);

					menu.addButton(button);
					inventory.setItem(slot, button.getItem());

				}
				currentSlot++;
			}
		}
	}

	public Map<Integer, String> getRowChars() {
		return rowChars;
	}

	public Map<String, IMenuButton> getItems() {
		return items;
	}

}
