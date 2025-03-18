package us.timinc.mc.cobblemon.shearems.mixins.minecraft;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.core.dispenser.ShearsDispenseItemBehavior.class)
public class ShearsDispenseItemBehaviorMixin {
    @org.spongepowered.asm.mixin.injection.Inject(method = "execute", at = @org.spongepowered.asm.mixin.injection.At("HEAD"), cancellable = true)
    void tryShearLivingEntity(net.minecraft.core.dispenser.BlockSource blockSource, net.minecraft.world.item.ItemStack itemStack, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<net.minecraft.world.item.ItemStack> cir) {
        net.minecraft.server.level.ServerLevel serverLevel = blockSource.level();
        net.minecraft.core.BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(net.minecraft.world.level.block.DispenserBlock.FACING));

        java.util.List<net.minecraft.world.entity.LivingEntity> list = serverLevel.getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class, new net.minecraft.world.phys.AABB(blockPos), net.minecraft.world.entity.EntitySelector.NO_SPECTATORS);

        String itemKey = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString();
        boolean didShear = false;
        for (net.minecraft.world.entity.LivingEntity livingEntity : list) {
            if (livingEntity instanceof com.cobblemon.mod.common.entity.pokemon.PokemonEntity pokemonEntity) {
                if (us.timinc.mc.cobblemon.shearems.Shearems.INSTANCE.getConfig().getTools().containsKey(itemKey) && us.timinc.mc.cobblemon.shearems.Shearems.INSTANCE.getConfig().getTools().get(itemKey).contains(pokemonEntity.getPokemon().getSpecies().resourceIdentifier.toString())) {
                    didShear = true;
                    us.timinc.mc.cobblemon.shearems.Shearems.INSTANCE.dispenserShearing(pokemonEntity, itemStack);
                }
            }
        }
        if (!didShear) {
            cir.setReturnValue(itemStack);
            cir.cancel();
        }
    }
}
