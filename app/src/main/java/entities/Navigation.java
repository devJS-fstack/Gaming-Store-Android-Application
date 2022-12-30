package entities;

public class Navigation {
    private int icon;
    private String title;
    private boolean isActive;

    public Navigation() {
    }

    public Navigation(int icon, String title, Boolean isActive) {
        this.icon = icon;
        this.title = title;
        this.isActive = isActive;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Category{" +
                "icon=" + icon +
                ", title='" + title + '\'' +
                '}';
    }
}
