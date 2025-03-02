package us.timinc.mc.cobblemon.shearems.mixins;

@org.spongepowered.asm.mixin.Mixin(com.cobblemon.mod.common.entity.pokemon.PokemonEntity.class)
public class PokemonEntityMixin extends net.minecraft.world.entity.animal.ShoulderRidingEntity {
    net.minecraft.world.entity.player.Player shornBy = null;
    net.minecraft.world.item.ItemStack shornWith = null;

    protected PokemonEntityMixin(net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.animal.ShoulderRidingEntity> entityType, net.minecraft.world.level.Level world) {
        super(entityType, world);
    }

    @org.spongepowered.asm.mixin.injection.Inject(method = "mobInteract", at = @org.spongepowered.asm.mixin.injection.At(value = "HEAD"))
    public void mobInteractMixinHead(net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<net.minecraft.world.InteractionResult> cir) {
        shornBy = player;
        shornWith = player.getItemInHand(hand);
    }

    @org.spongepowered.asm.mixin.injection.Inject(method = "mobInteract", at = @org.spongepowered.asm.mixin.injection.At(value = "TAIL"))
    public void mobInteractMixinTail(net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<net.minecraft.world.InteractionResult> cir) {
        shornBy = null;
        shornWith = null;
    }

    @org.spongepowered.asm.mixin.injection.Inject(method = "shear", at = @org.spongepowered.asm.mixin.injection.At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"), cancellable = true)
    public void sheared(net.minecraft.sounds.SoundSource shearedSoundCategory, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        if (shornBy == null || shornWith == null) return;
        ci.cancel();
        us.timinc.mc.cobblemon.shearems.Shearems.INSTANCE.dropShorn((com.cobblemon.mod.common.entity.pokemon.PokemonEntity) (Object) this, shornBy, shornWith);
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
