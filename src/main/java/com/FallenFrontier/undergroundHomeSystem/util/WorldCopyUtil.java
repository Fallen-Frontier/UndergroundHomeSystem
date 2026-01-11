package com.FallenFrontier.undergroundHomeSystem.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public final class WorldCopyUtil {

    private WorldCopyUtil() {}

    /**
     * Recursively copies a world from source to target.
     * Deletes session.lock and uid.dat in the target to allow Paper to load it.
     */
    public static void copy(File source, File target) throws IOException {
        if (!source.exists()) {
            throw new IOException("Template world does not exist: " + source.getAbsolutePath());
        }

        if (!target.exists()) {
            target.mkdirs();
        }

        // Walk source directory recursively
        Files.walk(source.toPath()).forEach(path -> {
            try {
                Path relative = source.toPath().relativize(path);
                Path targetPath = target.toPath().resolve(relative);

                if (Files.isDirectory(path)) {
                    if (!Files.exists(targetPath)) {
                        Files.createDirectories(targetPath);
                    }
                } else {
                    Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed copying template world: " + e.getMessage(), e);
            }
        });

        // Remove lock files so Paper can load the world
        File sessionLock = new File(target, "session.lock");
        if (sessionLock.exists()) sessionLock.delete();

        File uidDat = new File(target, "uid.dat");
        if (uidDat.exists()) uidDat.delete();
    }
}
