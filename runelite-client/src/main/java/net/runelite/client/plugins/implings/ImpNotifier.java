package net.runelite.client.plugins.implings;

import net.runelite.client.ui.ClientUI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImpNotifier {
    public static void notifyDiscord(String msg) throws IOException {
        DiscordWebhook webhook = new DiscordWebhook("https://discordapp.com/api/webhooks/649371243434344450/AcHS-JtUUdzz3dhuBTJuqaQcmXXg_O8juNFaoE8UQIA0TjzwLSgZhO7iuGD1tQAUeV2u");
        webhook.setContent(msg);
        //webhook.setAvatarUrl("https://your.awesome/image.png");
        //webhook.setUsername("Custom Usernames!");
        //webhook.setTts(true);
        /*webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Title")
                .setDescription("This is a description")
                .setColor(Color.RED)
                .addField("1st Field", "Inline", true)
                .addField("2nd Field", "Inline", true)
                .addField("3rd Field", "No-Inline", false)
                .setThumbnail("https://kryptongta.com/images/kryptonlogo.png")
                .setFooter("Footer text", "https://kryptongta.com/images/kryptonlogodark.png")
                .setImage("https://kryptongta.com/images/kryptontitle2.png")
                .setAuthor("Author Name", "https://kryptongta.com", "https://kryptongta.com/images/kryptonlogowide.png")
                .setUrl("https://kryptongta.com"));
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setDescription("Just another added embed object!"));*/
        webhook.execute(); //Handle exception
    }
    public static boolean impsToNotify(Impling imp){
        if(imp == Impling.DRAGON){
            return true;
        }
        if(imp == Impling.DRAGON_2){
            return true;
        }
        if(imp == Impling.LUCKY){
            return true;
        }
        if(imp == Impling.LUCKY_2){
            return true;
        }
        if(imp == Impling.BABY){//Test
            return true;
        }
        return false;
    }
    void takeSceenie(ClientUI gui){

        BufferedImage img = new BufferedImage(gui.getFrame().getWidth(), gui.getFrame().getHeight(), BufferedImage.TYPE_INT_RGB);
        gui.getFrame().paint(img.getGraphics());
        File outputfile = new File("saved.png");
        try {
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
