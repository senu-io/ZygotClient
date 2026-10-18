package com.zygot.cosmetics.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Robust animated GIF decoder and dynamic texture sequence manager for Minecraft 1.8.9 capes.
 * Decodes individual GIF frames, calculates per-frame delays, composites them onto a canvas,
 * and registers them as OpenGL dynamic textures.
 */
public class AnimatedCapeTexture {

    private final String id;
    private final ResourceLocation gifResource;
    private ResourceLocation[] frameLocations;
    private int[] frameDelays;
    private int totalDurationMs = 0;
    private boolean loaded = false;
    private boolean failed = false;
    private final long startTime = System.currentTimeMillis();

    public AnimatedCapeTexture(String id, ResourceLocation gifResource) {
        this.id = id;
        this.gifResource = gifResource;
    }

    public synchronized void load() {
        if (loaded || failed) return;

        try {
            InputStream in = null;
            try {
                in = Minecraft.getMinecraft().getResourceManager().getResource(gifResource).getInputStream();
            } catch (Exception e) {
                // Fallback to classloader stream
                String path = "/assets/" + gifResource.getResourceDomain() + "/" + gifResource.getResourcePath();
                in = getClass().getResourceAsStream(path);
            }

            if (in == null) {
                System.err.println("[Zygot] Could not find animated cape resource: " + gifResource);
                failed = true;
                return;
            }

            ImageInputStream stream = ImageIO.createImageInputStream(in);
            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            reader.setInput(stream);

            int numFrames = reader.getNumImages(true);
            if (numFrames <= 0) {
                failed = true;
                reader.dispose();
                stream.close();
                in.close();
                return;
            }

            int masterWidth = reader.getWidth(0);
            int masterHeight = reader.getHeight(0);

            IIOMetadata streamMeta = reader.getStreamMetadata();
            if (streamMeta != null) {
                try {
                    IIOMetadataNode root = (IIOMetadataNode) streamMeta.getAsTree(streamMeta.getNativeMetadataFormatName());
                    IIOMetadataNode screenDescriptor = (IIOMetadataNode) root.getElementsByTagName("LogicalScreenDescriptor").item(0);
                    if (screenDescriptor != null) {
                        if (screenDescriptor.hasAttribute("logicalScreenWidth")) {
                            masterWidth = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
                        }
                        if (screenDescriptor.hasAttribute("logicalScreenHeight")) {
                            masterHeight = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
                        }
                    }
                } catch (Exception ignored) {}
            }

            BufferedImage master = new BufferedImage(masterWidth, masterHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = master.createGraphics();

            List<BufferedImage> frameImages = new ArrayList<>();
            List<Integer> delays = new ArrayList<>();

            for (int i = 0; i < numFrames; i++) {
                BufferedImage frameImg = reader.read(i);
                IIOMetadata frameMeta = reader.getImageMetadata(i);
                IIOMetadataNode root = (IIOMetadataNode) frameMeta.getAsTree(frameMeta.getNativeMetadataFormatName());

                int x = 0;
                int y = 0;
                int delay = 100;
                String disposal = "none";

                try {
                    IIOMetadataNode imgDesc = (IIOMetadataNode) root.getElementsByTagName("ImageDescriptor").item(0);
                    if (imgDesc != null) {
                        if (imgDesc.hasAttribute("imageLeftPosition")) {
                            x = Integer.parseInt(imgDesc.getAttribute("imageLeftPosition"));
                        }
                        if (imgDesc.hasAttribute("imageTopPosition")) {
                            y = Integer.parseInt(imgDesc.getAttribute("imageTopPosition"));
                        }
                    }
                } catch (Exception ignored) {}

                try {
                    IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
                    if (gce != null) {
                        if (gce.hasAttribute("delayTime")) {
                            delay = Integer.parseInt(gce.getAttribute("delayTime")) * 10;
                        }
                        if (gce.hasAttribute("disposalMethod")) {
                            disposal = gce.getAttribute("disposalMethod");
                        }
                    }
                } catch (Exception ignored) {}

                if (delay <= 10) delay = 50;

                g2d.drawImage(frameImg, x, y, null);

                BufferedImage frameCopy = new BufferedImage(masterWidth, masterHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D copyG = frameCopy.createGraphics();
                copyG.drawImage(master, 0, 0, null);
                copyG.dispose();

                frameImages.add(frameCopy);
                delays.add(delay);

                if ("restoreToBackgroundColor".equalsIgnoreCase(disposal)) {
                    g2d.setBackground(new Color(0, 0, 0, 0));
                    g2d.clearRect(x, y, frameImg.getWidth(), frameImg.getHeight());
                }
            }

            g2d.dispose();
            reader.dispose();
            stream.close();
            in.close();

            // Register frames into Minecraft TextureManager
            this.frameLocations = new ResourceLocation[frameImages.size()];
            this.frameDelays = new int[delays.size()];
            this.totalDurationMs = 0;

            for (int i = 0; i < frameImages.size(); i++) {
                ResourceLocation loc = new ResourceLocation("zygot_dynamic", "cape_" + id + "_" + i);
                DynamicTexture dynTex = new DynamicTexture(frameImages.get(i));
                Minecraft.getMinecraft().getTextureManager().loadTexture(loc, dynTex);

                this.frameLocations[i] = loc;
                this.frameDelays[i] = delays.get(i);
                this.totalDurationMs += delays.get(i);
            }

            if (this.totalDurationMs <= 0) {
                this.totalDurationMs = 1000;
            }

            this.loaded = true;
        } catch (Exception e) {
            System.err.println("[Zygot] Failed to decode animated cape " + id + ": " + e.getMessage());
            failed = true;
        }
    }

    public ResourceLocation getCurrentTexture() {
        if (!loaded) {
            load();
        }

        if (failed || frameLocations == null || frameLocations.length == 0) {
            return gifResource;
        }

        if (frameLocations.length == 1 || totalDurationMs <= 0) {
            return frameLocations[0];
        }

        long elapsed = (System.currentTimeMillis() - startTime) % totalDurationMs;
        long cumulative = 0;

        for (int i = 0; i < frameLocations.length; i++) {
            cumulative += frameDelays[i];
            if (elapsed < cumulative) {
                return frameLocations[i];
            }
        }

        return frameLocations[0];
    }

    public boolean isLoaded() {
        return loaded;
    }
}
