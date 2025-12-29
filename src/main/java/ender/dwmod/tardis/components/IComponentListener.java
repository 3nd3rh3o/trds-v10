package ender.dwmod.tardis.components;

public interface IComponentListener {
    public void register();
    public void unRegister();
    public void onComponentValueChanged();
}
