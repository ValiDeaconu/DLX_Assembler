package Util;

import java.io.Serializable;

public class ApplicationSettings implements Serializable {
    private String outputFileExtension;
    private String applicationStyle;

    public ApplicationSettings(String outputFileExtension, String applicationStyle) {
        this.outputFileExtension = outputFileExtension;
        this.applicationStyle = applicationStyle;
    }

    public ApplicationSettings() {
    }

    public String getOutputFileExtension() {
        return outputFileExtension;
    }

    public void setOutputFileExtension(String outputFileExtension) {
        this.outputFileExtension = outputFileExtension;
    }

    public String getApplicationStyle() {
        return applicationStyle;
    }

    public void setApplicationStyle(String applicationStyle) {
        this.applicationStyle = applicationStyle;
    }
}
