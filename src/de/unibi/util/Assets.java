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

    public static Material stone_mat;
    public static Texture heightMapImage129;
    public static Texture heightMapImage257;
    public static Texture heightMapImage513;
    public static Material matTerrainMint;
    public static Material matTerrainGreen;
    public static Material unshaded;

    public static void loadAssets(AssetManager assetManager) {
        assetManager.registerLocator("assets", FileLocator.class);


        stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        stone_mat.setTexture("ColorMap", tex2);

        heightMapImage129 = assetManager.loadTexture(
                "Textures/Terrain/splat/flats128.png");

        heightMapImage257 = assetManager.loadTexture(
                "Textures/Terrain/splat/flats256.png");

        heightMapImage513 = assetManager.loadTexture(
                "Textures/Terrain/splat/flats.png");

        float dirtScale = 16;

        matTerrainMint = new Material(assetManager,
                "terrain/terrainf.j3md");

        Texture grassBright = assetManager.loadTexture("terrain/eben2.png");
        grassBright.setWrap(WrapMode.Repeat);
        matTerrainMint.setTexture("fDifMap", grassBright);
        matTerrainMint.setFloat("scale", dirtScale);
        matTerrainMint.setBoolean("mapping", false);
        // DIRT texture
        Texture mountainDark = assetManager.loadTexture("terrain/berg2.png");
        mountainDark.setWrap(WrapMode.Repeat);
        matTerrainMint.setTexture("mDifMap", mountainDark);

        
        matTerrainGreen = new Material(assetManager,
                "terrain/terrainf.j3md");

        Texture grass = assetManager.loadTexture("terrain/eben.png");
        grass.setWrap(WrapMode.Repeat);
        matTerrainGreen.setTexture("fDifMap", grass);
        matTerrainGreen.setFloat("scale", dirtScale);
        matTerrainGreen.setBoolean("mapping", false);
        // DIRT texture
        Texture mountain = assetManager.loadTexture("terrain/berg2.png");
        mountain.setWrap(WrapMode.Repeat);
        matTerrainGreen.setTexture("mDifMap", mountain);
        
        unshaded = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        unshaded.setColor("Color", new ColorRGBA(251f / 255f, 130f / 255f, 0f, 0.6f));
        unshaded.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

    }
}
