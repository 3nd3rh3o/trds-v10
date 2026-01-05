package ender.dwmod.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

public class StringToComponentParser {
    public static List<Component> parseStringToComponents(String text) {
        List<Component> components = new ArrayList<>();
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i+=2) {
            List<String> parts = List.of(lines[i+1].split("[+]"));
            int color = Integer.valueOf(parts.get(0));
            boolean isItalic = Boolean.valueOf(parts.get(1));
            boolean isBold = Boolean.valueOf(parts.get(2));
            boolean isUnderlined = Boolean.valueOf(parts.get(3));
            components.add(Component.literal(lines[i]).withColor(color).withStyle(Style.EMPTY.withItalic(isItalic).withBold(isBold).withUnderlined(isUnderlined)));
        }
        return components;
    }

    public static List<FormattedCharSequence> parseStringToFormattedCharSequences(String text) {
        List<FormattedCharSequence> sequences = new ArrayList<>();
        List<Component> components = parseStringToComponents(text);
        for (Component comp : components) {
            sequences.add(comp.getVisualOrderText());
        }
        return sequences;
    }

    public static String serializeComponentsToString(List<Component> components) {
        StringBuilder sb = new StringBuilder();
        for (Component comp : components) {
            sb.append(comp.getString()).append("\n");
            Style style = comp.getStyle();
            int color = style.getColor() != null ? style.getColor().getValue() : 0xFFFFFF;
            boolean isItalic = style.isItalic();
            boolean isBold = style.isBold();
            boolean isUnderlined = style.isUnderlined();
            sb.append(String.valueOf(color)).append("+").append(isItalic).append("+").append(isBold).append("+").append(isUnderlined).append("\n");
        }
        return sb.toString();
    }
}
