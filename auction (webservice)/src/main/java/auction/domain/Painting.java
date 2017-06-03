/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luuk
 */
@XmlRootElement
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Painting extends Item implements Serializable {
    
    private String title;
    private String painter;

    public Painting() {
        
    }
    
    public Painting(String title, String painter) {
        this.title = title;
        this.painter = painter;
    }

    public Painting(String title, String painter, User seller, Category category, String description) {
        super(seller, category, description);
        this.title = title;
        this.painter = painter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPainter() {
        return painter;
    }

    public void setPainter(String painter) {
        this.painter = painter;
    }
    
    
}
