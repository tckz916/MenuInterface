package brian.menuinterface.types;

import brian.menuinterface.IMenu;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class StandardMenu extends IMenu {

    private StandardMenu(int rows, String title){
        setTitle(title);
        setInventorySize(rows);
    }
    public static StandardMenu create(int rows, String title){
        return new StandardMenu(rows,title);
    }

    @Override
    public Inventory build() {

        Inventory inv = Bukkit.createInventory(this, getSize(),getTitle());
        setDummies(inv);
        setInventory(inv);
        return inv;

    }
}
