package de.unibi.util;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

/**
 * Loads different types of assets for future use in other classes.
 *
 * @author Andi
 */
public class Assets {

    public static Material mainMat;
    public static Material wall_mat;
    public static Material stone_mat;
    public static Texture heightMapImage;
    public static Material matTerrain;
    public static Material unshaded;

    public static void loadAssets(AssetManager assetManager) {
        assetManager.registerLocator("assets", FileLocator.class);

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

        float dirtScale = 16;

        matTerrain = new Material(assetManager,
                "terrain/terrainf.j3md");

        Texture grassf = assetManager.loadTexture("terrain/eben2.png");

        grassf.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("fDifMap", grassf);
        matTerrain.setFloat("scale", dirtScale);
        matTerrain.setBoolean("mapping", false);
        // DIRT texture
        Texture dirtf = assetManager.loadTexture("terrain/berg2.png");
        dirtf.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("mDifMap", dirtf);

        unshaded = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        unshaded.setColor("Color", new ColorRGBA(251f / 255f, 130f / 255f, 0f, 0.6f));
        unshaded.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

    }
}
