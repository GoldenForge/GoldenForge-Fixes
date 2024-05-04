package fr.modcraftmc.goldenforgefixes.mixin.fixes.immersive_petroleum;

import flaxbeard.immersivepetroleum.common.ReservoirRegionDataStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(ReservoirRegionDataStorage.class)
public class ReservoirRegionDataStorageMixin {

    @Shadow
    final Map<ReservoirRegionDataStorage.RegionPos, ReservoirRegionDataStorage.RegionData> regions = new ConcurrentHashMap<>();
}
