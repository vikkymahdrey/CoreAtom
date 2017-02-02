package com.agiledge.atom.commons.collectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {

	public static <T> Collection<T> findAll(Collection<T> coll, Checker<T> chk) {
	     List<T> l = new  ArrayList<T>();
	    for (T obj : coll) {
	         if (chk.check(obj))
	             l.add(obj);
	    }
	    return l;
	}
}
