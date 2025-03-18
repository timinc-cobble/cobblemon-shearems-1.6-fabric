package us.timinc.mc.cobblemon.shearems.mixins.cobblemon;

@org.spongepowered.asm.mixin.Mixin(com.cobblemon.mod.common.entity.pokemon.PokemonEntity.class)
public class PokemonEntityMixin extends net.minecraft.world.entity.animal.ShoulderRidingEntity {
    protected PokemonEntityMixin(net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.animal.ShoulderRidingEntity> entityType, net.minecraft.world.level.Level world) {
        super(entityType, world);
    }

    @org.spongepowered.asm.mixin.injection.Inject(method = "mobInteract", at = @org.spongepowered.asm.mixin.injection.At(value = "HEAD"))
    public void mobInteractMixinHead(net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<net.minecraft.world.InteractionResult> cir) {
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            us.timinc.mc.cobblemon.shearems.Shearems.INSTANCE.interactShearing(
                    (com.cobblemon.mod.common.entity.pokemon.PokemonEntity) (Object) this,
                    serverPlayer.getItemInHand(hand),
                    serverPlayer
            );
        }
    }

    @org.spongepowered.asm.mixin.injection.Inject(method = "shear", at = @org.spongepowered.asm.mixin.injection.At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"), cancellable = true)
    public void sheared(net.minecraft.sounds.SoundSource shearedSoundCategory, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        ci.cancel();
        us.timinc.mc.cobblemon.shearems.Shearems.INSTANCE.pokemonShorn((com.cobblemon.mod.common.entity.pokemon.PokemonEntity) (Object) this);
    }

    @Override
    public boolean isFood(net.minecraft.world.item.ItemStack itemStack) {
        return false;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public net.minecraft.world.entity.AgeableMob getBreedOffspring(net.minecraft.server.level.ServerLevel serverLevel, net.minecraft.world.entity.AgeableMob ageableMob) {
        return null;
    }
}
