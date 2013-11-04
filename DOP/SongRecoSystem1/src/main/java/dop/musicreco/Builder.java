package dop.musicreco;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Builder {

	private final DBObject query;
	public Builder() {
        query = new BasicDBObject();
    }

	public Builder set(final String key, final Object object)
	{
		 addToQuery(Operators.SET, key, object);
		 return this;
    }

	public Builder inc(final String key, final int value)
	{
		 addToQuery(Operators.INC, key, value);
		 return this;
	}

	public Builder unset(final String key)
	{
	     addToQuery(Operators.UNSET, key, 1);
	     return this;
	}

	public boolean isEmpty()
	{
	     return query.keySet().isEmpty();
	}

	public DBObject get()								//Returns the current built query
	{
	     return query;
    }

	private void addToQuery(final String operator, final String key, final Object object)
	{
	    final BasicDBObject subQuery = query.get(operator) != null ?

        (BasicDBObject) query.get(operator) :
        new BasicDBObject();
        query.put(operator, subQuery.append(key, object));
    }

	public class Operators
	{

	    public final static String SET = "$set";

	    public final static String INC = "$inc";

	    public final static String UNSET = "$unset";
	}


}
