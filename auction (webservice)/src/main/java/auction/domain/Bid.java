package auction.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import nl.fontys.util.FontysTime;
import nl.fontys.util.Money;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Bid implements Serializable{

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    @Column (name = "bid_time")
    private FontysTime time;
    
    @ManyToOne
    private User buyer;
    
    @Embedded
    private Money amount;

    @OneToOne (mappedBy = "highest", cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private Item belongsToItem;
    
    public Bid() {
        
    }
    
    public Bid(Item item, User buyer, Money amount) {
        this.buyer = buyer;
        this.amount = amount;
        this.belongsToItem = item;
    }

    public FontysTime getTime() {
        return time;
    }

    public User getBuyer() {
        return buyer;
    }

    public Money getAmount() {
        return amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getBelongsToItem() {
        return belongsToItem;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bid other = (Bid) obj;
        if (!Objects.equals(this.time, other.time)) {
            return false;
        }
        if (!Objects.equals(this.buyer, other.buyer)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        if (!Objects.equals(this.belongsToItem, other.belongsToItem)) {
            return false;
        }
        return true;
    }
    
    
    
}
