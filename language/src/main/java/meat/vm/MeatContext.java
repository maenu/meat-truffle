package meat.vm;

import java.math.BigDecimal;
import java.util.Map;

import meat.Utility;

public class MeatContext extends MeatDictionary {

	public static final String FRAME_SLOT = "context";

	public MeatContext(Map<Object, MeatObject> values) {
		super(values);
		this.values.put("context", new MeatVariable(this));
	}

	@Override
	protected MeatObject newOracle() {
		MeatOracle oracle = (MeatOracle) super.newOracle();
		oracle.methods.put("at:put:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(1))), context);
			MeatObject value = arguments.respondTo("at:", Utility.asList(new MeatNumber(new BigDecimal(2))), context);
			if (!(value instanceof MeatVariable)) {
				value = new MeatVariable(value);
			}
			this.values.put(key.value(), value);
			return value;
		});
		return oracle;
	}

}
