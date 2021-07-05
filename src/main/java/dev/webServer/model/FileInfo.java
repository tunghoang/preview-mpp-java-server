package dev.webServer.model;

public class FileInfo {
    private String name;
    private String url;
    private String image;
    private String json;

    public FileInfo(String name, String url, String image, String json) {
        this.setName(name);
        this.setUrl(url);
        this.setImage(image);
        this.setJson(json);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
