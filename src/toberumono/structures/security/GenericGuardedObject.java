package toberumono.structures.security;

import java.security.Guard;
import java.security.GuardedObject;

/**
 * @author Toberumono
 * @param <T>
 *            the type guarded by the {@link GenericGuardedObject}
 */
public class GenericGuardedObject<T> extends GuardedObject {
	
	/**
	 * Basic constructor.
	 * @param object the object to protect 
	 * @param guard the {@link Guard}
	 */
	public GenericGuardedObject(T object, Guard guard) {
		super(object, guard);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T getObject() {
		return (T) super.getObject();
	}
}
