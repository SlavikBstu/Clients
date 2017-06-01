package productinfo;

import java.util.Date;


public class ProductInfo {

    int id;
    String company, annotation;
    Date releaseDate;

    public ProductInfo(int id, String company, String annotation, Date releaseDate) {
        this.id = id;
        this.company = company;
        this.annotation = annotation;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
