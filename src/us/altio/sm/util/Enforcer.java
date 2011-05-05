package us.altio.sm.util;

import java.lang.reflect.ParameterizedType;



public class Enforcer<Type extends Exception> {

	/**
	 * http://stackoverflow.com/questions/75175/create-instance-of-generic-type-in-java
	 */
	public void enforce(boolean condition,String assertionText) throws IllegalStateException {
		if (!condition)
			throw new IllegalStateException(assertionText);
			/*try {
				throw (Type)((Class) ((ParameterizedType) this.getClass()
						.getGenericSuperclass()).getActualTypeArguments()[0])
						.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}*/
	}
	public void enforce(boolean condition) throws IllegalStateException {
		enforce(condition, "");
	}
	/*public void enforce(Predicate pred) throws Type {
		if (!pred.evaluate())
			try {
				throw (Type)((Class) ((ParameterizedType) this.getClass()
						.getGenericSuperclass()).getActualTypeArguments()[0])
						.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
	}*/
}
