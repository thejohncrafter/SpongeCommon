/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.mixin.core.command.server;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.common.entity.EntityUtil;

import java.util.EnumSet;
import java.util.Set;

@Mixin(CommandTeleport.class)
public abstract class MixinCommandTeleport extends CommandBase {

    // This boolean is added in order to make minimal changes to 'execute'.
    // It is set to true if the events fired in 'func_189863_a' are not cancelled.
    // This allows us to prevent calling 'notifyCommandListener' if the event is cancelled.
    private static boolean shouldNotifyCommandListener = false;

    /**
     * @author Aaron1011 - August 15, 2016 - Prevent 'notifyCommandListener' from being called the event is cancelled
     */
    @Overwrite
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 4)
        {
            throw new WrongUsageException("commands.teleport.usage", new Object[0]);
        }
        else
        {
            Entity entity = getEntity(server, sender, args[0]);

            if (entity.worldObj != null)
            {
                int i = 4096;
                Vec3d vec3d = sender.getPositionVector();
                int j = 1;
                CommandBase.CoordinateArg commandbase$coordinatearg = parseCoordinate(vec3d.xCoord, args[j++], true);
                CommandBase.CoordinateArg commandbase$coordinatearg1 = parseCoordinate(vec3d.yCoord, args[j++], -4096, 4096, false);
                CommandBase.CoordinateArg commandbase$coordinatearg2 = parseCoordinate(vec3d.zCoord, args[j++], true);
                Entity entity1 = sender.getCommandSenderEntity() == null ? entity : sender.getCommandSenderEntity();
                CommandBase.CoordinateArg commandbase$coordinatearg3 = parseCoordinate(args.length > j ? (double)entity1.rotationYaw : (double)entity.rotationYaw, args.length > j ? args[j] : "~", false);
                ++j;
                CommandBase.CoordinateArg commandbase$coordinatearg4 = parseCoordinate(args.length > j ? (double)entity1.rotationPitch : (double)entity.rotationPitch, args.length > j ? args[j] : "~", false);
                // Sponge start - check shouldNotifyCommandListener before calling 'notifyCommandListener'

                // Guard against any possible re-entrance
                boolean shouldNotify = shouldNotifyCommandListener;

                doTeleport(entity, commandbase$coordinatearg, commandbase$coordinatearg1, commandbase$coordinatearg2, commandbase$coordinatearg3, commandbase$coordinatearg4);
                if (shouldNotifyCommandListener) {
                    notifyCommandListener(sender, this, "commands.tp.success.coordinates", new Object[] {entity.getName(), Double.valueOf(commandbase$coordinatearg.getResult()), Double.valueOf(commandbase$coordinatearg1.getResult()), Double.valueOf(commandbase$coordinatearg2.getResult())});
                }
                shouldNotifyCommandListener = shouldNotify;
                // Sponge end
            }
        }
    }

    /**
     * @author Aaron1011 - August 15, 2016 - Muliple modification points are needed, so an overwrite is easier
     */
    @Overwrite
    private static void doTeleport(Entity p_189862_0_, CommandBase.CoordinateArg p_189862_1_, CommandBase.CoordinateArg p_189862_2_, CommandBase.CoordinateArg p_189862_3_, CommandBase.CoordinateArg p_189862_4_, CommandBase.CoordinateArg p_189862_5_)
    {
        if (p_189862_0_ instanceof EntityPlayerMP)
        {
            Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.<SPacketPlayerPosLook.EnumFlags>noneOf(SPacketPlayerPosLook.EnumFlags.class);
            float f = (float)p_189862_4_.getAmount();

            if (p_189862_4_.isRelative())
            {
                set.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            }
            else
            {
                f = MathHelper.wrapDegrees(f);
            }

            float f1 = (float)p_189862_5_.getAmount();

            if (p_189862_5_.isRelative())
            {
                set.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
            }
            else
            {
                f1 = MathHelper.wrapDegrees(f1);
            }

            // Sponge start
            EntityPlayerMP player = (EntityPlayerMP) p_189862_0_;
            double x = p_189862_1_.getAmount();
            double y = p_189862_2_.getAmount();
            double z = p_189862_3_.getAmount();
            MoveEntityEvent.Teleport event = EntityUtil.handleDisplaceEntityTeleportEvent(player, x, y, z, f, f1);
            if (event.isCancelled()) {
                return;
            }

            p_189862_0_.dismountRidingEntity();
            Vector3d position = event.getToTransform().getPosition();
            ((EntityPlayerMP)p_189862_0_).connection.setPlayerLocation(position.getX(), position.getY(), position.getZ(), (float) event.getToTransform().getYaw(), (float) event.getToTransform().getPitch(), set);
            p_189862_0_.setRotationYawHead((float) event.getToTransform().getYaw());
            // Sponge end
        }
        else
        {
            float f2 = (float)MathHelper.wrapDegrees(p_189862_4_.getResult());
            float f3 = (float)MathHelper.wrapDegrees(p_189862_5_.getResult());
            f3 = MathHelper.clamp_float(f3, -90.0F, 90.0F);

            // Sponge start
            double x = p_189862_1_.getResult();
            double y = p_189862_2_.getResult();
            double z = p_189862_3_.getResult();
            MoveEntityEvent.Teleport event = EntityUtil.handleDisplaceEntityTeleportEvent(p_189862_0_, x, y, z, f2, f3);
            if (event.isCancelled()) {
                return;
            }

            Vector3d position = event.getToTransform().getPosition();
            p_189862_0_.setLocationAndAngles(position.getX(), position.getY(), position.getZ(), (float) event.getToTransform().getYaw(), (float) event.getToTransform().getPitch());
            p_189862_0_.setRotationYawHead((float) event.getToTransform().getYaw());
            // Sponge end
        }

        if (!(p_189862_0_ instanceof EntityLivingBase) || !((EntityLivingBase)p_189862_0_).isElytraFlying())
        {
            p_189862_0_.motionY = 0.0D;
            p_189862_0_.onGround = true;
        }

        // Sponge start - set 'shouldNotifyCommandListener' to 'true' if we make it to the end of the method (the event wasn't cancelled)
        shouldNotifyCommandListener = true;
        // Sponge end
    }

}
