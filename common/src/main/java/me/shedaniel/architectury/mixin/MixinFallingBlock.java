package me.shedaniel.architectury.mixin;

import me.shedaniel.architectury.event.events.BlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FallingBlock.class)
public abstract class MixinFallingBlock extends Block {
    
    public MixinFallingBlock(Properties properties) {
        super(properties);
        throw new IllegalStateException();
    }
    
    @Inject(method = "onLand", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void handleLand(Level level, BlockPos pos, BlockState fallState, BlockState landOn, FallingBlockEntity entity, CallbackInfo ci) {
        BlockEvent.FALLING_LAND.invoker().onLand(level, pos, fallState, landOn, entity);
    }
    
}
