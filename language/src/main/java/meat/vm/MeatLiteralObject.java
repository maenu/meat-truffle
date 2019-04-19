package meat.vm;

public class MeatLiteralObject<V> extends MeatObject {

	// TODO maybe final
	protected V value;

	public MeatLiteralObject(V value) {
		super();
		this.value = value;
	}

	@Override
	public V value() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MeatLiteralObject)) {
			return false;
		}
		MeatLiteralObject<?> other = (MeatLiteralObject<?>) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

}
