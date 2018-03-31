package meat.vm;

public class MeatString extends MeatLiteralObject<String> {

	public MeatString(String value) {
		super(value);
	}

	@Override
	public String toString() {
		return String.format("'%s'", super.toString());
	}

}
