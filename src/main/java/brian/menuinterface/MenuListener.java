package brian.menuinterface;

import brian.menuinterface.button.ButtonOptions;
import brian.menuinterface.button.IMenuButton;
import brian.menuinterface.events.ButtonClickEvent;
import brian.menuinterface.events.ClickType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuListener implements Listener {

    private static boolean initialized;
    private MenuListener(){}
    private MenuListener(JavaPlugin plugin){
        Bukkit.getPluginManager().registerEvents(this,plugin);
        initialized = true;
    }
    public static void register(JavaPlugin plugin){

        if(!initialized) new MenuListener(plugin);

    }

    @EventHandler
    public void listenClick(InventoryClickEvent event){

        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().getHolder() instanceof IMenu) {

            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

            final ItemStack clickedItem = event.getCurrentItem();
            IMenu menu = (IMenu) event.getClickedInventory().getHolder();
            IMenuButton clickedButton = menu.findButtonByItem(clickedItem);

            if (clickedButton != null) {

                if(clickedButton.containsData(ButtonOptions.CANCEL_EVENT.getIdentifier())) {

                    if (((boolean) clickedButton.getItemData().get(ButtonOptions.CANCEL_EVENT.getIdentifier()))) {
                        event.setCancelled(true);
                    }

                } else event.setCancelled(true);

                ClickType clickType;
                if(event.isShiftClick()){
                    if(event.isLeftClick()) clickType = ClickType.SHIFT_LEFT;
                    else clickType = ClickType.SHIFT_RIGHT;
                } else {
                    if(event.isLeftClick()) clickType = ClickType.LEFT;
                    else clickType = ClickType.RIGHT;
                }

                ButtonClickEvent buttonClickEvent = new ButtonClickEvent(clickedButton, ((Player) event.getWhoClicked()),event.getClickedInventory(),event.getSlot(),clickType);

                if(clickedButton.getClickEvent() != null){
                    clickedButton.getClickEvent().accept(buttonClickEvent);
                } else if(menu.getClickListener() != null) menu.getClickListener().accept(buttonClickEvent);

            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){

        if(event.getInventory().getHolder() instanceof IMenu){

            if(((IMenu) event.getInventory().getHolder()).getCloseListener() != null) ((IMenu) event.getInventory().getHolder()).getCloseListener().accept(event);

        }

    }

    @EventHandler
    public void onClose(InventoryOpenEvent event){

        if(event.getInventory().getHolder() instanceof IMenu){

            if(((IMenu) event.getInventory().getHolder()).getOpenListener() != null) ((IMenu) event.getInventory().getHolder()).getOpenListener().accept(event);

        }

    }

}
