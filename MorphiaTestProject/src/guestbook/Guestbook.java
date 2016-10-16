package guestbook;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.AlsoLoad;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.PostLoad;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;

@Entity
public class Guestbook {
	@Id
	ObjectId id;
	String titel;
	String text;
	String userName;
}
