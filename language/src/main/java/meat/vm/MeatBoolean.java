package meat.vm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class MeatBoolean extends MeatLiteralObject<Boolean> {

	public MeatBoolean(Boolean value) {
		super(value);
	}

	@Override
	protected MeatObject newOracle() {
		Map<String, BiFunction<MeatObject, MeatObject, MeatObject>> methods = new HashMap<>();
		methods.put("ifTrue:ifFalse:", (arguments, context) -> {
			MeatObject target = (this.value)
					? arguments.respondTo("at:", new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }),
							context)
					: arguments.respondTo("at:", new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(2)) }),
							context);
			return target.respondTo("evaluateWith:in:",
					new MeatList(new MeatObject[] { new MeatList(new MeatObject[] {}), context }), context);
		});
		methods.put("ifTrue:", (arguments, context) -> {
			MeatObject target = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			return this.respondTo("ifTrue:ifFalse:",
					new MeatList(
							new MeatObject[] { new MeatList(new MeatObject[] { target, MeatBlock.empty() }), context }),
					context);
		});
		methods.put("ifFalse:", (arguments, context) -> {
			MeatObject target = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			return this.respondTo("ifTrue:ifFalse:",
					new MeatList(
							new MeatObject[] { new MeatList(new MeatObject[] { MeatBlock.empty(), target }), context }),
					context);
		});
		return new MeatOracle(this, methods);
	}

}
