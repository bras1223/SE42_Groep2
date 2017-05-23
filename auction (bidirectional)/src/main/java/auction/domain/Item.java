package auction.domain;

import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import nl.fontys.util.Money;

@Entity
@NamedQueries({
    @NamedQuery(name = "Item.getAll", query = "select i from Item as i"),
    @NamedQuery(name = "Item.count", query = "select count(i) from Item as i"),
    @NamedQuery(name = "Item.findByID", query = "select i from Item as i where i.id = :id"),
    @NamedQuery(name = "Item.findByDescription", query = "select i from Item as i where i.description = :description")
})
public class Item implements Comparable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne (cascade = CascadeType.PERSIST)
    private User seller;
    
    @Embedded
    @AttributeOverride(name = "description", column = @Column(name = "CAT_DESCRIPTION"))
    private Category category;
    
    private String description;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    private Bid highest;

    public Item() {
        
    }
    
    public Item(User seller, Category category, String description) {
        this.seller = seller;
        this.category = category;
        this.description = description;
        
        seller.addItem(this);
    }

    public Long getId() {
        return id;
    }

    public User getSeller() {
        return seller;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Bid getHighestBid() {
        return highest;
    }

    public Bid newBid(User buyer, Money amount) {
        if (highest != null && highest.getAmount().compareTo(amount) >= 0) {
            return null;
        }
       
        highest = new Bid(this, buyer, amount);
        return highest;
    }

    public int compareTo(Object arg0) {
        Item item = (Item) arg0;
        if (this.id > item.id) {
            return 1;
        } else if (this.id < item.id) {
            return -1;
        } else {
            return 0;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null) {
            return false;
        }
        
        if (!(o instanceof Item)) {
            return false;
        }
        
        Item other = (Item) o;
        return id == null ? other.id == null : id.equals(other.id);
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }
    
}
