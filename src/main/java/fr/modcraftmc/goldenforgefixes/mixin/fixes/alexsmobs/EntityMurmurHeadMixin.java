package fr.modcraftmc.goldenforgefixes.mixin.fixes.alexsmobs;

import com.github.alexthe666.alexsmobs.entity.EntityMurmur;
import com.github.alexthe666.alexsmobs.entity.EntityMurmurHead;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityMurmurHead.class)
public class EntityMurmurHeadMixin extends Mob {

    protected EntityMurmurHeadMixin(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    @Inject(method = "<init>(Lcom/github/alexthe666/alexsmobs/entity/EntityMurmur;)V", at = @At("RETURN"))
    public void inject(EntityMurmur parent, CallbackInfo ci) {
        this.moveControl = new CustomMoveController((EntityMurmurHead) (Object) this);
    }
}
