package ender.dwmod.tardis.components;

public class Interrior extends AbstractComponentCategory {
    
    public Interrior() {
        boolVars.put("light_state", false);
    }

    @Override
    public String getName() {
        return Interrior.name();
    }

    public static String name() {
        return "interrior";
    }
}
