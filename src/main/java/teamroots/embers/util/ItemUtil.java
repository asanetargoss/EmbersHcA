package teamroots.embers.util;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemUtil {

    public static final ItemStack EMPTY_ITEM_STACK = null;
    
    public static boolean stackEmpty(ItemStack itemStack) {
        return itemStack == EMPTY_ITEM_STACK;
    }
    
    protected static final ItemStack DEFAULT_TEST_STACK = new ItemStack(Items.APPLE);
    public static boolean slotFull(IItemHandler handler, int slot) {
        ItemStack testStack = handler.getStackInSlot(slot);
        if (stackEmpty(testStack)) {
            testStack = DEFAULT_TEST_STACK;
        }
        ItemStack simulatedResult = handler.insertItem(slot, testStack, true);
        return stackEmpty(simulatedResult) || (!stackEmpty(testStack) && simulatedResult.stackSize < testStack.stackSize);
    }
    
    public static @Nullable Item getItem(ItemStack stack) {
        if (stackEmpty(stack)) {
            return null;
        }
        return stack.getItem();
    }

}
