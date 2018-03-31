package meat.vm;

public class MeatLiteralObject<V> extends MeatObject {

	// TODO maybe final
	protected V value;

	public MeatLiteralObject(V value) {
		super();
		this.value = value;
	}

	@Override
	public Object value() {
		return this.value;
	}

}
