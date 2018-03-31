package meat.vm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class MeatVariable extends MeatObject {

	private MeatObject object;

	public MeatVariable(MeatObject object) {
		super();
		this.object = object;
	}

	@Override
	public MeatObject respondTo(String selector, MeatObject arguments, MeatObject context) {
		if (selector.equals("oracle") || selector.equals("oracle:") || selector.equals("object")
				|| selector.equals("object:")) {
			return super.respondTo(selector, arguments, context);
		}
		return this.object.respondTo(selector, arguments, context);
	}

	@Override
	public Object value() {
		return this.object.value();
	}

	@Override
	protected MeatObject newOracle() {
		Map<String, BiFunction<MeatObject, MeatObject, MeatObject>> methods = new HashMap<>();
		methods.put("object", (arguments, context) -> {
			return this.object;
		});
		methods.put("object:", (arguments, context) -> {
			this.object = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			return this;
		});
		return new MeatOracle(this, methods);
	}

}
