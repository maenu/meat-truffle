package meat.vm;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import meat.Utility;

public class MeatOracle extends MeatBlock {

	private MeatObject self;

	public MeatOracle(MeatObject self) {
		this(self, Collections.emptyMap());
	}

	public MeatOracle(MeatObject self, Map<String, BiFunction<MeatObject, MeatObject, MeatObject>> methods) {
		super(new String[0], (a, b) -> null);
		this.self = self;
		this.function = (arguments, context) -> {
			// FIXME had to break recursion
			MeatObject selector = ((MeatList) arguments).value[0];
			MeatObject arguments_ = ((MeatList) arguments).value[1];
			return this.methods.get(selector.value()).apply(arguments_, context);
		};
		this.methods.put("oracle", (arguments, context) -> {
			return this;
		});
		this.methods.put("oracle:", (arguments, context) -> {
			MeatObject oracle = context.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			this.self.oracle = Optional.of(oracle);
			return this.self;
		});
		this.methods.putAll(methods);
	}

}
