package fr.modcraftmc.goldenforgefixes.mixin.fixes.alexsmobs;

import com.github.alexthe666.alexsmobs.entity.EntityMurmurHead;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

class CustomMoveController extends MoveControl {
    private EntityMurmurHead parentEntity = null;


    public CustomMoveController(Mob parentMod) {
        super(parentMod);
        this.parentEntity = (EntityMurmurHead) parentMod;
    }

    public void tick() {
        if (!parentEntity.isPulledIn()) {
            float angle = 0.017453292F * (this.parentEntity.yBodyRot + 90.0F);
            float radius = (float)Math.sin((double)((float)this.parentEntity.tickCount * 0.2F)) * 2.0F;
            double extraX = (double)(radius * Mth.sin((float)(Math.PI + (double)angle)));
            double extraY = (double)radius * -Math.cos((double)angle - 1.5707963267948966);
            double extraZ = (double)(radius * Mth.cos(angle));
            Vec3 strafPlus = new Vec3(extraX, extraY, extraZ);
            if (this.operation == Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.wantedX - this.parentEntity.getX(), this.wantedY - this.parentEntity.getY(), this.wantedZ - this.parentEntity.getZ());
                double d0 = vector3d.length();
                double width = this.parentEntity.getBoundingBox().getSize();
                Vec3 shimmy = Vec3.ZERO;
                LivingEntity attackTarget = this.parentEntity.getTarget();
                if (attackTarget != null && this.parentEntity.horizontalCollision) {
                    shimmy = new Vec3(0.0, 0.005, 0.0);
                }

                Vec3 vector3d1 = vector3d.scale(this.speedModifier * 0.05 / d0);
                this.parentEntity.setDeltaMovement(this.parentEntity.getDeltaMovement().add(vector3d1.add(strafPlus.scale(0.003 * Math.min(d0, 100.0)).add(shimmy))));
                if (attackTarget == null) {
                    if (d0 >= width) {
                        Vec3 deltaMovement = this.parentEntity.getDeltaMovement();
                        this.parentEntity.setYRot(-((float) Mth.atan2(deltaMovement.x, deltaMovement.z)) * 57.295776F);
                        this.parentEntity.yBodyRot = this.parentEntity.getYRot();
                    }
                } else {
                    double d2 = attackTarget.getX() - this.parentEntity.getX();
                    double d1 = attackTarget.getZ() - this.parentEntity.getZ();
                    this.parentEntity.setYRot(-((float)Mth.atan2(d2, d1)) * 57.295776F);
                    this.parentEntity.yBodyRot = this.parentEntity.getYRot();
                }
            } else if (this.operation == Operation.WAIT) {
                this.parentEntity.setDeltaMovement(this.parentEntity.getDeltaMovement().add(strafPlus.scale(0.003)));
            }

        }
    }
}
