package cat.yoink.loader;

import cat.yoink.client.Client;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Mod(modid = "loader")
public class Loader implements IFMLLoadingPlugin
{
    @SuppressWarnings("unchecked")
    public Loader() throws Exception
    {
        Field field = LaunchClassLoader.class.getDeclaredField("resourceCache");
        field.setAccessible(true);

        Map<String, byte[]> cache = (Map<String, byte[]>) field.get(Launch.classLoader);

        URL url = new URL("https://yoink.site/client.jar");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        InputStream inputStream = httpURLConnection.getInputStream();

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null)
        {
            String name = zipEntry.getName();

            if (!name.endsWith(".class")) continue;

            name = name.substring(0, name.length() - 6);
            name = name.replace('/', '.');

            ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
            int bytesRead;
            byte[] tempBuffer = new byte[8192 * 2];
            while ((bytesRead = zipInputStream.read(tempBuffer)) != -1) streamBuilder.write(tempBuffer, 0, bytesRead);

            cache.put(name, streamBuilder.toByteArray());
        }
    }

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event)
    {
        new Client().initialize(event);
    }

    @Override public String getModContainerClass() { return null; }
    @Override public String[] getASMTransformerClass() { return new String[]{ }; }
    @Override public String getSetupClass() { return null; }
    @Override public void injectData(Map<String, Object> data) { }
    @Override public String getAccessTransformerClass() { return null; }
}
