package meat.vm;

import java.util.Optional;

import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public class MeatObject implements TruffleObject {

	protected Optional<MeatObject> oracle;

	public MeatObject() {
		this.oracle = Optional.empty();
	}

	public Object value() {
		return this;
	}

	public MeatObject getOracle() {
		return this.oracle.orElseGet(() -> {
			MeatObject oracle = this.newOracle();
			this.oracle = Optional.of(oracle);
			return oracle;
		});
	}

	protected MeatObject newOracle() {
		return new MeatOracle(this);
	}

	public MeatObject respondTo(String selector, MeatObject arguments, MeatObject context) {
		// well... good luck
		arguments = new MeatList(
				new MeatObject[] { new MeatList(new MeatObject[] { new MeatString(selector), arguments }), context });
		return this.getOracle().respondTo("evaluateWith:in:", arguments, context);
	}

	@Override
	public ForeignAccess getForeignAccess() {
		// TODO Auto-generated method stub
		return null;
	}

}
