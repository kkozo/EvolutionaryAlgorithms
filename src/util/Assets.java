package util;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.texture.Texture;

/**
 * Loads different types of assets for future use in other classes.
 * @author Andi
 */
public class Assets {

    public static Material mainMat;
    public static Material wall_mat;
    public static Material stone_mat;
    public static Texture heightMapImage;
    public static Material mat_terrain;

    public static void loadAssets(AssetManager assetManager) {
        mainMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key1 = new TextureKey("Textures/mysterybox.jpg");
        key1.setGenerateMips(true);
        Texture tex = assetManager.loadTexture(key1);
        mainMat.setTexture("ColorMap", tex);


        wall_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key = new TextureKey("Textures/mysterybox.jpg");
        key.setGenerateMips(true);
        Texture texx = assetManager.loadTexture(key);
        wall_mat.setTexture("ColorMap", texx);

        stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        stone_mat.setTexture("ColorMap", tex2);

        heightMapImage = assetManager.loadTexture(
                "Textures/Terrain/splat/flats128.png");
        mat_terrain = new Material(assetManager,
                "Common/MatDefs/Terrain/Terrain.j3md");

        mat_terrain.setTexture("Alpha", assetManager.loadTexture(
                "Textures/Terrain/splat/alphamap3.png"));
        
        Texture grass = assetManager.loadTexture(
                "Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);

        Texture dirt = assetManager.loadTexture(
                "Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex2", dirt);
        mat_terrain.setFloat("Tex2Scale", 32f);

        
        Texture rock = assetManager.loadTexture(
                "Textures/Terrain/splat/road.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex3", rock);
        mat_terrain.setFloat("Tex3Scale", 128f);
    }
}
