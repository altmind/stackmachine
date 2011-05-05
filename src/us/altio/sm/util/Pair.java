package us.altio.sm.util;

public class Pair<T1, T2> {
    private T1 field1;
    private T2 field2;

    public Pair(T1 field1, T2 field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public T1 getField1() {
        return field1;
    }

    public void setField1(T1 field1) {
        this.field1 = field1;
    }

    public T2 getField2() {
        return field2;
    }

    public void setField2(T2 field2) {
        this.field2 = field2;
    }
    
    public int hashCode()
    {
    	//please refer to http://www.concentric.net/~Ttwang/tech/inthash.htm part "Knuth's Multiplicative Method".
    	//yet it was not supposed to be used for achieving avalanche effect;
    	return (int) ((2654435761L*field1.hashCode())+field2.hashCode());
    }
    
    public boolean equals(Object o)
    {
    	if (!(o instanceof Pair<?,?>)) return false;
    	if (o == null) return false;
    	try
    	{
    		T1 o1=((Pair<T1,T2>) o).getField1();
    		T2 o2=((Pair<T1,T2>) o).getField2();
    		if ((field1==null) ^ (o1==null)) return false;
    		if ((field2==null) ^ (o2==null)) return false;
    		return ((field1!=null)?field1.equals(o1):true && (field2!=null)?field2.equals(o2):true);
    	}
    	catch (ClassCastException e) {
    		return false;
		}
    	catch (NullPointerException e)
    	{
    		return false;
    	}
    }
} 