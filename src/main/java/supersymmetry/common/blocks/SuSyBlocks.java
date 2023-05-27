package supersymmetry.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import scala.xml.dtd.impl.Base;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SuSyBlocks {

    public static BlockCoolingCoil COOLING_COIL;
    public static BlockSinteringBrick SINTERING_BRICK;
    public static BlockCoagulationTankWall COAGULATION_TANK_WALL;
    public static final EnumMap<SusyStoneVariantBlock.StoneVariant, SusyStoneVariantBlock> SUSY_STONE_BLOCKS = new EnumMap(SusyStoneVariantBlock.StoneVariant.class);
    public static BlockAlternatorCoil ALTERNATOR_COIL;
    public static BlockTurbineRotor TURBINE_ROTOR;
    public static BlockSeparatorRotor SEPARATOR_ROTOR;
    public static BlockDrillHead DRILL_HEAD;
    public static BlockStructural STRUCTURAL_BLOCK;
    public static BlockStructural1 STRUCTURAL_BLOCK_1;
    public static BlockDeposit DEPOSIT_BLOCK;
    public static BlockResource RESOURCE_BLOCK;
    public static BlockHome HOME;

    public static void init() {
        COOLING_COIL = new BlockCoolingCoil();
        COOLING_COIL.setRegistryName("cooling_coil");

        SINTERING_BRICK = new BlockSinteringBrick();
        SINTERING_BRICK.setRegistryName("sintering_brick");

        DRILL_HEAD = new BlockDrillHead();
        DRILL_HEAD.setRegistryName("drill_head");

        COAGULATION_TANK_WALL = new BlockCoagulationTankWall();
        COAGULATION_TANK_WALL.setRegistryName("coagulation_tank_wall");

        for (SusyStoneVariantBlock.StoneVariant shape : SusyStoneVariantBlock.StoneVariant.values()) {
            SUSY_STONE_BLOCKS.put(shape, new SusyStoneVariantBlock(shape));
        }

        ALTERNATOR_COIL = new BlockAlternatorCoil();
        ALTERNATOR_COIL.setRegistryName("alternator_coil");

        TURBINE_ROTOR = new BlockTurbineRotor();
        TURBINE_ROTOR.setRegistryName("turbine_rotor");

        SEPARATOR_ROTOR = new BlockSeparatorRotor();
        SEPARATOR_ROTOR.setRegistryName("separator_rotor");

        STRUCTURAL_BLOCK = new BlockStructural();
        STRUCTURAL_BLOCK.setRegistryName("structural_block");

        STRUCTURAL_BLOCK_1 = new BlockStructural1();
        STRUCTURAL_BLOCK_1.setRegistryName("structural_block_1");

        DEPOSIT_BLOCK = new BlockDeposit();
        DEPOSIT_BLOCK.setRegistryName("deposit_block");

        RESOURCE_BLOCK = new BlockResource();
        RESOURCE_BLOCK.setRegistryName("resource_block");

        HOME = new BlockHome();
        HOME.setRegistryName("home_block");
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModels() {
        COOLING_COIL.onModelRegister();
        SINTERING_BRICK.onModelRegister();
        registerItemModel(COAGULATION_TANK_WALL);
        for (SusyStoneVariantBlock block : SUSY_STONE_BLOCKS.values())
            registerItemModel(block);
        registerItemModel(ALTERNATOR_COIL);
        registerItemModel(DRILL_HEAD);
        registerItemModel(TURBINE_ROTOR);
        registerItemModel(SEPARATOR_ROTOR);
        registerItemModel(STRUCTURAL_BLOCK);
        registerItemModel(STRUCTURAL_BLOCK_1);
        registerItemModel(DEPOSIT_BLOCK);
        registerItemModel(RESOURCE_BLOCK);
        registerItemModel(HOME);
    }

    @SideOnly(Side.CLIENT)
    private static void registerItemModel(@NotNull Block block) {
        for (IBlockState state : block.getBlockState().getValidStates()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),
                    block.getMetaFromState(state),
                    new ModelResourceLocation(block.getRegistryName(),
                            statePropertiesToString(state.getProperties())));
        }
    }

    public static @NotNull String statePropertiesToString(@NotNull Map<IProperty<?>, Comparable<?>> properties) {
        StringBuilder stringbuilder = new StringBuilder();

        List<Map.Entry<IProperty<?>, Comparable<?>>> entries = properties.entrySet().stream()
                .sorted(Comparator.comparing(c -> c.getKey().getName()))
                .collect(Collectors.toList());

        for (Map.Entry<IProperty<?>, Comparable<?>> entry : entries) {
            if (stringbuilder.length() != 0) {
                stringbuilder.append(",");
            }

            IProperty<?> property = entry.getKey();
            stringbuilder.append(property.getName());
            stringbuilder.append("=");
            stringbuilder.append(getPropertyName(property, entry.getValue()));
        }

        if (stringbuilder.length() == 0) {
            stringbuilder.append("normal");
        }

        return stringbuilder.toString();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> @NotNull String getPropertyName(@NotNull IProperty<T> property, Comparable<?> value) {
        return property.getName((T) value);
    }
}
