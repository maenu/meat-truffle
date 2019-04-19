package meat.vm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import meat.Utility;

public class MeatNumber extends MeatLiteralObject<BigDecimal> {

	public MeatNumber(BigDecimal value) {
		super(value);
	}

	@Override
	protected MeatObject newOracle() {
		Map<String, BiFunction<MeatObject, MeatObject, MeatObject>> methods = new HashMap<>();
		methods.put("+", (arguments, context) -> {
			MeatObject other = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			return new MeatNumber(this.value.add((BigDecimal) other.value()));
		});
		methods.put("-", (arguments, context) -> {
			MeatObject other = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			return new MeatNumber(this.value.subtract((BigDecimal) other.value()));
		});
		methods.put("<=", (arguments, context) -> {
			MeatObject other = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			return new MeatBoolean(this.value.compareTo((BigDecimal) other.value()) <= 0);
		});
		methods.put("<", (arguments, context) -> {
			MeatObject other = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			return new MeatBoolean(this.value.compareTo((BigDecimal) other.value()) < 0);
		});
		methods.put(">=", (arguments, context) -> {
			MeatObject other = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			return new MeatBoolean(this.value.compareTo((BigDecimal) other.value()) >= 0);
		});
		methods.put(">", (arguments, context) -> {
			MeatObject other = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			return new MeatBoolean(this.value.compareTo((BigDecimal) other.value()) > 0);
		});
		return new MeatOracle(this, methods);
	}

}
