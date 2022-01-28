package dev.michaeltross.crows;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Crows.modId)
public class Crows {
    public static final String modId = "crows";
    private static final Logger LOGGER = LogManager.getLogger("Crows");

    public static Crows instance;

    public Crows() {

    }

}
