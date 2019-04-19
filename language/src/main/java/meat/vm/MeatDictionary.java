package meat.vm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import meat.Utility;

public class MeatDictionary extends MeatObject {

	protected Map<Object, MeatObject> values;

	public MeatDictionary(Map<Object, MeatObject> values) {
		super();
		this.values = values;
	}

	@Override
	protected MeatObject newOracle() {
		Map<String, BiFunction<MeatObject, MeatObject, MeatObject>> methods = new HashMap<>();
		methods.put("asContext", (arguments, context) -> {
			return new MeatContext(new HashMap<>(this.values));
		});
		methods.put("includes:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			return new MeatBoolean(this.values.containsKey(key.value()));
		});
		methods.put("at:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			MeatObject includes = this.respondTo("includes:", Utility.asList(key), context);
			return includes.respondTo("ifTrue:ifFalse:",
					Utility.asList(new MeatBlock(new String[0], (arguments_, context_) -> {
						return this.values.get(key.value());
					}), new MeatBlock(new String[0], (arguments_, context_) -> {
						return this.respondTo("at:put:", Utility.asList(key, new MeatObject()), context_);
					})), context);
		});
		methods.put("at:put:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			MeatObject value = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(2))), context);
			this.values.put(key.value(), value);
			return value;
		});
		return new MeatOracle(this, methods);
	}

}
