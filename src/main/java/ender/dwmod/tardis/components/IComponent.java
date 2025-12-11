package ender.dwmod.tardis.components;

public interface IComponent {
    public void install();
    public void uninstall();
    public boolean isPresent();
    public boolean isPowered();
    public void setPowered(boolean powered);
    public String getName();
}
