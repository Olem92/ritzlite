package net.runelite.client.plugins.implings;

import net.runelite.api.NPC;
import net.runelite.client.ui.ClientUI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImpNotifier {

    public static boolean impsToNotify(Impling imp){
        switch(imp){
            case DRAGON:
            case DRAGON_2:
            case LUCKY:
            case LUCKY_2:
            case CRYSTAL:
            case CRYSTAL_2:
            case CRYSTAL_3:
            case CRYSTAL_4:
            case CRYSTAL_5:
            case CRYSTAL_6:
            case CRYSTAL_7:
            case CRYSTAL_8:
            case CRYSTAL_9:
            case CRYSTAL_10:
            case CRYSTAL_11:
            case CRYSTAL_12:
            case CRYSTAL_13:
            case CRYSTAL_14:
            case CRYSTAL_15:
            case CRYSTAL_16:
            case CRYSTAL_17:
            return true;
            default:
                return false;
        }
    }
    static void registerImpling(String imp, int x, int y, int z,int world){
        try {
            new BufferedReader(new InputStreamReader(new URL("http://imushh.com/ritzlite/impling/registerImp.php?imp="+imp+"&x="+x+"&y="+y+"&z="+z+"&world="+world).openStream()));
        } catch (IOException e) {//https://galvek.com/impling/registerImp.php?imp=Lucky&x=1545&y=5465&z=5&world=317
            System.out.println("error");
        }
    }
}
