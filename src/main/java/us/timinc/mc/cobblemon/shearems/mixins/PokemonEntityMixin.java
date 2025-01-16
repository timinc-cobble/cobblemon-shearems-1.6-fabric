package us.timinc.mc.cobblemon.shearems.mixins;

@org.spongepowered.asm.mixin.Mixin(com.cobblemon.mod.common.entity.pokemon.PokemonEntity.class)
public class PokemonEntityMixin extends net.minecraft.entity.passive.TameableShoulderEntity {
    protected PokemonEntityMixin(net.minecraft.entity.EntityType<? extends net.minecraft.entity.passive.TameableShoulderEntity> entityType, net.minecraft.world.World world) {
        super(entityType, world);
    }

    @org.spongepowered.asm.mixin.injection.Inject(method = "sheared", at = @org.spongepowered.asm.mixin.injection.At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"), cancellable = true)
    public void sheared(net.minecraft.sound.SoundCategory shearedSoundCategory, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        ci.cancel();
        us.timinc.mc.cobblemon.shearems.Shearems.INSTANCE.dropShorn((com.cobblemon.mod.common.entity.pokemon.PokemonEntity)(Object)this);
    }

    @Override
    public boolean isBreedingItem(net.minecraft.item.ItemStack itemStack) {
        return false;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public net.minecraft.entity.passive.PassiveEntity createChild(net.minecraft.server.world.ServerWorld serverWorld, net.minecraft.entity.passive.PassiveEntity passiveEntity) {
        return null;
    }
}
