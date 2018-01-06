package malm.tech.helpers.javahelpers;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CollectionTools {
		public static <T> Stream<T> enumAsIter(Enumeration<T> enumerate) {
		Stream<T> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return enumerate.hasMoreElements();
			}

			@Override
			public T next() {
				return enumerate.nextElement();
			}
		}, Spliterator.ORDERED), false);
		return stream;
	}
}
