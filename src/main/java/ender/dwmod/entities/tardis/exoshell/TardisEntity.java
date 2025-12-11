package ender.dwmod.entities.tardis.exoshell;

import java.util.ArrayList;

import com.google.common.primitives.UnsignedInteger;

import ender.dwmod.tardis.TardisRegisties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;


// TODO - add Tracker for variables. 
//      (server) TardisData -> (server) Entity -> (on change!)(client) Entity 
//      also remember to send update packets to trigger animations.
// TardisData are in a world capability, and saved with the world data, to allow persistence of bigger nbt on a centralized place.
// Anything related to a Tardis(not entity) instance must NEVER handle data storage! they are only display and interaction proxies.
// So read state => ok
// Set var(TardisData) => ok
// Set entity from entity event => NOT OK
// Set TardisData from entity event => ok but is done via a trigger on the Tardis Instance and is server side only.
//  if in client => use a packet ! (player click ? but should be handled by minecraft so a bit pointless)
public class TardisEntity extends LivingEntity implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private UnsignedInteger ID;

    
    public TardisEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        return;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return new ArrayList<ItemStack>();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        return;
    }

    // clamp position to block center, and lock rotation
    // TODO - Portal logic
    //      if door opened => 
    //          if portal not present => spawn it
    //          If falling => tp portal to the correct relative position
    //      if door closed => if portal is present => remove portal
    @Override
    public void tick() { // FIXME - pos and rot not properly synced clientside
        if (((int)this.position().x) - this.position().x != 0 || ((int)this.position().z) - this.position().z != 0) {
            this.setPos(((int)this.position().x), this.position().y, ((int)this.position().z));
        }
        if (this.getRotationVector().y != 0) {
            this.setYRot(0f);
        }
        super.tick();
    }

    // override damages, unless if done by command or in mod logic
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(source, amount);
        }
        return false;
    }

    // make entity immovable by pushes
    @Override
    public void push(double x, double y, double z) {
        return;
    }

    // make entity solid to other entities so they can walk on it
    @Override
    public boolean isPushable() {
        return true;
    }

    // prevent player entering aabb while coliding
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    // make entity immovable by other entities
    @Override
    public void push(Entity entity) {
        return;
    }
    
    // On load ID from NBT
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        CompoundTag tardisData = compound.getCompound("TardisData");
        if (!tardisData.contains("ID"))
            ID = TardisRegisties.createTardis(this).id(); // create new Tardis on spawn
        else 
            ID = UnsignedInteger.fromIntBits(tardisData.getInt("ID")); // just regular loading, not a spawn
        super.readAdditionalSaveData(compound);
    }

    // On save ID to NBT
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        CompoundTag tardisData = new CompoundTag();
        tardisData.putInt("ID", ID.intValue());
        compound.put("TardisData", tardisData);
        super.addAdditionalSaveData(compound);
    }

    // On death remove Tardis from registries
    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (ID != null) // ensure server side
            TardisRegisties.deleteTardis(ID);
    }
}
