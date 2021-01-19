package me.shedaniel.architectury.plugin.forge;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ArchitecturyMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        
    }
    
    @Override
    public String getRefMapperConfig() {
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
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
        // Inject our own sugar
        switch (mixinClassName) {
            case "me.shedaniel.architectury.mixin.forge.MixinAbstractRecipeSerializer":
                targetClass.superName = "net/minecraftforge/registries/ForgeRegistryEntry";
                for (MethodNode method : targetClass.methods) {
                    if (Objects.equals(method.name, "<init>")) {
                        for (AbstractInsnNode insnNode : method.instructions) {
                            if (insnNode.getOpcode() == Opcodes.INVOKESPECIAL && insnNode instanceof MethodInsnNode) {
                                MethodInsnNode node = (MethodInsnNode) insnNode;
                                if (Objects.equals(node.name, "<init>") && Objects.equals(node.owner, "java/lang/Object")) {
                                    node.owner = "net/minecraftforge/registries/ForgeRegistryEntry";
                                    break;
                                }
                            }
                        }
                    }
                }
                String recipeSerializer = targetClass.interfaces.get(0);
                if (targetClass.signature != null) {
                    targetClass.signature = targetClass.signature.replace("Ljava/lang/Object;", "Lnet/minecraftforge/registries/ForgeRegistryEntry<L" + recipeSerializer + "<*>;>");
                }
                break;
        }
    }
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        
    }
}
