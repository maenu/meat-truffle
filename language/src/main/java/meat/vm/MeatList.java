package meat.vm;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class MeatList extends MeatLiteralObject<MeatObject[]> {

	public MeatList(MeatObject[] value) {
		super(value);
	}

	@Override
	public String toString() {
		return Arrays.toString(this.value);
	}

	@Override
	protected MeatObject newOracle() {
		Map<String, BiFunction<MeatObject, MeatObject, MeatObject>> methods = new HashMap<>();
		methods.put("at:", (arguments, context) -> {
			// FIXME need to break recursion here
			int index = ((BigDecimal) (((MeatList) arguments).value[0]).value()).intValue() - 1;
			MeatObject value = (0 <= index && index < this.value.length && this.value[index] != null)
					? this.value[index]
					: new MeatObject();
			this.value = Arrays.copyOf(this.value, Math.max(this.value.length, index + 1));
			this.value[index] = value;
			return value;
		});
		methods.put("at:put:", (arguments, context) -> {
			// FIXME need to break recursion here
			int index = ((BigDecimal) (((MeatList) arguments).value[0]).value()).intValue() - 1;
			MeatObject value = ((MeatList) arguments).value[1];
			this.value = Arrays.copyOf(this.value, Math.max(this.value.length, index));
			this.value[index] = value;
			return this;
		});
		return new MeatOracle(this, methods);
	}

}
