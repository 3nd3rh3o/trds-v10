package ender.dwmod.entities.tardis.exoshell;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.UnsignedInteger;

import ender.dwmod.entities.IMultiCollidable;
import ender.dwmod.tardis.TardisRegistries;
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
import net.minecraft.world.phys.AABB;
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
public class TardisEntity extends LivingEntity implements GeoEntity, IMultiCollidable {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private UnsignedInteger ID;

    private static final List<AABB> PHYSIC_COLLIDERS;

    
    public TardisEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        hasImpulse = true;
    }

    
    
    @Override
    public AABB getBoundingBox() {
        if (!level().isClientSide)
            return getColliders().get(0).move(getX(), getY(), getZ());
        return super.getBoundingBox();
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
    public void tick() {
        super.tick();
        if (onGround()) // ensure block alignement and rotation modulo.
            if (((int)this.position().x) - this.position().x != 0 || ((int)this.position().z) - this.position().z != 0 || this.getRotationVector().y != 0 || this.yBodyRot != 0) {
                this.setPos(((int)this.position().x), this.position().y, ((int)this.position().z));
                this.setYRot(0f);
                this.setYHeadRot(0f);
                this.yBodyRot = 0f;
                if (!level().isClientSide())
                    TardisRegistries.getTardis(ID).updatePosition(this);
                this.hasImpulse = true; // inform MC that position changed to update clients
            }
        else
            if (!level().isClientSide() && !onGround())
                TardisRegistries.getTardis(ID).updatePosition(this);
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
        return false;
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
        if (!tardisData.contains("ID") && !level().isClientSide())
            ID = TardisRegistries.createTardis(this).id(); // create new Tardis on spawn
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

    @Override
    protected void doPush(Entity entity) {
        return;
    }

    // On death remove Tardis from registries
    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (!level().isClientSide() && ID != null)
            TardisRegistries.deleteTardis(ID, this.level().getServer());
    }

    @Override // TODO - add actual colliders depending on state
                // and make the program use them !
    public List<AABB> getColliders() 
    {
        return PHYSIC_COLLIDERS;
    }


    
    // TODO - change to a switch and add correct values
    static {
            // SOUTH - DEFAULT / DOOR CLOSED
        PHYSIC_COLLIDERS = List.of( // blockbench coords / 16
            new AABB(-0.875, 0, -0.875, 0.875, 0.125, 0.875), // base
            new AABB(-0.875, 0.125, -0.875, -0.75, 2.875, 0.875), // left
            new AABB(0.75, 0.125, -0.875, 0.875, 2.875, 0.875), // right
            new AABB(-0.75, 0.125, -0.875, 0.75, 2.875, -0.75), // back
            new AABB(-0.875, 2.875, -0.875, 0.875, 3, 0.875) // roof
        );
    }
}
