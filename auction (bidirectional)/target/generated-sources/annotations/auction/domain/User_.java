package auction.domain;

import auction.domain.Item;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-24T10:22:04")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SetAttribute<User, Item> offeredItems;
    public static volatile SingularAttribute<User, Long> id;
    public static volatile SingularAttribute<User, String> email;

}