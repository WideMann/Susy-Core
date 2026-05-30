package supersymmetry.common.item.armor;

import static supersymmetry.api.util.SuSyUtility.susyId;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import gregtech.api.items.metaitem.stats.IItemDurabilityManager;
import supersymmetry.client.renderer.handler.GeoMetaArmorRenderer;
import supersymmetry.client.renderer.handler.ITextureRegistrar;

public class SimpleArmor implements IItemDurabilityManager, ITextureRegistrar {

    @SideOnly(Side.CLIENT)
    protected ModelBiped model;

    protected final EntityEquipmentSlot SLOT;
    protected int maxDurability;
    protected int tier;
    protected double relativeAbsorption;
    protected String name;

    private static final double DEFAULT_ABSORPTION = 0;

    public SimpleArmor(EntityEquipmentSlot slot, int maxDurability, String name, int tier, double relativeAbsorption) {
        SLOT = slot;
        this.maxDurability = maxDurability;
        this.tier = tier;
        this.relativeAbsorption = relativeAbsorption;
        this.name = name;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return getDurability(itemStack) / maxDurability;
    }


    public void damageArmor(EntityLivingBase entity, ItemStack itemStack, DamageSource source, int damage,
                            EntityEquipmentSlot equipmentSlot) {
        itemStack.attemptDamageItem(damage, entity.getRNG(), null);
        if (damage > 0) {
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, itemStack);
            int j = 0;

            for (int k = 0; i > 0 && k < damage; ++k) {
                if (EnchantmentDurability.negateDamage(itemStack, i, entity.getRNG())) {
                    ++j;
                }
            }

            damage -= j;

            if (damage <= 0) {
                return;
            }
        }

        changeDurability(itemStack, -damage);
    }

    int getDurability(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            return 0;
        }
        if (!stack.getTagCompound().hasKey("durability")) {
            stack.getTagCompound().setInteger("durability", maxDurability);
        }
        return stack.getTagCompound().getInteger("durability");
    }

    void changeDurability(ItemStack stack, int durabilityChange) {
        if (!stack.hasTagCompound()) {
            return;
        }
        NBTTagCompound compound = stack.getTagCompound();
        compound.setInteger("durability", getDurability(stack) + durabilityChange);
        stack.setTagCompound(compound);
    }


    public EntityEquipmentSlot getEquipmentSlot(ItemStack itemStack) {
        return SLOT;
    }


    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type, String name) {
        return switch (SLOT) {
            case HEAD -> "susy:textures/armor/" + name + "_head";
            case CHEST -> "susy:textures/armor/" + name + "_chest";
            case LEGS -> "susy:textures/armor/" + name + "_legs";
            case FEET -> "susy:textures/armor/" + name + "_feet";
            default -> null;
        };
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot slot,
                                    ModelBiped defaultModel) {
        return GeoMetaArmorRenderer.INSTANCE.setCurrentItem(entityLiving, itemStack, slot)
                .applyEntityStats(defaultModel).applySlot(slot);
    }

    @Override
    public List<ResourceLocation> getTextureLocations() {
        List<ResourceLocation> models = new ArrayList<>();
        switch (SLOT) {
            case HEAD -> models.add(susyId("armor/" + name + "_head"));
            case CHEST -> models.add(susyId("armor/" + name + "_chest"));
            case LEGS -> models.add(susyId("armor/" + name + "_legs"));
            case FEET -> models.add(susyId("armor/" + name + "_feet"));
        }
        return models;
    }
}
