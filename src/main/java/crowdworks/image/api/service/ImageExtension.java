package crowdworks.image.api.service;

public enum ImageExtension {
    JPG, PNG, JPEG, GIF;

    public static boolean isValidExtension(String filename) {
        for (ImageExtension extension : ImageExtension.values()) {
            if (filename.toUpperCase().endsWith("." + extension.name())) {
                return true;
            }
        }
        return false;
    }
}
