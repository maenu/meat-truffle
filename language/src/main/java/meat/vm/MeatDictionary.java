package meat.vm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import meat.MeatException;
import meat.Utility;

public class MeatDictionary extends MeatObject {

	protected Map<MeatObject, MeatObject> values;

	public MeatDictionary(Map<MeatObject, MeatObject> values) {
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
			for (MeatObject key_ : this.values.keySet()) {
				MeatBoolean equal = (MeatBoolean) key_.respondTo("=", Utility.asList(key), context);
				if (equal.value()) {
					return equal;
				}
			}
			return new MeatBoolean(false);
		});
		methods.put("at:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			// FIXME need to break recursion here
			boolean includes = false;
			for (MeatObject key_ : this.values.keySet()) {
				if (key_.equals(key)) {
					includes = true;
				}
			}
			return (new MeatBoolean(includes)).respondTo("ifTrue:ifFalse:",
					Utility.asList(new MeatBlock(new String[0], (arguments_, context_) -> {
						for (MeatObject key_ : this.values.keySet()) {
							// FIXME need to break recursion here
							if (key_.equals(key)) {
								return this.values.get(key_);
							}
						}
						throw new MeatException("key not found, should never happen");
					}), new MeatBlock(new String[0], (arguments_, context_) -> {
						return this.respondTo("at:put:", Utility.asList(key, new MeatObject()), context_);
					})), context);
		});
		methods.put("at:put:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			MeatObject value = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(2))), context);
			this.values.put(key, value);
			return value;
		});
		return new MeatOracle(this, methods);
	}

}
