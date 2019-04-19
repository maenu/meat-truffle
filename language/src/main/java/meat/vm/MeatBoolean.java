package meat.vm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import meat.Utility;

public class MeatBoolean extends MeatLiteralObject<Boolean> {

	public MeatBoolean(Boolean value) {
		super(value);
	}

	@Override
	protected MeatObject newOracle() {
		Map<String, BiFunction<MeatObject, MeatObject, MeatObject>> methods = new HashMap<>();
		methods.put("ifTrue:ifFalse:", (arguments, context) -> {
			MeatObject target = (this.value)
					? arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context)
					: arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(2))), context);
			return target.respondTo("evaluateWith:in:", Utility.asList(Utility.asList(), context), context);
		});
		methods.put("ifTrue:", (arguments, context) -> {
			MeatObject target = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			return this.respondTo("ifTrue:ifFalse:", Utility.asList(Utility.asList(target, MeatBlock.empty()), context),
					context);
		});
		methods.put("ifFalse:", (arguments, context) -> {
			MeatObject target = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			return this.respondTo("ifTrue:ifFalse:", Utility.asList(Utility.asList(MeatBlock.empty(), target), context),
					context);
		});
		return new MeatOracle(this, methods);
	}

}
