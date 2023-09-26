package fr.modcraftmc.goldenforgefixes;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class GoldenForgeFixesMixinPlugin implements IMixinConfigPlugin {
    private String mixinPackage;

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage + ".";
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String path = mixinClassName.substring(this.mixinPackage.length());
        if (path.startsWith("fixes.environmental")) {
            return LoadingModList.get().getModFileById("environmental") != null;
        }
        if (path.startsWith("fixes.atmospheric")) {
            return LoadingModList.get().getModFileById("atmospheric") != null;
        }
        if (path.startsWith("fixes.chunky")) {
            return LoadingModList.get().getModFileById("chunky") != null;
        }
        if (path.startsWith("fixes.create")) {
            return LoadingModList.get().getModFileById("create") != null;
        }
        if (path.startsWith("fixes.twilight")) {
            return false;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
