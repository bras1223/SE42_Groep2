package auction.domain;

import java.io.Serializable;
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
import nl.fontys.util.FontysTime;
import nl.fontys.util.Money;

@Entity
public class Bid implements Serializable{

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    @Column (name = "bid_time")
    private FontysTime time;
    
    @ManyToOne
    private User buyer;
    
    @Embedded
    private Money amount;

    @OneToOne (mappedBy = "highest")
    @JoinColumn(nullable=false)
    private Item belongsToItem;
    
    public Bid() {
        
    }
    
    public Bid(User buyer, Money amount) {
        this.buyer = buyer;
        this.amount = amount;
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
}
