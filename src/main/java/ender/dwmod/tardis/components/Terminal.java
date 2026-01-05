package ender.dwmod.tardis.components;

import java.util.List;

import ender.dwmod.utils.StringToComponentParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class Terminal extends AbstractComponentCategory {
    public Terminal() {
        stringVars.put("terminal_text", StringToComponentParser.serializeComponentsToString(List.of(Component.literal(">").withStyle(Style.EMPTY))));
    }

    @Override
    public String getName() {
        return Terminal.name();
    }

    public static String name() {
        return "terminal";
    }
}
