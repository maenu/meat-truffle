package meat.vm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class MeatBlock extends MeatObject {

	public static MeatBlock empty() {
		MeatBlock block = new MeatBlock(new String[0], (a, b) -> {
			return null;
		});
		block.function = (a, b) -> block;
		return block;
	}

	protected final String[] parameters;
	// TODO should be final
	protected BiFunction<MeatObject, MeatObject, MeatObject> function;
	protected final Map<String, BiFunction<MeatObject, MeatObject, MeatObject>> methods;

	public MeatBlock(String[] parameters, BiFunction<MeatObject, MeatObject, MeatObject> function) {
		super();
		this.parameters = parameters;
		this.function = function;
		this.methods = new HashMap<>();
		this.methods.put("evaluateWith:in:", (arguments, context) -> {
			// FIXME should not assert, just shortcut maybe? may lead to stack-overflow
			MeatObject context_ = ((MeatList) arguments).value[1];
			MeatObject arguments_ = ((MeatList) arguments).value[0];
			return this.execute(arguments_, context_);
		});
		this.methods.put("oracle", (arguments, context) -> {
			return this.getOracle();
		});
		this.methods.put("oracle:", (arguments, context) -> {
			MeatObject oracle = context.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			this.oracle = Optional.of(oracle);
			return this;
		});
		this.methods.put("=", (arguments, context) -> {
			MeatObject other = context.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			return new MeatBoolean(this.value() == other.value());
		});
	}

	@Override
	public MeatObject respondTo(String selector, MeatObject arguments, MeatObject context) {
		if (this.oracle.isPresent()) {
			return super.respondTo(selector, arguments, context);
		}
		return this.methods.get(selector).apply(arguments, context);
	}

	protected MeatObject execute(MeatObject arguments, MeatObject context) {
		// adds arguments to context by name
		IntStream.range(0, this.parameters.length).forEach(i -> {
			String parameter = this.parameters[i];
			MeatObject value = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(i + 1)) }), context);
			context.respondTo("at:put:", new MeatList(new MeatObject[] { new MeatString(parameter), value }), context);
		});
		return this.function.apply(arguments, context);
	}

}
