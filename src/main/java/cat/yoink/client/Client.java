package cat.yoink.client;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "client")
public class Client
{
    private static final Logger logger = LogManager.getLogger("Client");

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event)
    {
        logger.info("Main initialization!");
    }
}
