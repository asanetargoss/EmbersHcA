package teamroots.embers.network.message;

import java.util.Random;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.particle.ParticleUtil;

public class MessageEmberBurstFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	
	public MessageEmberBurstFX(){
		super();
	}
	
	public MessageEmberBurstFX(double x, double y, double z){
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
	}

    public static class MessageHolder implements IMessageHandler<MessageEmberBurstFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageEmberBurstFX message, final MessageContext ctx) {
    		Minecraft.getMinecraft().addScheduledTask(new Runnable() {public void run() {
	    		World world = Minecraft.getMinecraft().world;
				for (int k = 0; k < 80; k ++){
					ParticleUtil.spawnParticleGlow(world, (float)message.posX, (float)message.posY, (float)message.posZ, 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 255, 64, 16, 4.0f, 24);
				}
    		}});
    		return null;
        }
    }

}
