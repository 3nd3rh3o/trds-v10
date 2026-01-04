package ender.dwmod.tardis.components;

public class ExtDoor extends AbstractComponentCategory {

    public ExtDoor() {
        boolVars.put("door_state", false);
        boolVars.put("door_lock", false);
    }

    @Override
    public String getName() {
        return ExtDoor.name();
    }

    public static String name() {
        return "extDoor";
    }
}
